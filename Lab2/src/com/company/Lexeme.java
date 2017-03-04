package com.company;

import java.util.regex.Pattern;

class Lexeme
{
	private Pattern pattern;
	private final int priority;
	private String name;

	Lexeme(String name, String regex, int priority)
	{
		pattern = Pattern.compile(regex);
		this.name = name;
		this.priority = priority;
	}

	boolean isMatch(String string)
	{
		return pattern.matcher(string).find();
	}

	java.util.regex.Pattern getPattern()
	{
		return pattern;
	}

	String getName()
	{
		return name;
	}

	int getPriority()
	{
		return priority;
	}

	@Override
	public String toString()
	{
		return name;
	}
}