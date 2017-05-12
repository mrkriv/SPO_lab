package com.company.Metadata;

import com.company.BuildExeption;
import com.company.SyntaxTree.RootBodyNode;
import com.company.VirtualMachine.Opcode;

import java.util.*;

public class Compiler
{
	private class Link
	{
		MethodInfo source;
		int source_offest;
		int offest;
		String label;

		Link(MethodInfo source, int source_offest, String label, int offest)
		{
			this.source = source;
			this.source_offest = source_offest;
			this.offest = offest;
			this.label = label;
		}
	}

	public List<VariableInfo> variables;
	public List<MethodInfo> methods;
	public MethodInfo currentMethod;
	private List<Link> links;


	public Compiler()
	{
	}

	public List<Integer> compile(RootBodyNode root) throws BuildExeption
	{
		variables = new ArrayList<>();
		links = new ArrayList<>();

		methods = new ArrayList<>();
		methods.add(new MethodInfo("void", "print", "int", "value"));
		methods.add(new MethodInfo("int", "read"));

		root.compile(this);

		List<Integer> words = new ArrayList<>();

		for(MethodInfo method : methods)
		{
			method.offest = words.size();
			words.addAll(method.words);
			method.words.clear();
		}

		for(Link link : links)
		{
			int in = link.source.offest + link.source_offest;
			int to = link.offest;

			if(link.source.labels.containsKey(link.label))
			{
				to += link.source.labels.get(link.label);
			}
			else
			{
				MethodInfo method = getMethod(link.label);
				if(method != null)
				{
					to += method.offest;
				}
				else
					throw new BuildExeption("Метка '%s' не найдена", link.label);
			}

			words.set(in, to);
		}

		return words;
	}

	public void addWord(int value)
	{
		currentMethod.words.add(value);
	}

	public void addOpcode(Opcode value)
	{
		currentMethod.words.add(value.ordinal());
	}

	public void addLink(String label, int offest)
	{
		links.add(new Link(currentMethod, getOffest() + 1, label, offest));
		currentMethod.words.add(-1);
	}

	public void addLocalLabel(String label)
	{
		currentMethod.labels.put(label, getOffest());
	}

	private int getOffest()
	{
		return currentMethod.words.size() - 1;
	}

	public String generateLocalLabelName()
	{
		return String.format("local_%d%d", currentMethod.labels.size(), new Random().nextInt() % 9999);
	}

	public MethodInfo getMethod(String name)
	{
		for(MethodInfo method : methods)
		{
			if(method.name.equals(name))
				return method;
		}
		return null;
	}

	public int getVariableIndex(String name)
	{
		int i = 0;
		for(VariableInfo var : variables)
		{
			if(var.name.equals(name))
				return i;
			i++;
		}
		return -1;
	}

	public int getMethodIndex(String name)
	{
		int i = 0;
		for(MethodInfo method : methods)
		{
			if(method.name.equals(name))
				return i;
			i++;
		}
		return -1;
	}
}