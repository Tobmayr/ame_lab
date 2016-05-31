package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.MappingService;
import org.modelexecution.xmof.animation.decorator.ActivityDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.GraphitiActivityDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.handler.GraphitiActivityDiagramHandler;
import org.modelexecution.xmof.vm.XMOFBasedModel;
import org.modelexecution.xmof.animation.ui.Activator;

public class GraphitiAnimationController extends AnimationController {

	private GraphitiActivityDiagramHandler diagramHandler;


	public GraphitiAnimationController(XMOFBasedModel model,
			Resource modelResource) {
		super(model);
		diagramHandler = new GraphitiActivityDiagramHandler(modelResource);
	
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);

	}

	@Override
	protected  void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (String activityName : getModelProcessor().getActivityNames()) {
			diagramDecoratorMap.put(activityName,
					new GraphitiActivityDiagramDecorator(activityName,
							diagramHandler.getKernelEditor()));
		}
	}

	protected void openOrCreateAcitvityDiagram(Activity acitvity) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				diagramHandler.showDiagram(acitvity);
			}

		});
	}

	public GraphitiActivityDiagramHandler getDiagramHandler() {
		return diagramHandler;
	}

	public void setDiagramHandler(GraphitiActivityDiagramHandler diagramHandler) {
		this.diagramHandler = diagramHandler;
	}


}
