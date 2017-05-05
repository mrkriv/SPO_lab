package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.ArrayList;
import java.util.List;

public class RootBody extends BodyNode
{
	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String> methodTable) throws BuildExeption
	{
		int varCount = varTable.size();
		int pointer = opcodes.size();

		opcodes.add(512);

		super.compile(opcodes, new ArrayList<>(), methodTable);
	}
}
