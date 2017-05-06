package com.company.VirtualMachine;

public enum Opcode
{
	nop,

	ret,
	call,
	jmp,

	jne,
	jeq,
	jls,
	jle,
	jgr,
	jge,

	callprint,
	callread,

	pushm,
	pushc,
	pop,

	comps,

	addi,
	subi,
	muli,
	divi,
}