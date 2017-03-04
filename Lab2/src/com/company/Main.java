package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{
	private static List<Lexeme> Lexems;

	public static void main(String[] args)
	{
		Init();
		List<Token> tokens = Parse(
			"for (int i = 0; i < 5; i++)" +
			"{" +
				"int j = i;" +
				"int h = 5;" +
				"while(j-- > 0)" +
					"h++;" +
			"}");

		tokens.forEach(System.out::println);
	}

	private static void Init()
	{
		Lexems = new ArrayList<>();
		Lexems.add(new Lexeme("OPERATOR", "^(for|while|if)$", 0));
		Lexems.add(new Lexeme("VAR_TYPE", "^(int|double|float|char|void|byte|short|long)$", 1));
		Lexems.add(new Lexeme("NAME", "^[a-zA-Z_]\\w*$", 2));
		Lexems.add(new Lexeme("OPERATION", "^(-|\\+|>|<|\\*|\\/)$", 2));
}

	private static List<Token> Parse(String text)
	{
		List<Token> tokens = new ArrayList<>();

		for(int i = 0; i < text.length(); )
		{
			List<Lexeme> foundOld = new ArrayList<>();
			List<Lexeme> found = new ArrayList<>();

			int offest;
			for(offest = 0; offest < text.length() && (!found.isEmpty() || offest == 0); offest++)
			{
				foundOld = found;
				String buff = text.substring(i, i + offest + 1);
				//System.out.println(buff);

				found = Lexems.stream().filter(lex -> lex.isMatch(buff)).collect(Collectors.toList());
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