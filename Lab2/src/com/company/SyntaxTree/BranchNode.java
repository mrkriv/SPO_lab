package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.VirtualMachine.Opcode;

import java.util.List;

public class BranchNode extends BodyNode
{
	public ConditionNode condition = new ConditionNode();

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		condition.compile(opcodes, varTable, methodTable);

		Opcode cond_opcode;
		switch(condition.operator)
		{
			// Invert condition
			case "==": 	cond_opcode = Opcode.jne;	break;
			case "!=":	cond_opcode = Opcode.jeq;	break;
			case "<":	cond_opcode = Opcode.jge;	break;
			case ">":	cond_opcode = Opcode.jle;	break;
			case ">=":	cond_opcode = Opcode.jls;	break;
			case "<=":	cond_opcode = Opcode.jgr;	break;

			default:
				throw new BuildExeption("Неизвестный логический оператор '%s'", condition.operator);
		}

		opcodes.add(cond_opcode.ordinal());
		opcodes.add(0);

		int start = opcodes.size();
		super.compile(opcodes, varTable, methodTable);

		opcodes.set(start - 1, opcodes.size());
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < level; i++)
			sb.append("\t");

		sb.append(getClass().getSimpleName());
		sb.append('\n');

		sb.append(condition.print(level + 1));

		for(int i = 0; i < level; i++)
			sb.append("\t");
		sb.append("Then\n");

		for(Node node : childs)
		{
			sb.append(node.print(level + 1));
		}

		return sb;
	}
}
