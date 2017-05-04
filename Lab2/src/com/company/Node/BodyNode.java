package com.company.Node;

import java.util.ArrayList;
import java.util.List;

public class BodyNode extends Node
{
	protected List<Node> childs = new ArrayList<>();

	public void addChild(Node node)
	{
		childs.add(node);
	}

	public int getSize()
	{
		return childs.size();
	}

	public void backtrack(int size)
	{
		while(childs.size() > size)
			childs.remove(size - 1);
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}