package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.Map;

import org.modelexecution.xmof.Syntax.Activities.CompleteStructuredActivities.StructuredActivityNode;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;

public abstract class DiagramDecorator {
	private boolean activityFinished = false;
	protected Activity activity;
	protected Map<String, ActivityNode> activityNodeMap;
	protected ActivityNode activeNode;
	protected ActivityNode previouslyActiveNode;
	protected StructuredActivityNode inStructuredNode = null;

	public DiagramDecorator(Activity activity) {

		this.activity = activity;
	}

	public void initializeActivityNodeMap() {
		activityNodeMap = new HashMap<>();

		for (ActivityNode node : activity.getNode()) {
			processActivityNode(node);

		}
	}

	private void processActivityNode(ActivityNode node) {
		if (node.getName() != null) {
			if (node instanceof StructuredActivityNode) {

				getActivityNodes((StructuredActivityNode) node);
			}
			activityNodeMap.put(node.getName(), node);
		}
	}

	private void getActivityNodes(StructuredActivityNode expNode) {
		for (ActivityNode actNode : expNode.getNode()) {
			processActivityNode(actNode);
		}

	}

	public boolean decorateActivityNode(Match match) {
		if (activityNodeMap == null) {
			initializeActivityNodeMap();
		}
		if (isActivityFinished()) {
			resetDecorations();
			setActivityFinished(false);
		}
		activeNode = activityNodeMap.get(match.getXmofElementName());

		if (previouslyActiveNode != null && !(previouslyActiveNode instanceof StructuredActivityNode)) {
			decoratePreviouslyActiveNode(DecorationType.NODE_TRAVERSED);
		}
		if (inStructuredNode != null) {
			if (activeNode.getInStructuredNode() == null
					|| !activeNode.getInStructuredNode().equals(inStructuredNode)) {
				previouslyActiveNode=inStructuredNode;
				decoratePreviouslyActiveNode(DecorationType.STRUCTURED_NODE_TRAVERSED);
				inStructuredNode = null;
			}
		}
		if (activeNode == null)
			return false;
		if (activeNode instanceof StructuredActivityNode) {
			decorateActiveNode(DecorationType.STRUCTURED_NODE_ACTIVE);
			inStructuredNode = (StructuredActivityNode) activeNode;
		} else {
			decorateActiveNode(DecorationType.NODE_ACTIVE );
		}
		return true;

	}

	protected abstract void decoratePreviouslyActiveNode(DecorationType nodeTraversed);

	protected abstract void decorateActiveNode(DecorationType structuredNodeActive);

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
