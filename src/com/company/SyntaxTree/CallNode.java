package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.Metadata.MethodInfo;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class CallNode extends BodyNode
{
	private final String name;

	public CallNode(String name) {this.name = name;}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		MethodInfo method = m.getMethod(name);

		if(method == null)
			throw new BuildExeption("Метод '%s' не существует", name);

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

			if(method.arguments.size() != childs.size())
				throw new BuildExeption("Метод '%s' имеет %d параметров", name, method.arguments.size());

			super.compile(m);

//			for(int i = 0; i <  method.arguments.size(); i++)
//			{
//				childs.get(i).compile(m);
//			}
		}
	}
}