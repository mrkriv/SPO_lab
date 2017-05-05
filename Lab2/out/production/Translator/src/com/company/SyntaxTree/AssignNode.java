package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;

public class AssignNode extends ValueBody
{
	String name;

	public AssignNode(String name)
	{
		this.name = name;
	}

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		if(!varTable.contains(name))
			throw new BuildExeption("Переменная '%s' не существует", name);

		super.compile(opcodes, varTable, methodTable);

		opcodes.add(Opcode.pop.ordinal());
		opcodes.add(varTable.indexOf(name));
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level;i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append(" Variable: ");
		sb.append(name);
		sb.append("\n");

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}
