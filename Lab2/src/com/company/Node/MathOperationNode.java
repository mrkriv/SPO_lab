package com.company.Node;

public class MathOperationNode extends Node
{
	private String name;

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
