package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;


public class WhileNode extends BodyNode
{
	public ConditionNode condition = new ConditionNode();

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		String l_start = m.generateLocalLabelName();
		m.addLocalLabel(l_start);

		condition.compile(m);

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

		m.addWord(cond_opcode.ordinal());

		String l_end = m.generateLocalLabelName();
		m.addLink(l_end, 1);

		super.compile(m);

		m.addOpcode(Opcode.jmp);
		m.addLink(l_start, 1);

		m.addLocalLabel(l_end);
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