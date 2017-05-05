package com.company;

public class BuildExeption extends Exception
{
	public BuildExeption(String message)
	{
		super(message);
	}

	public BuildExeption(String format, Object... args)
	{
		super(String.format(format, args));
	}
}