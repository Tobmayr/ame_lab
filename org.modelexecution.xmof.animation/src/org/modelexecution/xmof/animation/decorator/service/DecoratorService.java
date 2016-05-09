package org.modelexecution.xmof.animation.decorator.service;

import org.eclipse.graphiti.tb.IDecorator;

public class DecoratorService {

	private static Object decoratedElement;
	private static IDecorator[] decorators;

	public static void setDecoratedElement(Object element,
			IDecorator[] decorators) {
		DecoratorService.decorators = decorators;
		decoratedElement = element;
	}

	public static IDecorator[] getDecorators() {
		return decorators;
	}

	public static boolean equalsDecoratedElement(Object element) {
		if (decoratedElement == null)
			return false;
		return decoratedElement.equals(element);
	}

	public static void clear() {
		decoratedElement = null;
		decorators = null;

	}

}
