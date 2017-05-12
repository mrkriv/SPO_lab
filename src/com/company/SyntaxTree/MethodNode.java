package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.Metadata.MethodInfo;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class MethodNode extends BodyNode
{
	public final String type;
	public final String name;

	public MethodNode(String type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(m.currentMethod != null)
			throw new BuildExeption("Невозможно обьявить метод в методе");

		if(m.methods.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Метод '%s' уже обьявленн", name);

		m.currentMethod = new MethodInfo(type, name);
		m.methods.add(m.currentMethod);

		m.addWord(128);

		super.compile(m);

		m.addOpcode(Opcode.ret);

		m.currentMethod = null;
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level;i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append(" Name: ");
		sb.append(name);
		sb.append(" Type: ");
		sb.append(type);
		sb.append("\n");

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}
