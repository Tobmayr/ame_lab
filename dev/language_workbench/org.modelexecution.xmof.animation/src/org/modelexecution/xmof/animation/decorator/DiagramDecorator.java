package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Action;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.InputPin;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.OutputPin;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Pin;
import org.modelexecution.xmof.Syntax.Activities.CompleteStructuredActivities.StructuredActivityNode;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.ExpansionNode;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.ExpansionRegion;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ControlFlow;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ObjectFlow;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ObjectNode;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.util.ActivityEdgeId;

public abstract class DiagramDecorator {
	private boolean activityFinished = false;
	protected Activity activity;
	protected Map<String, ActivityNode> activityNodeMap;
	protected Map<ActivityEdgeId, ActivityEdge> activityEdgeMap;
	protected ActivityNode activeNode;
	protected ActivityNode previouslyActiveNode;
	protected StructuredActivityNode inStructuredNode = null;
	protected ActivityEdge activeEdge;

	public DiagramDecorator(Activity activity) {

		this.activity = activity;
	}

	public void initializeMaps() {
		activityNodeMap = new HashMap<>();
		activityEdgeMap = new HashMap<>();
		for (ActivityNode node : activity.getNode()) {
			processActivityNode(node);
		}
		for (ActivityEdge edge : activity.getEdge()) {
			processActivityEdge(edge);
		}

	}

	private void processActivityEdge(ActivityEdge edge) {
		ActivityEdgeId id = null;
		ActivityNode source = null, target = null;
		if (edge instanceof ControlFlow) {
			source = edge.getSource();
			target = edge.getTarget();
		} else if (edge instanceof ObjectFlow) {
			source = retrieveConnectedNode(edge.getSource());
			target = retrieveConnectedNode(edge.getTarget());
		}
		if (source != null && target != null) {
			id = new ActivityEdgeId(source.getName(), target.getName());
			activityEdgeMap.put(id, edge);
		}

	}

	private ActivityNode retrieveConnectedNode(ActivityNode node) {
		if (node instanceof ObjectNode) {
			if (node instanceof Pin) {
				return retrieveConnectedNode((Pin) node);
			} else if (node instanceof ExpansionNode) {
				return retrieveConnectedNode((ExpansionNode) node);
			}
		}
		return node;

	}

	private ActivityNode retrieveConnectedNode(ExpansionNode expansionNode) {
		if (expansionNode.getRegionAsInput() != null) {
			return expansionNode.getRegionAsInput();
		} else
			return expansionNode.getRegionAsOutput();
	}

	private ActivityNode retrieveConnectedNode(Pin pin) {
		if (pin.eContainer() instanceof ActivityNode) {
			return (ActivityNode) pin.eContainer();
		}

		return null;
	}

	private void processActivityNode(ActivityNode node) {
		if (node.getName() != null) {
			if (node instanceof StructuredActivityNode) {

				getActivityNodes((StructuredActivityNode) node);
				getActivityEdges((StructuredActivityNode) node);
			}
			activityNodeMap.put(node.getName(), node);
		}
	}

	private void getActivityEdges(StructuredActivityNode node) {
		for (ActivityEdge edge : node.getEdge()) {
			processActivityEdge(edge);
		}

	}

	private void getActivityNodes(StructuredActivityNode expNode) {
		for (ActivityNode actNode : expNode.getNode()) {
			processActivityNode(actNode);
		}

	}

	public boolean decorateActivityElement(Match match) {
		if (activityNodeMap == null) {
			initializeMaps();
		}
		if (isActivityFinished()) {
			resetDecorations();
			setActivityFinished(false);
		}
		activeNode = activityNodeMap.get(match.getXmofElementName());

		if (previouslyActiveNode != null && !(previouslyActiveNode instanceof StructuredActivityNode)) {
			decorateElement(previouslyActiveNode, DecorationType.NODE_TRAVERSED);
		}
		if (inStructuredNode != null) {
			if (activeNode.getInStructuredNode() == null
					|| !activeNode.getInStructuredNode().equals(inStructuredNode)) {
				previouslyActiveNode = inStructuredNode;
				decorateElement(previouslyActiveNode, DecorationType.STRUCTURED_NODE_TRAVERSED);
				inStructuredNode = null;
			}
		}
		if (activeNode == null)
			return false;
		if (activeNode instanceof StructuredActivityNode) {
			decorateElement(activeNode, DecorationType.STRUCTURED_NODE_ACTIVE);

			inStructuredNode = (StructuredActivityNode) activeNode;
		} else {
			decorateElement(activeNode, DecorationType.NODE_ACTIVE);
		}

		activeEdge = retrieveActiveEdge();
		if (activeEdge != null) {
			decorateElement(activeEdge, DecorationType.EDGE_ACTIVE);
		}
		previouslyActiveNode = activeNode;
		return true;

	}

	private ActivityEdge retrieveActiveEdge() {
		if (previouslyActiveNode == null)
			return null;
		ActivityEdgeId id = new ActivityEdgeId(previouslyActiveNode.getName(), activeNode.getName());
		return activityEdgeMap.get(id);
	}

	protected abstract void decorateElement(EObject element, DecorationType type);

	public abstract void resetDecorations();

	public boolean isActivityFinished() {
		return activityFinished;
	}

	public void setActivityFinished(boolean activityFinished) {
		this.activityFinished = activityFinished;
	}

	public Activity getActivity() {
		return activity;
	}

}
