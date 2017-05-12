package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.Metadata.MethodInfo;
import com.company.Metadata.VariableInfo;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class MethodNode extends BodyNode
{
	public final String type;
	public final String name;
	public final BodyNode VariablesBody = new BodyNode();

	public MethodNode(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public void preCompile(Compiler m) throws BuildExeption
	{
		if(m.methods.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Метод '%s' уже обьявленн", name);

		MethodInfo method = new MethodInfo(type, name);

		for(Node node : VariablesBody.childs)
		{
			if(!(node instanceof VarDefineNode))
				throw new BuildExeption("Этого тут быть не должно");

			VarDefineNode var = (VarDefineNode)node;
			method.arguments.add(new VariableInfo(var.type, var.name));
		}

		m.methods.add(method);
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(m.currentMethod != null)
			throw new BuildExeption("Невозможно обьявить метод в методе");

		m.currentMethod = m.getMethod(name);
		m.addWord(128);

		for(VariableInfo var : m.currentMethod.arguments)
		{
			m.variables.add(var);
			m.addOpcode(Opcode.pop);
			m.addWord(m.getVariableIndex(var.name));
		}

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
