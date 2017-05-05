package com.company.SyntaxTree;

import com.company.BuildExeption;
import java.util.List;

public class MathOperationNode extends Node
{
	private String name;

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable) throws BuildExeption
	{

	}

	public MathOperationNode(String name) {
		this.name = name;
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" '");
		sb.append(name);
		sb.append("'");
		sb.append("\n");

		return sb;
	}
}
