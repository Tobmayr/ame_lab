package org.modelexecution.xmof.animation.decorator.service;

import java.util.HashMap;
import java.util.Map;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.util.EdgeId;

public class SiriusDecoratorService {

	private static Map<String, ElementContainer> activityElementContainerMap = new HashMap<>();

	public static void setActiveNode(Activity activity, ActivityNode node) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.setActiveNode(node.getName());
		}
	}

	public static void addTraversedNode(Activity activity, ActivityNode node) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.addTraversedNode(node.getName());
		}
	}

	public static void setActiveEdge(Activity activity, ActivityEdge edge) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {

			container.setActiveEdge(new EdgeId(edge));
		}
	}

	public static void addTraversedEdge(Activity activity, ActivityEdge edge) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.addTraversedEdge(new EdgeId(edge));
		}
	}

	public static boolean isTraveresedElement(ActivityNode node) {
		String key = getActivityName(node);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null) {
			return container.getTraversedNodes().contains(node.getName());
		}
		return false;
	}

	public static boolean isActiveElement(ActivityNode node) {
		String key = getActivityName(node);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null) {
			return container.getActiveNode().equals(node.getName());
		}

		return false;

	}

	public static boolean isActiveEdge(ActivityEdge edge) {
		String key = getActivityName(edge);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null) {

			return container.getActiveEdge().equals(new EdgeId(edge));
		}
		return false;
	}

	public static boolean isTraversedEdge(ActivityEdge edge) {
		String key = getActivityName(edge);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null) {

			return container.getTraversedEdges().contains(new EdgeId(edge));
		}
		return false;
	}

	private static String getActivityName(ActivityNode node) {
		if (node.getActivity() != null) {
			return node.getActivity().getName();
		} else if (node.getInStructuredNode() != null) {
			if (node.getInStructuredNode().getActivity() != null) {
				return node.getInStructuredNode().getActivity().getName();
			}
		}
		return "";
	}

	private static String getActivityName(ActivityEdge edge) {
		if (edge.getActivity() != null) {
			return edge.getActivity().getName();
		} else if (edge.getInStructuredNode() != null) {
			if (edge.getInStructuredNode().getActivity() != null) {
				return edge.getInStructuredNode().getActivity().getName();
			}
		}
		return "";
	}

	public static void clear(String activityName) {
		activityElementContainerMap.put(activityName, new ElementContainer());
	}

	public static void intializeContainer(String activityName) {
		if (!activityElementContainerMap.containsKey(activityName)) {
			activityElementContainerMap.put(activityName, new ElementContainer());
		}

	}

	public static void reset() {
		activityElementContainerMap = new HashMap<>();

	}

}
