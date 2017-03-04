package com.company;

class Token
{
	private final Lexeme lexeme;
	private final int start;
	private final int end;
	private final String value;

	Token(Lexeme lexeme, String matchResult, int start, int end)
	{
		this.value = matchResult;
		this.lexeme = lexeme;
		this.start = start;
		this.end = end;
	}

	Lexeme getLexeme()
	{
		return lexeme;
	}

	String getValue()
	{
		return value;
	}

	int getStart()
	{
		return start;
	}

	int getEnd()
	{
		return end;
	}

	@Override
	public String toString()
	{
		return String.format("%s: %s [%d-%d]", lexeme, value, start, end);
	}
}
