package org.modelexecution.xmof.sirius.convert;

public class Test {

	public static void main(String[] args)throws Exception{
		DescriptionParser parser= new DescriptionParser("description/uml2.odesign", "output/xmof.odesign ");
		parser.convertToXMOFDescription();

	}
}
