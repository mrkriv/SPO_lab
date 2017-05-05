package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.List;

public abstract class Node
{
	public abstract void compile(List<Integer> opcodes, List<String> varTable) throws BuildExeption;

	@Override
	public String toString()
	{
		return print(0).toString();
	}

	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level;i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append('\n');

		return sb;
	}
}
