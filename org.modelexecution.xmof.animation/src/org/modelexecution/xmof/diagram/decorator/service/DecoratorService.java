package org.modelexecution.xmof.diagram.decorator.service;


public class DecoratorService {
	
	private static Object decoratedElement;
	private static String name;
	
	public static void decorateNode(String nodeName) {
		name=nodeName;
	}
	
	public static void decorateNode(Object node) {
		decoratedElement = node;
	}
	
	public static Object getdecoratedElement() {
		return decoratedElement;
	}
	
	public static String getName() {
		return name;
	}

	public static boolean isdecoratedElement(Object businessObject) {
		if(decoratedElement==businessObject) {
			return true;
		} else {
			return false;
		}
	}
}
