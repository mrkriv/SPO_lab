package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class AssignNode extends ValueBodyNode
{
	private final String name;

	public AssignNode(String name)
	{
		this.name = name;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(!m.variables.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Переменная '%s' не существует", name);

		super.compile(m);

		m.addOpcode(Opcode.pop);
		m.addWord(m.getVariableIndex(name));
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
