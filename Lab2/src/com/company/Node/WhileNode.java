package com.company.Node;

public class WhileNode extends BodyNode
{
	public ConditionNode condition = new ConditionNode();

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level; i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append('\n');

		sb.append(condition.print(level + 1));

		for(int i = 0; i < level; i++)
			sb.append("\t");
		sb.append("Then\n");

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}