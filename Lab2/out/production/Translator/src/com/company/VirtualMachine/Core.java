package com.company.VirtualMachine;

import java.util.List;
import java.util.Stack;

public class Core
{
	private List<Integer> program;
	private Stack<Frame> frameStack;
	private Stack<Integer> stack;

	public Core(List<Integer> program)
	{
		stack = new Stack<>();

		frameStack = new Stack<>();
		frameStack.push(new Frame(1, program.get(0)));

		this.program = program;
	}

	public void run()
	{
		int destination, value, source;

		while(!frameStack.isEmpty() && getPointer() >= 0 && getPointer() < program.size())
		{
			switch(readOpcode())
			{
				case nop:
					break;

				case ret:
					frameStack.pop();
					break;
				case call:
					destination = readInt();
					frameStack.push(new Frame(readInt() + 1, program.get(destination)));
					break;
				case jmp:
					frameStack.lastElement().pointer = readInt();
					break;
				case callprint:
					System.out.printf(">> %d\n", stack.pop());
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
					stack.push(stack.pop() / stack.pop());
					break;

				default:
					break;
			}
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
