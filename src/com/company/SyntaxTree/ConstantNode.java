package com.company.SyntaxTree;

import com.company.Metadata.Compiler;
import com.company.Terminals;
import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class ConstantNode extends Node
{
	private final String value;
	private final Terminals type;

	public ConstantNode(String name, Terminals type) {
		this.value = name;
		this.type = type;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		m.addOpcode(Opcode.pushc);

		if(type == Terminals.BOOLEAN)
		{
			m.addWord(Objects.equals(value, "true") ? 1 : 0);
		} else
		{
			m.addWord(Integer.parseInt(value));
		}
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Value: ");
		sb.append(value);
		sb.append(" Type: ");
		sb.append(type);
		sb.append("\n");

		return sb;
	}
}
