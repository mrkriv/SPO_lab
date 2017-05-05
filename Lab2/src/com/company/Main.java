package com.company;

import com.company.SyntaxTree.BodyNode;
import com.company.VirtualMachine.Core;

import java.util.ArrayList;
import java.util.List;

public class Main
{
	public static void main(String[] args)
	{
//		String source =
		//						"float count = 5;\n" +
		//						"if (count == 5) \n" +
		//						"{\n" +
		//						"	bool qwe = false;\n" +
		//						"}\n" +
		//						"if (count < 8)\n"+
		//						"{\n" +
		//						"	int j = i;\n"	+
		//						"	int h = 5;\n" +
		//						"	while(j > 0){\n" +
		//						"		h *= j + 2 - 57;\n" +
		//						"	}\n" +
		//						"}";
		String source =
						"int h = 7;" + "\n" +
						"int b;" + "\n" +
						"b = h;" + "\n" +
						"h = 4;" + "\n" +
						"" + "\n" +
						"" + "\n" +
						"" + "\n" +
						"" + "\n" +
						"" + "\n";

		System.out.println("Source:");
		System.out.println(source);

		Lexer lexer = new Lexer();
		Parcer parcer = new Parcer();

		List<Token> tokens = lexer.run(source);

		System.out.println("\nLexer:");
		tokens.forEach(System.out::println);

		List<Integer> opcodes = new ArrayList<>();

		try
		{
			BodyNode root = parcer.run(tokens);

			System.out.println("\nParcer:");
			System.out.println(root);

			root.compile(opcodes, new ArrayList<>());
		}
		catch(BuildExeption exp)
		{
			System.out.println("\nError:");
			System.out.println(exp.getMessage());
		}

		Core vm = new Core(opcodes);
		vm.run();
	}
}