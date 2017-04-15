package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

enum Terminals
{
	VAR_TYPE,
	NAME,
	IF_OF,
	FOR_OF,
	WHILE_OP,
	UNAR_OP,
	CONDITION_OP,
	MATH_OP,
	DIGIT,
	DIGIT_NATURAL,
	BOOLEAN,
	ASSIGN_OP,
	BRACED_OPEN,
	BRACED_CLOSE,
	BODY_OPEN,
	BODY_CLOSE,
	LINE_END,
}

class Lexeme
{
	private static List<Lexeme> lexems;
	private Pattern pattern;
	private final int priority;
	private Terminals type;

	private static void Init()
	{
		lexems = new ArrayList<>();

		addLex(Terminals.VAR_TYPE, 		"^(int|bool|double|float|char|void|byte|short|long)$");
		addLex(Terminals.IF_OF, 		"^if$");
		addLex(Terminals.FOR_OF, 		"^for$");
		addLex(Terminals.WHILE_OP, 		"^while$");
		addLex(Terminals.DIGIT, 		"^[0-9]+$");
		addLex(Terminals.DIGIT_NATURAL, "^[0-9]?\\.[0-9]+$");
		addLex(Terminals.NAME, 			"^[a-zA-Z_]\\w*$");
		addLex(Terminals.UNAR_OP, 		"^(\\+\\+|--)$");
		addLex(Terminals.BOOLEAN, 		"^(true|false)$");
		addLex(Terminals.CONDITION_OP, 	"^(<|>|<=|>=|==)$");
		addLex(Terminals.MATH_OP, 		"^(-|\\+|\\*|\\/)$");
		addLex(Terminals.ASSIGN_OP,		"^(=|\\*=|\\+=|-=|\\/=)$");
		addLex(Terminals.BRACED_OPEN, 	"^\\($");
		addLex(Terminals.BRACED_CLOSE, 	"^\\)$");
		addLex(Terminals.BODY_OPEN, 	"^\\{$");
		addLex(Terminals.BODY_CLOSE, 	"^\\}$");
		addLex(Terminals.LINE_END, 		"^;$");
	}
	
	private static void addLex(Terminals type, String regex)
	{
		lexems.add(new Lexeme(type, regex, lexems.size()));
	}

	private Lexeme(Terminals type, String regex, int priority)
	{
		pattern = Pattern.compile(regex);
		this.type = type;
		this.priority = priority;
	}

	public static List<Lexeme> getLexems()
	{
		if(lexems == null)
			Init();

		return lexems;
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