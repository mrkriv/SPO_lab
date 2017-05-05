package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ValueBody extends BodyNode
{
	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		Stack<MathOperationNode> operators = new Stack<>();
		Stack<Node> result = new Stack<>();

		for(Node node : childs)
		{
			if(node instanceof MathOperationNode)
			{
				MathOperationNode operator = ((MathOperationNode)node);

				while(!operators.isEmpty() && operators.lastElement().getPrior() < operator.getPrior())
				{
					result.push(node);
				}

				operators.push(operator);
			}
			else
			{
				result.push(node);
			}
		}

		result.addAll(operators);

		if(result.isEmpty())
			throw new BuildExeption("Требуется значение");

		childs = new ArrayList<>(result);

		super.compile(opcodes, varTable, methodTable);
	}
}
