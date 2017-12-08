package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

public class ReturnNode extends ValueBodyNode
{
	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		super.compile(m);

		m.currentMethod.isHaveReturn = true;
		m.addOpcode(Opcode.ret);
	}
}
