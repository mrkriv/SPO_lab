package com.company;

import java.util.regex.Pattern;

class Lexeme
{
	private final Pattern pattern;
	private final int priority;
	private final Terminals type;

	Lexeme(Terminals type, String regex, int priority)
	{
		pattern = Pattern.compile(regex);
		this.type = type;
		this.priority = priority;
	}

	public boolean isMatch(String string)
	{
		return pattern.matcher(string).find();
	}

	public Terminals getType()
	{
		return type;
	}

	public int getPriority()
	{
		return priority;
	}

	@Override
	public String toString()
	{
		return type.toString();
	}
}