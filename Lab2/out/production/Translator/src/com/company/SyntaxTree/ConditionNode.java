package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;

public class ConditionNode extends Node
{
	public String operator;
	public final ValueBody left = new ValueBody();
	public final ValueBody right = new ValueBody();

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		left.compile(opcodes, varTable, methodTable);
		right.compile(opcodes, varTable, methodTable);
		opcodes.add(Opcode.comps.ordinal());
	}
}
