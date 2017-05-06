package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Lexer
{
	private final List<Lexeme> lexems = new ArrayList<>();

	Lexer()
	{
		addLex(Terminals.COMMENT_LINE,	"^//$");
		addLex(Terminals.COMMENT_START, "^/\\*$");
		addLex(Terminals.COMMENT_END, 	"^\\*/$");

		addLex(Terminals.VAR_TYPE, 		"^(int|bool|double|float|char|void|byte|short|long)$");
		addLex(Terminals.IF_OF, 		"^if$");
		addLex(Terminals.FOR_OF, 		"^for$");
		addLex(Terminals.WHILE_OP, 		"^while$");

		addLex(Terminals.BRACED_OPEN, 	"^\\($");
		addLex(Terminals.BRACED_CLOSE, 	"^\\)$");
		addLex(Terminals.BODY_OPEN, 	"^\\{$");
		addLex(Terminals.BODY_CLOSE, 	"^\\}$");
		addLex(Terminals.COMMA, 		"^,$");
		addLex(Terminals.LINE_END, 		"^;$");

		addLex(Terminals.UNAR_OP, 		"^(\\+\\+|--)$");
		addLex(Terminals.CONDITION_OP, 	"^(<|>|<=|>=|==)$");
		addLex(Terminals.MATH_OP, 		"^(-|\\+|\\*|\\/)$");
		addLex(Terminals.ASSIGN_OP,		"^(=|\\*=|\\+=|-=|\\/=)$");

		addLex(Terminals.DIGIT, 		"^[0-9]+$");
		addLex(Terminals.DIGIT_NATURAL, "^[0-9]?\\.[0-9]+$");
		addLex(Terminals.BOOLEAN, 		"^(true|false)$");
		addLex(Terminals.NAME, 			"^[a-zA-Z_]\\w*$");
	}

	private void addLex(Terminals type, String regex)
	{
		lexems.add(new Lexeme(type, regex, lexems.size()));
	}

	List<Token> run(String text)
	{
		text += " ";
		List<Token> tokens = new ArrayList<>();
		boolean ignore = false;

		for(int i = 0; i < text.length(); )
		{
			List<Lexeme> foundOld = new ArrayList<>();
			List<Lexeme> found = new ArrayList<>();

			int offest;
			for(offest = 0; offest + 1 < text.length() && (!found.isEmpty() || offest == 0); offest++)
			{
				foundOld = found;
				String buff = text.substring(i, i + offest + 1);

				found = lexems.stream().filter(lex -> lex.isMatch(buff)).collect(Collectors.toList());
			}

			if(found.isEmpty() && !foundOld.isEmpty())
			{
				foundOld.sort((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));
				Lexeme lexeme = foundOld.get(0);

				offest--;

				if(lexeme.getType() == Terminals.COMMENT_START)
				{
					ignore = true;
				}
				else if(lexeme.getType() == Terminals.COMMENT_END)
				{
					ignore = false;
				}
				else if(!ignore)
				{
					if(lexeme.getType() == Terminals.COMMENT_LINE)
					{
						i = text.indexOf('\n', i) + 1 - ++offest;
					}
					else
						tokens.add(new Token(lexeme, text.substring(i, offest + i), i, i + offest));
				}
			}

			i += offest;
		}

		return tokens;
	}
}
