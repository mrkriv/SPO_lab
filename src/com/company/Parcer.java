package com.company;

import com.company.SyntaxTree.*;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/*
	* expr -> method | LINE_END
	* expr_body -> var_def | var_unar | while | if | var_assign | call_e | body | return_op | LINE_END
	* body -> BODY_OPEN expr_body BODY_CLOSE
	* method -> VAR_TYPE NAME BRACED_OPEN (var_def_simple (COMMA var_def_simple)* )? BRACED_CLOSE body
	* var_def_simple -> VAR_TYPE NAME
	* var_def -> var_def_simple (ASSIGN_OP value)? LINE_END
	* var_assign -> VAR_NAME ASSIGN_OP value LINE_END
	* var_unar -> VAR_NAME UNAR_OP LINE_END
	* call_e -> call LINE_END
	* call -> NAME BRACED_OPEN (value (COMMA value)* )? BRACED_CLOSE LINE_END
	* value -> ( BRACED_OPEN value BRACED_OPEN ) | ( (const_value|NAME|call) (MATH_OP value)? )
	* while -> WHILE_OP BRACED_OPEN condition BRACED_CLOSE body
	* if -> IF_OP BRACED_OPEN condition BRACED_CLOSE body
	* condition -> value CONDITION_OP value
	* const_value -> DIGIT | DIGIT_NATURAL | BOOLEAN
	* return_op -> RETURN VAR_NAME? LINE_END
*/

class Parcer
{
	private List<Token> tokens;
	private List<Pair<Integer, String>> errors;
	private int index;
	private Stack<Node> nodes;

	RootBodyNode run(List<Token> tokens) throws BuildExeption
	{
		errors = new ArrayList<>();
		nodes = new Stack<>();
		this.tokens = tokens;

		RootBodyNode root = new RootBodyNode();
		nodes.push(root);

		expr();

		return root;
	}

