package com.company.VirtualMachine;


class Frame
{
	public int pointer;
	public final int[] localVariable;

	public Frame(int pointer, int stackSize)
	{
		this.pointer = pointer;
		localVariable = new int[stackSize];
	}
}
