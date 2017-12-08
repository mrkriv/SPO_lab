package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;
import com.company.VirtualMachine.Opcode;

import java.util.Objects;

public class VarUnarNode extends Node
{
	public final String operator;
	public final String name;

	public VarUnarNode(String name, String operator)
	{
		this.operator = operator;
		this.name = name;
	}

	@Override
	public void compile(Compiler m) throws BuildExeption
	{
		if(!m.variables.stream().anyMatch(v -> Objects.equals(v.name, name)))
			throw new BuildExeption("Переменная '%s' не существует", name);

		Opcode cond_opcode;
		switch(operator)
		{
			case "++": 	cond_opcode = Opcode.inc;	break;
			case "--":	cond_opcode = Opcode.dec;	break;

			default:
				throw new BuildExeption("Неизвестный унарный оператор '%s'", operator);
		}

		m.addOpcode(cond_opcode);
		m.addWord(m.getVariableIndex(name));
	}

	@Override
	public StringBuilder print(int level)
	{
		StringBuilder sb = super.print(level);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" Name: ");
		sb.append(name);
		sb.append(" Operator: ");
		sb.append(operator);
		sb.append("\n");

		return sb;
	}
}
