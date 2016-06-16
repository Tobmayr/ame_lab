package org.modelexecution.xmof.animation.decorator;

import java.util.HashSet;
import java.util.Set;

import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.impl.ExpansionRegionImpl;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.decorator.service.SiriusDecoratorService;

public class SiriusDiagramDecorator extends DiagramDecorator {

	private Set<String> activityNodes;

	public SiriusDiagramDecorator(Activity activity) {
		super(activity.getName());
		intializeActivityNodesSet(activity);
	}

	private void intializeActivityNodesSet(Activity activity) {
		activityNodes= new HashSet<>();
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
	public boolean decorateActivityNode(Match match) {
	 if (isActivityFinished()){
		SiriusDecoratorService.clear();
		setActivityFinished(false);
	 }
	 
	 if (activityNodes.contains(match.getXmofElementName().trim())){
		 SiriusDecoratorService.setActiveElement(match, getActivityName());
		 return true;
	 }
	 
	 return false;
	
	}
	
	public boolean decorateExpansionRegion(Match match){
		
		
		return false;
	}

}
