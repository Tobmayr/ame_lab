package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.DecoratorService;
import org.modelexecution.xmof.animation.decorator.SiriusDiagramDecorator;
import org.modelexecution.xmof.animation.handler.SiriusDiagramHandler;
import org.modelexecution.xmof.animation.mapping.Match;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class SiriusAnimationController extends AnimationController {

	public SiriusAnimationController(XMOFBasedModel model, URI airdURI) {
		super(model, new SiriusDiagramHandler(airdURI));
	}

	@Override
	protected void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (String activityName : getModelProcessor().getActivityNames()) {
			diagramDecoratorMap.put(activityName,
					new SiriusDiagramDecorator(getModelProcessor().getActivityByName(activityName), this));
		}

	}


	@Override
	protected void decorateActivityNode(Match match) {
		super.decorateActivityNode(match);
		refresh();
	}

	

	public void refresh() {
		((SiriusDiagramHandler) diagramHandler).refreshDiagram(activeDecorator.getActivity().getName());
	}

}
