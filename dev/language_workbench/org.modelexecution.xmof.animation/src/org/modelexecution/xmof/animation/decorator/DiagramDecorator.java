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
import org.modelexecution.xmof.animation.decorator.internal.EdgeId;
import org.modelexecution.xmof.animation.decorator.internal.ElementState;
import org.modelexecution.xmof.animation.mapping.Match;
import org.modelexecution.xmof.animation.mapping.XMOFType;

public abstract class DiagramDecorator {
	private boolean activityFinished = false;
	protected Activity activity;
	protected Map<String, ActivityNode> activityNodeMap;
	protected Map<EdgeId, ActivityEdge> activityEdgeMap;
	protected ActivityNode activeNode;
	protected ActivityNode previouslyActiveNode;
	protected StructuredActivityNode inStructuredNode = null;
	protected ActivityEdge activeEdge;
	protected ActivityEdge previouslyActiveEdge;

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

	public boolean decorateActivityElement(Match match) {
		if (activityNodeMap == null) {
			initializeMaps();
		}
		if (isActivityFinished()) {
			resetDiagram();

		}
		activeNode = activityNodeMap.get(match.getXmofElementName());

		if (previouslyActiveNode != null && !(previouslyActiveNode instanceof StructuredActivityNode)) {
			decorateElement(previouslyActiveNode, ElementState.TRAVERSED);
		}
		if (inStructuredNode != null) {
			if (activeNode.getInStructuredNode() == null
					|| !activeNode.getInStructuredNode().equals(inStructuredNode)) {
				previouslyActiveNode = inStructuredNode;
				decorateElement(previouslyActiveNode, ElementState.TRAVERSED);
				inStructuredNode = null;
			}
		}

		if (previouslyActiveEdge != null) {
			decorateElement(previouslyActiveEdge, ElementState.TRAVERSED);
		}

		if (activeNode != null) {
			if (activeNode instanceof StructuredActivityNode) {
				decorateElement(activeNode, ElementState.ACTIVE);

				inStructuredNode = (StructuredActivityNode) activeNode;
			} else {
				decorateElement(activeNode, ElementState.ACTIVE);
			}
		}

		activeEdge = retrieveActiveEdge();
		if (activeEdge != null) {
			decorateElement(activeEdge, ElementState.ACTIVE);
		}

		previouslyActiveNode = activeNode;
		previouslyActiveEdge = activeEdge;
		return activeNode != null;

	}

	private void resetDiagram() {
		resetDecorations();
		setActivityFinished(false);
		activeEdge = null;
		previouslyActiveEdge = null;
		inStructuredNode = null;
		activeNode = null;
		previouslyActiveNode = null;

	}

	protected void resetDecorations() {
		DecoratorService.clear(activity.getName());
		refreshDiagram();
	};

	protected abstract void refreshDiagram();

	protected void decorateElement(EObject element, ElementState state) {
		DecoratorService.addDecoratedElement(activity, element, state);

	}

	public boolean isActivityFinished() {
		return activityFinished;
	}

	public void setActivityFinished(boolean activityFinished) {
		this.activityFinished = activityFinished;
	}

	public Activity getActivity() {
		return activity;
	}

	private void processActivityEdge(ActivityEdge edge) {
		EdgeId id = null;
		ActivityNode source = null, target = null;
		if (edge instanceof ControlFlow) {
			source = edge.getSource();
			target = edge.getTarget();
		} else if (edge instanceof ObjectFlow) {
			source = retrieveConnectedNode(edge.getSource());
			target = retrieveConnectedNode(edge.getTarget());
		}
		if (source != null && target != null) {
			id = new EdgeId(source.getName(), target.getName());
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

	private ActivityEdge retrieveActiveEdge() {
		if (previouslyActiveNode == null)
			return null;
		EdgeId id = new EdgeId(previouslyActiveNode.getName(), activeNode.getName());
		return activityEdgeMap.get(id);
	}

}
