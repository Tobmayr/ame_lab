package org.modelexecution.xmof.animation.decorator.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.internal.Match;

public class SiriusDecoratorService {

	private static String currentlyActiveElement = null;
	private static Set<String> traversedElements = new HashSet<>();
	private static String callerObject = "";

	public static void setActiveElement(Match match, String activityName) {
		String identifier = activityName.trim() + "|" + match.getXmofElementName().trim();
		callerObject = match.getInvokerObjectName();
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

	public static String getCallerObject(EObject o) {
		return "Activity has been called by:\n" + callerObject;

	}

	public static boolean animatorRunning(EObject o) {
		return callerObject.isEmpty() || callerObject == null;
	}

}
