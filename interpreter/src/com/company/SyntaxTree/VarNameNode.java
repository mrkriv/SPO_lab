package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class VarNameNode extends Node
{
	private final String name;

	public VarNameNode(String name) {this.name = name;}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(!m.variables.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Переменная '%s' не существует", name);

		m.addOpcode(Opcode.pushm);
		m.addWord(m.getVariableIndex(name));
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Name: ");
		sb.append(name);
		sb.append("\n");

		return sb;
	}
}
