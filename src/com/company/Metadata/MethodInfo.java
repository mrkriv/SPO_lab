package com.company.Metadata;

import java.util.*;

public class MethodInfo
{
	public final String name;
	public final String returnType;
	public final List<VariableInfo> arguments;
	public final Map<String, Integer> labels;
	public final List<Integer> words;
	public int offest;
	public boolean isHaveReturn;

	public MethodInfo(String returnType, String name)
	{
		this.name = name;
		this.returnType = returnType;
		this.arguments = new ArrayList<>();
		this.labels = new HashMap<>();
		this.words = new ArrayList<>();
	}

	public MethodInfo(String returnType, String name, String arg1_type, String arg1_name)
	{
		this.name = name;
		this.returnType = returnType;
		this.arguments = new ArrayList<>();

		arguments.add(new VariableInfo(arg1_type, arg1_name));
		this.labels = new HashMap<>();
		this.words = new ArrayList<>();
	}
}