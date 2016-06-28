package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.decorator.SiriusDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.service.SiriusDecoratorService;
import org.modelexecution.xmof.animation.handler.SiriusDiagramHandler;
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
	protected void openOrCreateAcitvityDiagram(Activity activity) {
		diagramHandler.openOrShowDiagram(activity);
		SiriusDecoratorService.intializeContainer(activity.getName());

	}

	@Override
	protected void decorateActivityNode(Match match) {
		super.decorateActivityNode(match);
		refresh();
	}

	@Override
	public void dispose() {
		SiriusDecoratorService.reset();

	}

	public void refresh() {
		((SiriusDiagramHandler) diagramHandler).refreshDiagram(activeDecorator.getActivity().getName());
	}

}
