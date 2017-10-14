package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.Metadata.VariableInfo;

import java.util.Objects;

public class VarDefineNode extends Node
{
	public final String type;
	public final String name;

	public VarDefineNode(String type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(m.variables.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Переменная '%s' уже обьявленна", name);

		m.variables.add(new VariableInfo(type, name));
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Name: ");
		sb.append(name);
		sb.append(" Type: ");
		sb.append(type);
		sb.append("\n");

		return sb;
	}
}
