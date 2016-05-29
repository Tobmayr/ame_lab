package org.modelexecution.xmof.animation.decorator.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;

public class SiriusDecorationService {

	private static String ACTIVE_ACTIVITY = null;
	private static String ACTIVE_ELEMENTNAME = null;
	private static Map<String, Set<String>> activityNodeIndex = new HashMap<String, Set<String>>();

	public boolean isActiveElement(Activity activity, ActivityNode node) {
		if (ACTIVE_ACTIVITY == null || ACTIVE_ELEMENTNAME == null)
			return false;
		return ACTIVE_ACTIVITY.equals(activity.getName())
				&& ACTIVE_ELEMENTNAME.equals(node.getName());
	}

	public static void setActiveElement(String elementName, String activityName) {
		addToTraversedElements(ACTIVE_ELEMENTNAME, ACTIVE_ACTIVITY);
		ACTIVE_ELEMENTNAME = elementName;
		ACTIVE_ACTIVITY = activityName;
	}

	private static void addToTraversedElements(String elementName,
			String activityName) {
		if (elementName != null && activityName != null) {
			if (activityNodeIndex.containsKey(activityName)) {
				activityNodeIndex.get(activityName).add(elementName);
			} else {
				Set<String> elements = new HashSet<>();
				elements.add(elementName);
				activityNodeIndex.put(activityName, elements);

			}
		}

	}

	public static boolean isTraveresedElement(Activity activity, ActivityNode node) {
		Set<String> elements = activityNodeIndex.get(activity);
		if (elements != null) {
			return elements.contains(node);
		}
		return false;
	}

	public static void clear() {
		activityNodeIndex = new HashMap<String, Set<String>>();
		ACTIVE_ELEMENTNAME = null;
		ACTIVE_ACTIVITY = null;
	}
}
