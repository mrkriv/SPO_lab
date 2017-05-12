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

		root.preCompile(this);
		root.compile(this);

		List<Integer> words = new ArrayList<>();

		int optimizeCounter = 0;

		for(MethodInfo method : methods)
		{
			optimizeCounter += method.words.size();
			optimizeMethod(method);
			optimizeCounter -= method.words.size();

			method.offest = words.size();
			words.addAll(method.words);
		}

		System.out.printf("Optimize: %d words (%d %s)\n", optimizeCounter, optimizeCounter * 100 / words.size(), '%');

		for(Link link : links)
		{
			int in = link.source.offest + link.source_offest;
			int to = link.offest;

			if(link.source.labels.containsKey(link.label))
			{
				to += link.source.offest + link.source.labels.get(link.label);
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

	private void removeOpcode(MethodInfo method, int start, int count)
	{
		for(int n = 0; n < count; n++)
			method.words.remove(start);

		for(Map.Entry<String, Integer> label : method.labels.entrySet())
		{
			if(label.getValue() >= start + count)
				label.setValue(label.getValue() - count);
		}

		for(Link link : links)
		{
			if(link.source != method)
				continue;

			if(link.source_offest >= start + count)
				link.source_offest -= count;

			if(link.offest >= start + count)
				link.offest -= count;
		}
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

	public VariableInfo getVariable(String name)
	{
		for(VariableInfo var : variables)
		{
			if(var.name.equals(name))
				return var;
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

	private void optimizeMethod(MethodInfo method)
	{
		for(int i = 1; i < method.words.size(); )
		{
			Opcode opcode = Opcode.values()[method.words.get(i)];
			int next = i + Opcode.getSize(opcode);

			if(next >= method.words.size())
				break;

			Opcode next_opcode = Opcode.values()[method.words.get(i + Opcode.getSize(opcode))];

			// Replace Push-Pop or Pop-Push to Copys (Copy from stack)
			if(	(opcode == Opcode.pushm && next_opcode == Opcode.pop ||
					opcode == Opcode.pop && next_opcode == Opcode.pushm ) &&
					Objects.equals(method.words.get(i + 1), method.words.get(next + 1)))
			{
				int size = Opcode.getSize(opcode) + Opcode.getSize(next_opcode) - Opcode.getSize(Opcode.copys);
				method.words.set(i, Opcode.copys.ordinal());

				removeOpcode(method, i + Opcode.getSize(Opcode.copys), size);
				continue;
			}

			if((opcode == Opcode.pushm && next_opcode == Opcode.pushm))
			{
				int size = Opcode.getSize(opcode) + Opcode.getSize(next_opcode) - Opcode.getSize(Opcode.pushm2);
				method.words.set(i, Opcode.pushm2.ordinal());
				method.words.set(i+2, method.words.get(next + 1));

				removeOpcode(method, i + Opcode.getSize(Opcode.pushm2), size);
				continue;
			}

			if((opcode == Opcode.pushc && next_opcode == Opcode.pushc))
			{
				int size = Opcode.getSize(opcode) + Opcode.getSize(next_opcode) - Opcode.getSize(Opcode.pushc2);
				method.words.set(i, Opcode.pushc2.ordinal());
				method.words.set(i+2, method.words.get(next + 1));

				removeOpcode(method, i + Opcode.getSize(Opcode.pushc2), size);
				continue;
			}

			i = next;
		}
	}
}