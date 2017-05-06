package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;
import java.util.Objects;

public class CallNode extends BodyNode
{
	private String name;

	public CallNode(String name) {this.name = name;}

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		if(!methodTable.contains(name))
			throw new BuildExeption("Метод '%s' не существует", name);

		super.compile(opcodes, varTable, methodTable);

		if(Objects.equals(name, "print"))
		{
			opcodes.add(Opcode.callprint.ordinal());
		}
		if(Objects.equals(name, "read"))
		{
			opcodes.add(Opcode.callread.ordinal());
		} else
		{
			opcodes.add(Opcode.pushm.ordinal());
			opcodes.add(methodTable.indexOf(name));
		}
	}
}