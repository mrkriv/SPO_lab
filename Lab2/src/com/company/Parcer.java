package com.company;

import com.company.SyntaxTree.*;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
	* expr -> var_def | while | if | var_assign | LINE_END
	* var_def -> VAR_TYPE NAME ASSIGN_OP value LINE_END
	* var_assign -> VAR_NAME ASSIGN_OP value LINE_END
	* value -> (const_value|NAME) (MATH_OP value)?
	* while -> WHILE_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	* if -> IF_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	* condition -> value CONDITION_OP value
	* const_value -> DIGIT | DIGIT_NATURAL | BOOLEAN
*/

public class Parcer
{
	private List<Token> tokens;
	private List<Pair<Integer, String>> errors;
	private int index;
	private Stack<Node> nodes;

	BodyNode run(List<Token> tokens) throws BuildExeption
	{
		errors = new ArrayList<>();
		nodes = new Stack<>();
		this.tokens = tokens;

		BodyNode root = new RootBody();
		nodes.push(root);

		expr();

		return root;
	}

	// expr -> var_def | while | if | var_assign | LINE_END
	private void expr() throws BuildExeption
	{
		while(tokens.size() > index)
		{
			errors.clear();

			if(check(Terminals.BODY_CLOSE))
				return;

			if(	tryStep(Parcer::var_def) ||
				tryStep(Parcer::var_assign)	||
				tryStep(Parcer::if_operator) ||
				tryStep(Parcer::while_operator)	||
				tryStep(Parcer::line_end))
				continue;

			if(!errors.isEmpty())
			{
				errors.sort((a, b) -> -a.getKey().compareTo(b.getKey()));

				index = errors.get(0).getKey();
				String msg = errors.get(0).getValue();
				System.out.printf("%s, символ %d (%s)\n", msg, current().getStart(), current().getValue());
			}
			else
			{
				System.out.printf("Неверная синтаксическая конструкция, символ %d (%s)\n", current().getStart(), current().getValue());
			}

			step();
		}
	}

	// while -> WHILE_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	private void while_operator() throws BuildExeption
	{
		WhileNode loop = new WhileNode();
		addAndPushNode(loop);

		checkAndStep(Terminals.WHILE_OP);
		checkAndStep(Terminals.BRACED_OPEN);
		loop.condition = condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		checkAndStep(Terminals.BODY_OPEN);
		expr();
		checkAndStep(Terminals.BODY_CLOSE);
		nodes.pop();
	}

	// IF_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	private void if_operator() throws BuildExeption
	{
		BranchNode branch = new BranchNode();
		addAndPushNode(branch);

		checkAndStep(Terminals.IF_OF);
		checkAndStep(Terminals.BRACED_OPEN);
		branch.condition = condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		checkAndStep(Terminals.BODY_OPEN);
		expr();
		checkAndStep(Terminals.BODY_CLOSE);
		nodes.pop();
	}

	// condition -> value CONDITION_OP value
	private ConditionNode condition() throws BuildExeption
	{
		ConditionNode node = new ConditionNode();

		nodes.push(node.left);
		value();
		nodes.pop();

		node.operator = checkAndStep(Terminals.CONDITION_OP);

		nodes.push(node.right);
		value();
		nodes.pop();

		return node;
	}

	// var_def -> VAR_TYPE NAME ASSIGN_OP value LINE_END
	private void var_def() throws BuildExeption
	{
		String type = checkAndStep(Terminals.VAR_TYPE);
		String name = checkAndStep(Terminals.NAME);

		addNode(new VarDefineNode(type, name));

		if(stepIF(Terminals.ASSIGN_OP))
		{
			addAndPushNode(new AssignNode(name));
			value();
			nodes.pop();
		}

		checkAndStep(Terminals.LINE_END);
	}

	// var_assign -> VAR_NAME ASSIGN_OP value LINE_END
	private void var_assign() throws BuildExeption
	{
		String name = checkAndStep(Terminals.NAME);
		checkAndStep(Terminals.ASSIGN_OP);

		addAndPushNode(new AssignNode(name));
		value();
		nodes.pop();

		checkAndStep(Terminals.LINE_END);
	}

	// (const_value|NAME) (MATH_OP value)?
	private void value() throws BuildExeption
	{
		if(check(Terminals.NAME))
		{
			addNode(new NameNode(current().getValue()));
			step();
		}
		else
			const_value();

		if(stepIF(Terminals.MATH_OP))
		{
			addNode(new MathOperationNode(tokens.get(index-1).getValue()));
			value();
		}
	}

	// DIGIT | DIGIT_NATURAL | BOOLEAN
	private void const_value() throws BuildExeption
	{
		if(	check(Terminals.DIGIT) ||
			check(Terminals.DIGIT_NATURAL)||
			check(Terminals.BOOLEAN))
		{
			addNode(new ConstantNode(current().getValue(), current().getLexeme().getType()));
			step();
		}
		else
			throw new BuildExeption("Неверный тип, ожидалось число или логическое значение");
	}

	private void line_end() throws BuildExeption
	{
		checkAndStep(Terminals.LINE_END);
	}


	// Не лезь, убьет

	@FunctionalInterface
	interface ParcerFunction {
		void invoke(Parcer self) throws BuildExeption;
	}

	private boolean tryStep(ParcerFunction method) throws BuildExeption
	{
		int startIndex = index;
		int nodesIndex = nodes.size();
		int nodesChildIndex = nodes.lastElement() instanceof BodyNode ? ((BodyNode) nodes.lastElement()).getSize() : 0;

		try
		{
			method.invoke(this);
		}
		catch(BuildExeption exeption)
		{
			//backtrack...

			errors.add(new Pair<>(index, exeption.getMessage()));
			index = startIndex;

			while(nodes.size() > nodesIndex)
				nodes.pop();

			if(nodes.lastElement() instanceof BodyNode)
				((BodyNode) nodes.lastElement()).backtrack(nodesChildIndex);

			return false;
		}
		return true;
	}

	private boolean stepIF(Terminals type) throws BuildExeption
	{
		if(check(type))
		{
			step();
			return true;
		}
		return false;
	}

	private boolean check(Terminals type)throws BuildExeption
	{
		return current().getLexeme().getType() == type;
	}

	private String checkAndStep(Terminals type) throws BuildExeption
	{
		Token token = current();

		if(token.getLexeme().getType() != type)
			throw new BuildExeption("Неверная синтаксическая конструкция, ожидалось " + type.toString());

		step();
		return token.getValue();
	}

	private Token current() throws BuildExeption
	{
		if(index >= tokens.size())
			throw new BuildExeption("Непредвиденный конец инструкций");

		return tokens.get(index);
	}

	private void step()
	{
		++index;
	}

	private void addNode(Node node)
	{
		((BodyNode)nodes.lastElement()).addChild(node);
	}

	private void addAndPushNode(Node node)
	{
		((BodyNode)nodes.lastElement()).addChild(node);
		nodes.push(node);
	}
}
