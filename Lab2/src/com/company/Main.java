package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Main
{
	public static void main(String[] args)
	{
//		String source = "bool g= 4 ; \n" +
//				"int h = g;\n" +
//				"h += 5+ g-7;";
		String source =
						"float count = 5;" +
						"if (count == 5) " +
						"{" +
						"	bool qwe = false;" +
						"}" +
						"if (count < 8)\n"+
						"{\n" +
						"	int j = i;\n"	+
						"	int h = 5;" +
						"	while(j > 0){\n" +
						"		h *= j + 2 - 57;\n" +
						"	}" +
						"}";


		System.out.println("Source:");
		System.out.println(source);

		System.out.println("\nLexer:");
		List<Token> tokens = lexer(source);

		tokens.forEach(System.out::println);

		Parcer parcer = new Parcer(tokens);

		try
		{
			System.out.println("\nParcer:");
			parcer.run();
			parcer.DebugLog.forEach(System.out::print);
		}
		catch(ParceExeption parceExeption)
		{
			parcer.DebugLog.forEach(System.out::print);
			System.out.println("Parcer error: " + parceExeption.getMessage());
		}
	}

	private static List<Token> lexer(String text)
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

				found = Lexeme.getLexems().stream().filter(lex -> lex.isMatch(buff)).collect(Collectors.toList());
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