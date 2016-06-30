package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.GraphitiDiagramDecorator;
import org.modelexecution.xmof.animation.handler.GraphitiDiagramHandler;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class GraphitiAnimationController extends AnimationController {

	public GraphitiAnimationController(XMOFBasedModel model, Resource modelResource) {
		super(model, new GraphitiDiagramHandler(modelResource));
	}

	@Override
	protected void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (Activity activity : getModelProcessor().getActivities()) {
			diagramDecoratorMap.put(activity.getName(), new GraphitiDiagramDecorator(activity,
					((GraphitiDiagramHandler) diagramHandler).getKernelEditor()));
		}
	}


}
