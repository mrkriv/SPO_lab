package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;

public class VarNameNode extends Node
{
	private final String name;

	public VarNameNode(String name) {this.name = name;}

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String>methodTable) throws BuildExeption
	{
		if(!varTable.contains(name))
			throw new BuildExeption("Переменная '%s' не существует", name);

		opcodes.add(Opcode.pushm.ordinal());
		opcodes.add(varTable.indexOf(name));
	}
}
