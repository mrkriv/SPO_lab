package com.company.VirtualMachine;

public enum Opcode
{
	nop,

	ret,
	callc,
	calls,
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

	inc,
	dec,
}