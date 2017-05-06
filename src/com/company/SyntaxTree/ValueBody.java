package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.List;
import java.util.Stack;

public class ValueBody extends BodyNode
{
	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		Stack<MathOperationNode> operators = new Stack<>();

		for(Node node : childs)
		{
			if(node instanceof MathOperationNode)
			{
				MathOperationNode operator = ((MathOperationNode) node);

				while(!operators.isEmpty() && operators.lastElement().getPrior() >= operator.getPrior())
				{
					operators.pop().compile(opcodes, varTable, methodTable);
				}

				operators.push(operator);
			}
			else
			{
				node.compile(opcodes, varTable, methodTable);
			}
		}

		while(!operators.isEmpty())
		{
			operators.pop().compile(opcodes, varTable, methodTable);
		}
	}
}
