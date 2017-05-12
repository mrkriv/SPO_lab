package com.company.SyntaxTree;

import com.company.BuildExeption;
import com.company.Metadata.Compiler;

public class RootBodyNode extends BodyNode
{
	public void preCompile(Compiler m) throws BuildExeption
	{
		for(Node node : childs)
		{
			if(node instanceof MethodNode)
			{
				((MethodNode)node).preCompile(m);
			}
		}
	}
}
