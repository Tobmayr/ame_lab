package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.MappingService;
import org.modelexecution.xmof.animation.decorator.DiagramDecorator;
import org.modelexecution.xmof.animation.decorator.GraphitiDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.handler.GraphitiDiagramHandler;
import org.modelexecution.xmof.vm.XMOFBasedModel;
import org.modelexecution.xmof.animation.ui.Activator;

public class GraphitiAnimationController extends AnimationController {

	private GraphitiDiagramHandler diagramHandler;


	public GraphitiAnimationController(XMOFBasedModel model,
			Resource modelResource) {
		super(model);
		diagramHandler = new GraphitiDiagramHandler(modelResource);
	
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);

	}

	@Override
	protected  void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (String activityName : getModelProcessor().getActivityNames()) {
			diagramDecoratorMap.put(activityName,
					new GraphitiDiagramDecorator(activityName,
							diagramHandler.getKernelEditor()));
		}
	}

	protected void openOrCreateAcitvityDiagram(Activity acitvity) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				diagramHandler.openOrShowDiagram(acitvity);
			}

		});
	}

	public GraphitiDiagramHandler getDiagramHandler() {
		return diagramHandler;
	}

	public void setDiagramHandler(GraphitiDiagramHandler diagramHandler) {
		this.diagramHandler = diagramHandler;
	}


}
