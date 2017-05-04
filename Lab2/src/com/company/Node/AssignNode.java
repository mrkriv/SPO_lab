package com.company.Node;

public class AssignNode extends ValueBody
{
	String name;

	public AssignNode(String name)
	{
		this.name = name;
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level;i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append(" Variable: ");
		sb.append(name);
		sb.append("\n");

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}
