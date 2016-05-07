package org.modelexecution.xmof.animation.decorator.service;

public class DecoratorService {

	private static Object decoratedElement;
	private static boolean active = true;

	public static void setDecoratedElement(Object element, boolean active) {
		DecoratorService.active = active;
		decoratedElement = element;
	}

	public static boolean equalsDecoratedElement(Object element) {
		if (decoratedElement == null)
			return false;
		return decoratedElement.equals(element);
	}

	public static boolean isActiveElement() {
		return active;
	}

}
