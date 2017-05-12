package com.company;

import com.company.Metadata.Compiler;
import com.company.SyntaxTree.RootBodyNode;
import com.company.VirtualMachine.Core;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Tests
{
	private static String Run(String source)
	{
		try
		{
			Lexer lexer = new Lexer();
			Parcer parcer = new Parcer();
			Core vm = new Core();

			List<Token> tokens = lexer.run(source);
			RootBodyNode root = parcer.run(tokens);

			Compiler compiler = new Compiler();

			vm.enableOut = false;
			return vm.run(compiler.compile(root));
		}
		catch(BuildExeption e)
		{
			return e.getMessage();
		}
	}

	@Test
	public void TestVariableBase()
	{
		String source = "void main(){" +
				"int a = 125345;" +
				"print(a);" +
				"}";

		Assert.assertEquals(Run(source), "125345\n");
	}

	@Test
	public void TestVariableSwap()
	{
		String source = "void main(){" +
				"int a = 125345;" +
				"int b = 4567;" +
				"int c = b;" +
				"b = a;" +
				"a = c;" +
				"print(a);" +
				"}";

		Assert.assertEquals(Run(source), "4567\n");
	}

	@Test
	public void TestMath()
	{
		String source = "void main(){" +
				"print((1+10+45*7-6+9*((9-7)+4*(5-4)))/(10-8));" +
				"}";

		Assert.assertEquals(Run(source), "187\n");
	}

	@Test
	public void TestBranch()
	{
		String source = "void main(){" +
				"int a = 12345;" +
				"if(a > 5 * 10){print(1);}else{print(0);}" +
				"}";

		Assert.assertEquals(Run(source), "1\n");

		source = "void main(){" +
				"int a = 6;" +
				"if(a > 5 * 10){print(1);}else{print(0);}" +
				"}";

		Assert.assertEquals(Run(source), "0\n");
	}

	@Test
	public void TestMethodRecursion()
	{
		String source = "void main(){" +
				"print(pow_rec(2,10));" +
				"}" +
				"int pow_rec(int x, int s){" +
				"if(s == 1){" +
				"return x;" +
				"}" +
				"return x * pow_rec(x, s - 1);" +
				"}";

		Assert.assertEquals(Run(source), "1024\n");
	}

	@Test
	public void TestWhile()
	{
		String source = "void main(){" +
				"int x = 2;" +
				"int s = 10;" +
				"int r = x;" +
				"while(s > 1)" +
				"{" +
				"	r *= x;" +
				"	s--;"+
				"}" +
				"print(r);" +
				"}";

		Assert.assertEquals(Run(source), "1024\n");
	}
}