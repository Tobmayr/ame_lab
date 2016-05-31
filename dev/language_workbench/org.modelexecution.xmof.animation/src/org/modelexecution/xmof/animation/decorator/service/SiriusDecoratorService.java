package org.modelexecution.xmof.animation.decorator.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;

public class SiriusDecoratorService {

	private static String currentlyActiveElement = null;
	private static Set<String> traversedElements = new HashSet<>();

	public static void setActiveElement(String elementName, String activityName) {
		String identifier = activityName + "|" + elementName;
		if (currentlyActiveElement != null) {
			traversedElements.add(currentlyActiveElement);
		}
		currentlyActiveElement = identifier;

	}

	public static boolean isTraveresedElement(EObject o) {
		if (o instanceof ActivityNode) {
			ActivityNode node = (ActivityNode) o;
			String identifier = node.getActivity().getName() + "|" + node.getName();
			return traversedElements.contains(identifier);
		}
		return false;

	}

	public static boolean isActiveElement(EObject o) {
		if (currentlyActiveElement == null)
			return false;
		if (o instanceof ActivityNode) {
			ActivityNode node = (ActivityNode) o;
			String identifier = node.getActivity().getName() + "|" + node.getName();
			return currentlyActiveElement.equals(identifier);
		}
		return false;
	}

	public static void clear() {
		traversedElements = new HashSet<>();
		currentlyActiveElement = null;
	}
}