	// expr -> method | LINE_END
	private void expr() throws BuildExeption
	{
		while(tokens.size() > index)
		{
			errors.clear();

			if(check(Terminals.BODY_CLOSE))
				return;

			if(	tryStep(Parcer::method) ||
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

	// expr_body -> var_def | var_unar | while | if | var_assign | call_e | body | return_op | LINE_END
	private void expr_body() throws BuildExeption
	{
		while(tokens.size() > index)
		{
			errors.clear();

			if(check(Terminals.BODY_CLOSE))
				return;

			if(	tryStep(Parcer::var_def) ||
				tryStep(Parcer::var_assign)	||
				tryStep(Parcer::var_unar) ||
				tryStep(Parcer::if_operator) ||
				tryStep(Parcer::while_operator)	||
				tryStep(Parcer::call_e) ||
				tryStep(Parcer::body) ||
				tryStep(Parcer::return_op) ||
				tryStep(Parcer::line_end))
				continue;

			step();
		}
	}

	// body -> BODY_OPEN expr BODY_CLOSE
	private void body() throws BuildExeption
	{
		addAndPushNode(new BodyNode());
		checkAndStep(Terminals.BODY_OPEN);
		expr_body();
		checkAndStep(Terminals.BODY_CLOSE);
		nodes.pop();
	}

	// method -> VAR_TYPE NAME BRACED_OPEN (var_def_simple (COMMA var_def_simple)* )? BRACED_CLOSE body
	private void method() throws BuildExeption
	{
		String type = checkAndStep(Terminals.VAR_TYPE);
		String name = checkAndStep(Terminals.NAME);
		checkAndStep(Terminals.BRACED_OPEN);

		MethodNode node = new MethodNode(type, name);
		addAndPushNode(node);

		if(!check(Terminals.BRACED_CLOSE))
		{
			nodes.push(node.VariablesBody);
			do
			{
				var_def_simple();
			}
			while(stepIF(Terminals.COMMA));

			nodes.pop();
		}

		checkAndStep(Terminals.BRACED_CLOSE);

		checkAndStep(Terminals.BODY_OPEN);
		expr_body();
		checkAndStep(Terminals.BODY_CLOSE);
		nodes.pop();
	}

	// while -> WHILE_OP BRACED_OPEN condition BRACED_CLOSE body
	private void while_operator() throws BuildExeption
	{
		WhileNode loop = new WhileNode();
		addAndPushNode(loop);

		checkAndStep(Terminals.WHILE_OP);
		checkAndStep(Terminals.BRACED_OPEN);
		loop.condition = condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		checkAndStep(Terminals.BODY_OPEN);
		expr_body();
		checkAndStep(Terminals.BODY_CLOSE);
		nodes.pop();
	}

	// IF_OP BRACED_OPEN condition BRACED_CLOSE body
	private void if_operator() throws BuildExeption
	{
		BranchNode branch = new BranchNode();
		addAndPushNode(branch);

		checkAndStep(Terminals.IF_OF);
		checkAndStep(Terminals.BRACED_OPEN);
		branch.condition = condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		checkAndStep(Terminals.BODY_OPEN);
		expr_body();
		checkAndStep(Terminals.BODY_CLOSE);

		if(stepIF(Terminals.ELSE))
		{
			branch.elseBody = new BodyNode();
			nodes.push(branch.elseBody);

			checkAndStep(Terminals.BODY_OPEN);
			expr_body();
			checkAndStep(Terminals.BODY_CLOSE);

			nodes.pop();
		}

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

	// call -> call LINE_END
	private void call_e()throws BuildExeption
	{
		call();
		checkAndStep(Terminals.LINE_END);
	}

	// call -> NAME BRACED_OPEN (value (COMMA value)* )? BRACED_CLOSE
	private void call()throws BuildExeption
	{
		String name = checkAndStep(Terminals.NAME);
		checkAndStep(Terminals.BRACED_OPEN);

		addAndPushNode(new CallNode(name));

		if(!check(Terminals.BRACED_CLOSE))
		{
			do
			{
				addAndPushNode(new ValueBodyNode());
				value();
				nodes.pop();
			}
			while(stepIF(Terminals.COMMA));
		}

		checkAndStep(Terminals.BRACED_CLOSE);
		nodes.pop();
	}

	// var_def_simple -> VAR_TYPE NAME
	private void var_def_simple() throws BuildExeption
	{
		String type = checkAndStep(Terminals.VAR_TYPE);
		String name = checkAndStep(Terminals.NAME);

		addNode(new VarDefineNode(type, name));
	}

	// var_def -> var_def_simple ASSIGN_OP value LINE_END
	private void var_def() throws BuildExeption
	{
		var_def_simple();
		String name = tokens.get(index - 1).getValue();

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
		String operator = checkAndStep(Terminals.ASSIGN_OP);

		addAndPushNode(new AssignNode(name));

		if(!Objects.equals(operator, "="))
		{
			addNode(new VarNameNode(name));
			addNode(new MathOperationNode(operator.substring(0, 1)));
			addAndPushNode(new ValueBodyNode());
			value();
			nodes.pop();
		}
		else
			value();

		nodes.pop();

		checkAndStep(Terminals.LINE_END);
	}

	// var_unar -> VAR_NAME UNAR_OP LINE_END
	private void var_unar() throws BuildExeption
	{
		String name = checkAndStep(Terminals.NAME);
		String operator = checkAndStep(Terminals.UNAR_OP);

		addNode(new VarUnarNode(name, operator));

		checkAndStep(Terminals.LINE_END);
	}

	// return -> RETURN VAR_NAME? LINE_END
	private void return_op() throws BuildExeption
	{
		addAndPushNode(new ReturnNode());

		if(!check(Terminals.LINE_END))
			value();

		nodes.pop();

		checkAndStep(Terminals.LINE_END);
	}

	// value -> ( BRACED_OPEN value BRACED_OPEN ) | ( (const_value|NAME|call) (MATH_OP value)? )
	private void value() throws BuildExeption
	{
		if(stepIF(Terminals.BRACED_OPEN))
		{
			addAndPushNode(new ValueBodyNode());
			value();
			checkAndStep(Terminals.BRACED_CLOSE);
			nodes.pop();
		}
		else
		{
			if(check(Terminals.NAME))
			{
				if(!tryStep(Parcer::call))
				{
					addNode(new VarNameNode(current().getValue()));
					step();
				}
			}
			else const_value();
		}

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

	private boolean tryStep(ParcerFunction method)
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
