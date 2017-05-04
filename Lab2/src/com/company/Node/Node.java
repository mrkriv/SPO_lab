package com.company.Node;

public abstract class Node
{
	//abstract void compile();


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
