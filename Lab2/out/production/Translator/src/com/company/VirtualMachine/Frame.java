package com.company.VirtualMachine;


public class Frame
{
	public int pointer;
	public int[] localVariable;

	public Frame(int pointer, int stackSize)
	{
		this.pointer = pointer;
		localVariable = new int[stackSize];
	}
}
