package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class CallNode extends BodyNode
{
	private final String name;

	public CallNode(String name) {this.name = name;}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		//if(!m.methods.stream().anyMatch(i -> Objects.equals(i.name, name)))
		//	throw new BuildExeption("Метод '%s' не существует", name);

		super.compile(m);

		if(Objects.equals(name, "print"))
		{
			m.addOpcode(Opcode.callprint);
		}
		else if(Objects.equals(name, "read"))
		{
			m.addOpcode(Opcode.callread);
		}
		else
		{
			m.addOpcode(Opcode.callc);
			m.addLink(name, 0);
		}
	}
}