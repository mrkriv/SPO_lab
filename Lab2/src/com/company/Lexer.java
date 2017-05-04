package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer
{
	private List<Lexeme> lexems = new ArrayList<>();

	Lexer()
	{
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

	private void addLex(Terminals type, String regex)
	{
		lexems.add(new Lexeme(type, regex, lexems.size()));
	}

	List<Token> run(String text)
	{
		text += " ";
		List<Token> tokens = new ArrayList<>();

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
				tokens.add(new Token(foundOld.get(0), text.substring(i, --offest + i), i, i + offest));
			}

			i += offest;
		}

		return tokens;
	}
}
