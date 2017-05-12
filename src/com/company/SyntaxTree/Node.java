package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;

public abstract class Node
{
	protected abstract void compile(Compiler m) throws BuildExeption;

	@Override
	public String toString()
	{
		return print(0).toString();
	}

	StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level;i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append('\n');

		return sb;
	}
}
