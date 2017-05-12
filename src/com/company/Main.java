package com.company;

import com.company.Metadata.Compiler;
import com.company.SyntaxTree.BodyNode;
import com.company.SyntaxTree.RootBodyNode;
import com.company.VirtualMachine.Core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main
{
	public static void main(String[] args)
	{
		String source;
		try
		{
			source = String.join("\n" ,Files.readAllLines(Paths.get("src\\Test.script"), StandardCharsets.UTF_8));
		}
		catch(IOException e)
		{
			System.out.println("Test.script not found");
			return;
		}

		System.out.println("Source:");
		System.out.println(source);

		Lexer lexer = new Lexer();
		Parcer parcer = new Parcer();
		Core vm = new Core();

		List<Token> tokens = lexer.run(source);

		System.out.println("\nLexer:");
		tokens.forEach(System.out::println);

		try
		{
			RootBodyNode root = parcer.run(tokens);

			System.out.println("\nParcer:");
			System.out.println(root);

			Compiler compiler = new Compiler();
			List<Integer> program = compiler.compile(root);

			System.out.println("\nDecompile:");
			System.out.println(vm.decompile(program));

			System.out.println("\nRun...");
			vm.run(program);

			System.out.println("\nFinished");
		}
		catch(BuildExeption exp)
		{
			System.out.println("\nError:");
			System.out.println(exp.getMessage());
		}
	}
}