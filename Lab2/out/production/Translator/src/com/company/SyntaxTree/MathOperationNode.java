package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;

public class MathOperationNode extends Node
{
	private String name;

	public MathOperationNode(String name) {
		this.name = name;
	}

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
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

		opcodes.add(opced.ordinal());
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
				return 1;

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
