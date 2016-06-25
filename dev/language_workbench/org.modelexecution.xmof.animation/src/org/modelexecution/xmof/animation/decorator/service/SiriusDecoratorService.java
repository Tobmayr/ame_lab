package org.modelexecution.xmof.animation.decorator.service;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Pin;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.ExpansionNode;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.InitialNode;
import org.modelexecution.xmof.animation.controller.internal.Match;

public class SiriusDecoratorService {

	private static String currentlyActiveElement = "";
	private static String previouslyActiveElement = "";
	private static Set<String> traversedElements = new HashSet<>();
	private static Set<ActivityEdge> traversedEdges = new HashSet<>();
	private static ActivityEdge currentlyActiveEdge = null;
	private static String callerObject = "";

	public static void setActiveElement(Match match, String activityName) {
		String identifier = activityName.trim() + "|" + match.getXmofElementName().trim();
		callerObject = match.getInvokerObjectName();
		if (currentlyActiveElement != null) {
			previouslyActiveElement = currentlyActiveElement;
			traversedElements.add(currentlyActiveElement);
		}
		if (currentlyActiveEdge != null) {
			traversedEdges.add(currentlyActiveEdge);
		}
		currentlyActiveElement = identifier;
		traversedElements.remove(currentlyActiveElement);

	}

	public static boolean isTraveresedElement(ActivityNode node) {
		if (node == null)
			return false;
		String identifier = generateIdentifier(node);
		return traversedElements.contains(identifier);
	}

	public static boolean isActiveElement(ActivityNode node) {
		if (node == null || node.getName()==null)
			return false;
		
		String identifier = generateIdentifier(node);
		return currentlyActiveElement.equals(identifier);
			

	}


	private static String generateIdentifier(ActivityNode node) {
		String activityName = "";
		if (node.getActivity() != null) {
			activityName = node.getActivity().getName();
		} else if (node.getInStructuredNode() != null && node.getInStructuredNode().getActivity() != null) {
			activityName = node.getInStructuredNode().getActivity().getName();
		}
		return activityName + "|" + node.getName();

	}

	public static boolean isActiveEdge(ActivityEdge edge) {
		if (currentlyActiveElement.isEmpty() || previouslyActiveElement.isEmpty())return false;
		if (edge.getSource() == null || edge.getTarget() == null)
			return false;
		
		if(isSimpleActiveEdge(edge))return true;
		ActivityNode sourceNode = retrieveSourceNode(edge);
		ActivityNode targetNode = retrieveTargeNode(edge);

		if (isPreviouslyActiveElement(sourceNode) && isActiveElement(targetNode)) {
			currentlyActiveEdge = edge;
			traversedEdges.remove(currentlyActiveEdge);
			return true;
		}
		return false;
	}

	private static boolean isSimpleActiveEdge(ActivityEdge edge) {
		return isPreviouslyActiveElement(edge.getSource()) && isActiveElement(edge.getTarget());

	}

	private static ActivityNode retrieveTargeNode(ActivityEdge edge) {
		if (edge.getTarget() instanceof Pin) {
			return retrieveConnectedActivityNode((Pin) edge.getTarget());
		}else if (edge.getTarget() instanceof ExpansionNode){
			return retrieveConnectedActivityNode((ExpansionNode) edge.getTarget());
		}
		return edge.getTarget();
	}

	private static ActivityNode retrieveConnectedActivityNode(ExpansionNode expansionNode) {
		if (expansionNode.getRegionAsInput()!=null){
			return expansionNode.getRegionAsInput();
		}else 
			return expansionNode.getRegionAsOutput();
	}

	private static ActivityNode retrieveSourceNode(ActivityEdge edge) {
		if (edge.getSource() instanceof Pin) {
			return retrieveConnectedActivityNode((Pin) edge.getSource());
		}else if (edge.getSource() instanceof ExpansionNode){
			return retrieveConnectedActivityNode((ExpansionNode) edge.getSource());
		}
		return edge.getSource();
	}

	private static ActivityNode retrieveConnectedActivityNode(Pin pin) {
		if (pin.eContainer() instanceof ActivityNode) {
			return (ActivityNode) pin.eContainer();
		}

		return null;

	}

	public static boolean isTraversedEdge(ActivityEdge edge) {
		return traversedEdges.contains(edge);
	}

	public static boolean isPreviouslyActiveElement(ActivityNode node) {
		if (node == null || node.getName()==null)
			return false;
		String identifier = generateIdentifier(node);
		if (previouslyActiveElement.equals(identifier))
			return true;
		if (node instanceof InitialNode)
			return true;
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
