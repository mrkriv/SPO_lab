package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;

import java.util.ArrayList;
import java.util.List;

public class BodyNode extends Node
{
	final List<Node> childs = new ArrayList<>();

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		int varCount = m.variables.size();

		for(Node node : childs)
		{
			node.compile(m);
		}

		while(m.variables.size() > varCount)
			m.variables.remove(m.variables.size() - 1);
	}

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
			childs.remove(childs.size() - 1);
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