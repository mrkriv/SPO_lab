package com.company.Node;

import com.company.Terminals;

public class ConstantNode extends Node
{
	private String name;
	private Terminals type;

	public ConstantNode(String name, Terminals type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Value: ");
		sb.append(name);
		sb.append(" Type: ");
		sb.append(type);
		sb.append("\n");

		return sb;
	}
}
