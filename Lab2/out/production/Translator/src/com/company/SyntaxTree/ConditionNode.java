package com.company.SyntaxTree;

import com.company.BuildExeption;

import java.util.List;

public class ConditionNode extends Node
{
	public String operator;
	public ValueBody left = new ValueBody();
	public ValueBody right = new ValueBody();

	@Override
	public void compile(List<Integer> opcodes, List<String> varTable, List<String>methodTable) throws BuildExeption
	{

	}
}
