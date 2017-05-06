package com.company.SyntaxTree;

import com.company.Terminals;
import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;
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
	public void compile(List<Integer> opcodes, List<String> varTable, List<String>methodTable) throws BuildExeption
	{
		opcodes.add(Opcode.pushc.ordinal());

		if(type == Terminals.BOOLEAN)
		{
			opcodes.add(Objects.equals(value, "true") ? 1 : 0);
		} else
		{
			opcodes.add(Integer.parseInt(value));
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
