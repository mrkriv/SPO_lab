package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;

import java.util.Stack;

public class ValueBodyNode extends BodyNode
{
	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		Stack<MathOperationNode> operators = new Stack<>();

		for(Node node : childs)
		{
			if(node instanceof MathOperationNode)
			{
				MathOperationNode operator = ((MathOperationNode) node);

				while(!operators.isEmpty() && operators.lastElement().getPrior() >= operator.getPrior())
				{
					operators.pop().compile(m);
				}

				operators.push(operator);
			}
			else
			{
				node.compile(m);
			}
		}

		while(!operators.isEmpty())
		{
			operators.pop().compile(m);
		}
	}
}
