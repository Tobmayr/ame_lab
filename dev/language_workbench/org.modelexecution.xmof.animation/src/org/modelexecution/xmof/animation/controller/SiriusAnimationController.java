package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.SiriusDiagramDecorator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class SiriusAnimationController extends AnimationController {

	public SiriusAnimationController(XMOFBasedModel model) {
		super(model);
	}

	@Override
	protected void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (String activityName : getModelProcessor().getActivityNames()) {
			diagramDecoratorMap.put(activityName,
					new SiriusDiagramDecorator(getModelProcessor().getActivityByName(activityName)));
		}

	}

	@Override
	protected void openOrCreateAcitvityDiagram(Activity activity) {
		
		
	}

}
