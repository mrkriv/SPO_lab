package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.ArrayList;
import java.util.List;

public class BodyNode extends Node
{
	protected List<Node> childs = new ArrayList<>();

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		int varCount = varTable.size();

		for(Node node : childs)
		{
			node.compile(opcodes, varTable, methodTable);
		}

		while(varTable.size() > varCount)
			varTable.remove(varTable.size() - 1);
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