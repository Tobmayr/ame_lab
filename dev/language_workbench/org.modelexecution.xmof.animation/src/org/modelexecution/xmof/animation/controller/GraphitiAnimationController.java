package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.XMOFModelProcessor;
import org.modelexecution.xmof.animation.decorator.GraphitiActivityDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.handler.GraphitiActivityDiagramHandler;
import org.modelexecution.xmof.vm.XMOFBasedModel;
import org.modelexecution.xmof.animation.ui.Activator;

public class GraphitiAnimationController extends AnimationController {

	private GraphitiActivityDiagramHandler diagramHandler;
	private GraphitiActivityDiagramDecorator activeDecorator;
	private Map<String, String> activityCallerMap;

	private Map<String, GraphitiActivityDiagramDecorator> diagramDecoratorMap;

	public GraphitiAnimationController(XMOFBasedModel model,
			Resource modelResource) {
		super(model);
		diagramHandler = new GraphitiActivityDiagramHandler(modelResource);
		activityCallerMap = new HashMap<>();
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);

	}

	private void initializeDecorators() {
		diagramDecoratorMap = new HashMap<>();
		for (String activityName : getModelProcessor().getActivityNames()) {
			diagramDecoratorMap.put(activityName,
					new GraphitiActivityDiagramDecorator(activityName,
							diagramHandler.getKernelEditor()));
		}
	}

	protected void decorateActivityNode(Match match) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (activeDecorator != null) {
					if (!tryDecorateInCurrentActivity(match)) {

						if (tryDecorateInCallingAcitivty(match)) {
							activeDecorator.setActivityFinished(true);
						} else {
							activeDecorator.setActivityFinished(false);
						}
					}

				}

			}

			private boolean tryDecorateInCallingAcitivty(Match match) {
				String callingActivity = activityCallerMap.get(activeDecorator
						.getActivityName());
				if (callingActivity != null) {
					activeDecorator = diagramDecoratorMap.get(callingActivity
							.trim());
					if (activeDecorator.decorateActivityNode(match)) {
						openOrCreateAcitvityDiagram(getModelProcessor()
								.getActivityByName(callingActivity));
					}
					return true;
				}
				return false;

			}

			private boolean tryDecorateInCurrentActivity(Match match) {
				return activeDecorator.decorateActivityNode(match);
			}

		});

	}

	private void openOrCreateAcitvityDiagram(Activity acitvity) {
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

	public GraphitiActivityDiagramDecorator getActiveDecorator() {
		return activeDecorator;
	}

	public void setActiveDecorator(
			GraphitiActivityDiagramDecorator activeDecorator) {
		this.activeDecorator = activeDecorator;
	}

	@Override
	public void handleMain(Match match) {
		initializeDecorators();
		Activity activity = getModelProcessor().getActivityByName(
				match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());

	}

	@Override
	public void handleActivity(Match match) {
		Activity activity = getModelProcessor().getActivityByName(
				match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activityCallerMap.put(activity.getName(),
				activeDecorator.getActivityName());
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());
	}

	@Override
	public void handleExecutableNode(Match match) {
		decorateActivityNode(match);

	}

	@Override
	public void handleControlNode(Match match) {
		decorateActivityNode(match);

	}

	@Override
	public void handleExpansionRegion(Match match) {
		decorateActivityNode(match);

	}

}
