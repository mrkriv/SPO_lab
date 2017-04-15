package com.company;

public class Variable
{
	private String type;
	private String name;

	public Variable(String type, String name)
	{
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString()
	{
		return String.format("{%s} %s", type, name);
	}
}