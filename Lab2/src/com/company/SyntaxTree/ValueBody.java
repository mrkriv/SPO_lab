package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ValueBody extends BodyNode
{
	@Override
	public void compile(List<Integer> opcodes, List<String> varTable) throws BuildExeption
	{
		Stack<Node> operands = new Stack<>();
		Stack<Node> operators = new Stack<>();
		Stack<Node> result = new Stack<>();


		for(Node node : childs)
		{

		}

		if(result.isEmpty())
			throw new BuildExeption("Требуется значение");

		if(!operands.isEmpty())
			throw new BuildExeption("Некорректное математическое выражение, ожидался оператор");

		if(!operators.isEmpty())
			throw new BuildExeption("Некорректное математическое выражение, ожидался операнд");

		childs = new ArrayList<>(result);

		super.compile(opcodes, varTable);
	}
}
