package com.company.VirtualMachine;

import java.io.IOException;
import java.util.*;

public class Core
{
	private List<Integer> program;
	private Stack<Frame> frameStack;
	private Stack<Integer> stack;

	public Core()
	{
	}

	public String decompile(List<Integer> program)
	{
		Map<Opcode, Integer> sizeMap = new HashMap<>();

		//		frameStack = new Stack<>();
		//		frameStack.push(new Frame(0, 32));
		//
		//		stack = new Stack<>();
		//		for(int i = 0; i < Opcode.values().length * 3; i++)
		//			stack.push(1);
		//
		//		for(Opcode opcod : Opcode.values())
		//		{
		//			if(opcod == Opcode.callread || opcod == Opcode.callprint)
		//			{
		//				sizeMap.put(opcod, 1);
		//				continue;
		//			}
		//
		//			this.program = new ArrayList<>();
		//			this.program.add(opcod.ordinal());
		//			this.program.add(0);
		//			this.program.add(0);
		//			this.program.add(0);
		//			this.program.add(0);
		//
		//			frameStack.push(new Frame(0, 32));
		//			step();
		//
		//			sizeMap.put(opcod, getPointer());
		//		}

		sizeMap.put(Opcode.nop, 1);
		sizeMap.put(Opcode.ret, 1);
		sizeMap.put(Opcode.call, 1);
		sizeMap.put(Opcode.jmp, 2);

		sizeMap.put(Opcode.callprint, 1);
		sizeMap.put(Opcode.callread, 1);

		sizeMap.put(Opcode.pushm, 2);
		sizeMap.put(Opcode.pushc, 2);
		sizeMap.put(Opcode.pop, 2);

		sizeMap.put(Opcode.addi, 1);
		sizeMap.put(Opcode.subi, 1);
		sizeMap.put(Opcode.muli, 1);
		sizeMap.put(Opcode.divi, 1);

		StringBuilder sb = new StringBuilder();
		int pointer = 1;
		while(pointer < program.size())
		{
			Opcode opcod = Opcode.values()[program.get(pointer++)];
			sb.append(opcod.toString() + "\t");

			for(int i = 1; i < sizeMap.get(opcod) && pointer < program.size(); i++)
				sb.append("\t" + program.get(pointer++).toString());

			sb.append("\n");
		}

		return sb.toString();
	}

	public void run(List<Integer> program)
	{
		stack = new Stack<>();

		frameStack = new Stack<>();
		frameStack.push(new Frame(1, program.get(0)));

		this.program = program;

		while(!frameStack.isEmpty() && getPointer() >= 0 && getPointer() < program.size())
			step();
	}

	private void step()
	{
		int destination, value, source;
		switch(readOpcode())
		{
			case nop:
				break;

			case ret:
				frameStack.pop();
				break;
			case call:
				destination = stack.pop();
				frameStack.push(new Frame(destination + 1, program.get(destination)));
				break;
			case jmp:
				frameStack.lastElement().pointer = readInt();
				break;

			case callprint:
				System.out.printf(">> %d\n", stack.pop());
				break;
			case callread:
				stack.push(new Scanner(System.in).nextInt());
				break;

			case pushc:
				value = readInt();
				stack.push(value);
				break;
			case pushm:
				source = readInt();
				stack.push(getFrame().localVariable[source]);
				break;
			case pop:
				destination = readInt();
				getFrame().localVariable[destination] = stack.pop();

				break;

			case addi:
				stack.push(stack.pop() + stack.pop());
				break;
			case subi:
				stack.push(-stack.pop() + stack.pop());
				break;
			case muli:
				stack.push(stack.pop() * stack.pop());
				break;
			case divi:
				value = stack.pop();
				stack.push(stack.pop() / value);
				break;

			default:
				break;
		}
	}

	private int readInt()
	{
		getFrame().pointer++;
		return program.get(getPointer() - 1);
	}

	private Opcode readOpcode()
	{
		getFrame().pointer++;
		return Opcode.values()[program.get(getPointer() - 1)];
	}

	private Frame getFrame()
	{
		return frameStack.lastElement();
	}

	private int getPointer()
	{
		return getFrame().pointer;
	}
}
