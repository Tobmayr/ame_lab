package org.modelexecution.xmof.animation.decorator.service;

import java.util.LinkedHashSet;
import java.util.Set;

public class DecoratorService {

	private static Set<Object> decoratedElements = new LinkedHashSet<Object>();

	public static void addDecoratedElement(Object element) {
		decoratedElements.add(element);
	}

	public static void removeDecoratedElement(Object element) {
		decoratedElements.remove(element);
	}

	public static boolean isDecoratedElement(Object element) {
		return decoratedElements.contains(element);
	}

	public static void clearDecoratedElements() {
		decoratedElements.clear();
	}

}
