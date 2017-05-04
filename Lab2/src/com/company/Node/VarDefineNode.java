package com.company.Node;

public class VarDefineNode extends Node
{
	private String type;
	private String name;

	public VarDefineNode(String type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Name: ");
		sb.append(name);
		sb.append(" Type: ");
		sb.append(type);
		sb.append("\n");

		return sb;
	}
}
