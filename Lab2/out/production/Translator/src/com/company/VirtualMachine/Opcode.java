package com.company.VirtualMachine;

public enum Opcode
{
	nop,

	ret,
	call,
	jmp,

	callprint,

	pushm,
	pushc,
	pop,

	addi,
	subi,
	muli,
	divi,
}