package org.modelexecution.xmof.animation.decorator;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.impl.ExpansionRegionImpl;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.decorator.service.SiriusDecoratorService;

public class SiriusDiagramDecorator extends DiagramDecorator {

	private Set<String> activityNodes;

	public SiriusDiagramDecorator(Activity activity) {
		super(activity);
		intializeActivityNodesSet(activity);
	}

	private void intializeActivityNodesSet(Activity activity) {
		activityNodes = new HashSet<>();
		for (ActivityNode node : activity.getNode()) {
			processActivityNode(node);
		}

	}

	private void processActivityNode(ActivityNode node) {
		if (node.getName() != null) {
			if (node instanceof ExpansionRegionImpl) {

				getActivityNodes((ExpansionRegionImpl) node);
			}
			activityNodes.add(node.getName());
		}

	}

	private void getActivityNodes(ExpansionRegionImpl expNode) {
		for (ActivityNode actNode : expNode.getNode()) {
			processActivityNode(actNode);
		}

	}

	@Override
	public boolean decorateActivityElement(Match match) {
		if (isActivityFinished()) {

			setActivityFinished(false);
		}

		if (activityNodes.contains(match.getXmofElementName().trim())) {
			SiriusDecoratorService.setActiveElement(match, getActivity().getName());
			return true;
		}

		return false;

	}

	@Override
	public void resetDecorations() {
		SiriusDecoratorService.clear();

	}

	@Override
	protected void decorateElement(EObject element, DecorationType type) {
		// TODO Auto-generated method stub

	}

}
