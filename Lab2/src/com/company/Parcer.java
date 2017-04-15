package com.company;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

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
	private List<Variable> Variable;
	private List<Pair<Integer, String>> errors;
	private int index;
	public List<String> DebugLog;

	Parcer(List<Token> tokens)
	{
		this.tokens = tokens;
	}

	void run() throws ParceExeption
	{
		DebugLog = new ArrayList<>();
		Variable = new ArrayList<>();
		errors = new ArrayList<>();

		expr();
	}

	// expr -> var_def | while | if | var_assign | LINE_END
	private void expr() throws ParceExeption
	{
		while(tokens.size() > index)
		{
			DebugLog.forEach(System.out::print);
			DebugLog.clear();
			errors.clear();

			if(	tryStep(Parcer::var_def) ||
				tryStep(Parcer::var_assign)	||
				tryStep(Parcer::if_operator) ||
				tryStep(Parcer::while_operator)	||
				tryStep(Parcer::line_end))
				continue;

			if(tryStep(Parcer::body_close))
				return;

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
			//throw new ParceExeption("Не верная синтаксическая конструкция");
		}
	}

	// while -> WHILE_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	private void while_operator() throws ParceExeption
	{
		log("Begin while loop");

		checkAndStep(Terminals.WHILE_OP);
		checkAndStep(Terminals.BRACED_OPEN);
		condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		log("While body:");
		checkAndStep(Terminals.BODY_OPEN);
		expr();
		//checkAndStep(Terminals.BODY_CLOSE);

		log("End while loop");
	}

	// IF_OP BRACED_OPEN condition BRACED_CLOSE BODY_OPEN expr BODY_CLOSE
	private void if_operator() throws ParceExeption
	{
		log("Begin if block");

		checkAndStep(Terminals.IF_OF);
		checkAndStep(Terminals.BRACED_OPEN);
		condition();
		checkAndStep(Terminals.BRACED_CLOSE);

		log("If block body:");
		checkAndStep(Terminals.BODY_OPEN);
		expr();
		//checkAndStep(Terminals.BODY_CLOSE);

		log("End if block");
	}

	// condition -> value CONDITION_OP value
	private void condition() throws ParceExeption
	{
		logf("Condition:\n\t");
		value();

		logf("\tOperator %s\n", current().getValue());
		checkAndStep(Terminals.CONDITION_OP);
		value();
	}

	// var_def -> VAR_TYPE NAME ASSIGN_OP value LINE_END
	private void var_def() throws ParceExeption
	{
		String type = checkAndStep(Terminals.VAR_TYPE);
		String name = checkAndStep(Terminals.NAME);

		logf("Define var %s as %s\n", name, type);

		Variable.add(new Variable(type, name));

		if(stepIF(Terminals.ASSIGN_OP))
		{
			logf("\tAssign (%s) to var %s\n\t\t", current().getValue(), name);
			value();
		}

		checkAndStep(Terminals.LINE_END);
	}


	// var_assign -> VAR_NAME ASSIGN_OP value LINE_END
	private void var_assign() throws ParceExeption
	{
		String name = checkAndStep(Terminals.NAME);
		checkAndStep(Terminals.ASSIGN_OP);

		logf("Assign (%s) to var %s\n\t", tokens.get(index-1).getValue(), name);
		value();

		checkAndStep(Terminals.LINE_END);
	}


	// (const_value|NAME) (MATH_OP value)?
	private void value() throws ParceExeption
	{
		if(check(Terminals.NAME))
		{
			logf("Value %s (name)\n", current().getValue());
			step();
		}
		else
			const_value();

		if(stepIF(Terminals.MATH_OP))
		{
			logf("\t %s ", tokens.get(index-1).getValue());
			value();
		}
	}


	// DIGIT | DIGIT_NATURAL | BOOLEAN
	private void const_value() throws ParceExeption
	{
		if(check(Terminals.DIGIT))
		{
			logf("Value %s (digit)\n", current().getValue());
			step();
		}
		else if(check(Terminals.DIGIT_NATURAL))
		{
			logf("Value %s (natural digit)\n", current().getValue());
			step();
		}
		else if(check(Terminals.BOOLEAN))
		{
			logf("Value %s (boolean)\n", current().getValue());
			step();
		}
		else
			throw new ParceExeption("Неверный тип, ожидалось число или логическое значение");
	}


	private void line_end() throws ParceExeption
	{
		checkAndStep(Terminals.LINE_END);
	}

	private void body_close() throws ParceExeption
	{
		checkAndStep(Terminals.BODY_CLOSE);
	}


	// Не лезь, убьет

	@FunctionalInterface
	interface ParcerFunction {
		void invoke(Parcer self) throws ParceExeption;
	}

	private boolean tryStep(ParcerFunction method) throws ParceExeption
	{
		int startIndex = index;
		int debugLogIndex = DebugLog.size();

		try
		{
			method.invoke(this);
		}
		catch(ParceExeption exeption)
		{
			errors.add(new Pair<>(index, exeption.getMessage()));
			index = startIndex;

			while(DebugLog.size() > debugLogIndex)
				DebugLog.remove(DebugLog.size() - 1);

			return false;
		}
		return true;
	}

	private boolean stepIF(Terminals type) throws ParceExeption
	{
		if(check(type))
		{
			index++;
			return true;
		}
		return false;
	}

	private boolean check(Terminals type)throws ParceExeption
	{
		return current().getLexeme().getType() == type;
	}

	private String checkAndStep(Terminals type) throws ParceExeption
	{
		Token token = current();

		if(token.getLexeme().getType() != type)
			throw new ParceExeption("Неверная синтаксическая конструкция, ожидалось " + type.toString());

		step();
		return token.getValue();
	}

	private Token current() throws ParceExeption
	{
		if(index >= tokens.size())
			throw new ParceExeption("Непредвиденный конец инструкций");

		return tokens.get(index);
	}

	private void step()
	{
		++index;
	}

	private void logf(String format, Object ... args)
	{
		DebugLog.add(String.format(format, args));
	}

	private void log(String message)
	{
		DebugLog.add(message + "\n");
	}
}
