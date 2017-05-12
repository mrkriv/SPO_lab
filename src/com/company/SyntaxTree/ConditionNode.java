package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

public class ConditionNode extends Node
{
	public String operator;
	public final ValueBodyNode left = new ValueBodyNode();
	public final ValueBodyNode right = new ValueBodyNode();

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		left.compile(m);
		right.compile(m);
		m.addOpcode(Opcode.comps);
	}
}
