package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

public class MathOperationNode extends Node
{
	private final String name;

	public MathOperationNode(String name) {
		this.name = name;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		Opcode opced;

		switch(name)
		{
			case "+": opced = Opcode.addi; break;
			case "-": opced = Opcode.subi; break;
			case "*": opced = Opcode.muli; break;
			case "/": opced = Opcode.divi; break;

			default:
				throw new BuildExeption("Неизвестный математический оператор %s", name);
		}

		m.addWord(opced.ordinal());
	}

	int getPrior()
	{
		switch(name)
		{
			case "+":
			case "-":
				return 1;

			case "*":
			case "/":
				return 2;

			default:
				return 0;
		}
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" '");
		sb.append(name);
		sb.append("'");
		sb.append("\n");

		return sb;
	}
}
