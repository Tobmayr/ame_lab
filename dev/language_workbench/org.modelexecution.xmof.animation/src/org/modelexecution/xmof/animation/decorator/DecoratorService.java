package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.decorator.internal.EdgeId;
import org.modelexecution.xmof.animation.decorator.internal.ElementContainer;
import org.modelexecution.xmof.animation.decorator.internal.ElementState;

public class DecoratorService {

	private static Map<String, ElementContainer> activityElementContainerMap = new HashMap<>();

	public static void addDecoratedElement(Activity activity, EObject element, ElementState state) {
		if (element instanceof ActivityNode) {
			setNode(activity, (ActivityNode) element, state);
		} else if (element instanceof ActivityEdge) {
			setEdge(activity, (ActivityEdge) element, state);
		}
	}

	public static boolean isActiveNode(ActivityNode node) {
		String key = getActivityName(node);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null && container.getActiveNode() != null) {
			return container.getActiveNode().equals(node.getName());
		}

		return false;

	}

	public static boolean isTraversedNode(ActivityNode node) {
		String key = getActivityName(node);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null) {
			return container.getTraversedNodes().contains(node.getName());
		}
		return false;
	}

	public static boolean isActiveEdge(ActivityEdge edge) {
		String key = getActivityName(edge);
		ElementContainer container = activityElementContainerMap.get(key);
		if (container != null&&container.getActiveEdge()!=null) {
	
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

	public static void intializeContainer(String activityName) {
		if (!activityElementContainerMap.containsKey(activityName)) {
			activityElementContainerMap.put(activityName, new ElementContainer());
		}

	}

	public static void reset() {
		activityElementContainerMap = new HashMap<>();

	}

	public static void clear(String activityName) {
		activityElementContainerMap.put(activityName, new ElementContainer());
	}

	private static void setActiveNode(Activity activity, ActivityNode node) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.setActiveNode(node.getName());
		}
	}

	private static void addTraversedNode(Activity activity, ActivityNode node) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.addTraversedNode(node.getName());
		}
	}

	private static void setActiveEdge(Activity activity, ActivityEdge edge) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {

			container.setActiveEdge(new EdgeId(edge));
		}
	}

	private static void addTraversedEdge(Activity activity, ActivityEdge edge) {
		ElementContainer container = activityElementContainerMap.get(activity.getName());
		if (container != null) {
			container.addTraversedEdge(new EdgeId(edge));
		}
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

	private static void setNode(Activity activity, ActivityNode node, ElementState state) {
		if (state == ElementState.ACTIVE) {
			setActiveNode(activity, node);
		} else if (state == ElementState.TRAVERSED) {
			addTraversedNode(activity, node);
		}

	}

	private static void setEdge(Activity activity, ActivityEdge edge, ElementState state) {
		if (state == ElementState.ACTIVE) {
			setActiveEdge(activity, edge);
		} else if (state == ElementState.TRAVERSED) {
			addTraversedEdge(activity, edge);
		}
	}

}
