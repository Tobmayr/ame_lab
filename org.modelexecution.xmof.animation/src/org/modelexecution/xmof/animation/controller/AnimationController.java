package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.XMOFIndexingService;
import org.modelexecution.xmof.animation.controller.internal.XMOFMatchingService;
import org.modelexecution.xmof.animation.decorator.ActivityDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.handler.ActivityDiagramHandler;
import org.modelexecution.xmof.vm.XMOFBasedModel;
import org.modelexecution.xmof.animation.ui.Activator;

public class AnimationController {

	private XMOFIndexingService indexingService;
	private ActivityDiagramHandler diagramHandler;
	private XMOFMatchingService mseMatcher;
	private ActivityDiagramDecorator activeDecorator;
	private Map<String, String> activityCallerMap;
	private XMOFBasedModel model;

	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		this.model = model;
		diagramHandler = new ActivityDiagramHandler(modelResource);
		mseMatcher = new XMOFMatchingService(this);
		indexingService = new XMOFIndexingService(model);
		activityCallerMap = new HashMap<>();
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		initialize();
	}

	private void initialize() {
		mseMatcher.setAllowedActivities(indexingService.getActivityMap()
				.keySet());

	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence) {
		String fullName = mseOccurrence.getMse().getName();
		Match match = mseMatcher.matchMSEOccurence(fullName);
		Activator.getDefault().getMessaggingSystem()
				.debug(fullName, Activator.PLUGIN_ID);
		processType(match);

	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN: {
			indexingService.passEditorToDiagramDecorator(diagramHandler
					.getKernelEditor());
			Activity activity = indexingService.getActivityByName(match
					.getXmofElementName());
			openOrCreateAcitvityDiagram(activity);
			activeDecorator = indexingService
					.getDiagramDecoratorForActivity(activity.getName());

			return;
		}
		case ACTITVITY: {
			Activity activity = indexingService.getActivityByName(match
					.getXmofElementName());
			openOrCreateAcitvityDiagram(activity);
			activityCallerMap.put(activity.getName(),
					activeDecorator.getActivityName());
			activeDecorator = indexingService
					.getDiagramDecoratorForActivity(activity.getName());
			return;
		}
		case CALLOPERATION:
		case CONTROLNODE:
		case ACTIVITYNODE: {
			decorateActivityNode(match.getXmofElementName(),
					DecorationType.NODE);
			return;
		}
		case EXPANSIONREGION: {
			decorateActivityNode(match.getXmofElementName(),
					DecorationType.EXPANSION_REGION);
			return;
		}

		default: {

		}
		}

	}

	private void decorateActivityNode(String xmofElementName,
			DecorationType type) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (activeDecorator != null) {
					if (!tryDecorateInCurrentActivity(xmofElementName, type)) {
						
						if (tryDecorateInCallingAcitivty(xmofElementName, type)){
							activeDecorator.setActivityFinished(true);
						}else{
							activeDecorator.setActivityFinished(false);
						}
					}

				}

			}

			private boolean tryDecorateInCallingAcitivty(String xmofElementName,
					DecorationType type) {
				String callingActivity = activityCallerMap
						.get(activeDecorator.getActivityName());
				if (callingActivity != null) {
					activeDecorator = indexingService
							.getDiagramDecoratorForActivity(callingActivity);
					if (activeDecorator.decorateActivityNode(
							xmofElementName, type)) {
						openOrCreateAcitvityDiagram(indexingService
								.getActivityByName(callingActivity));
					}
					return true;
				}
				return false;

			}

			private boolean tryDecorateInCurrentActivity(
					String xmofElementName, DecorationType type) {
				return activeDecorator.decorateActivityNode(xmofElementName,
						type);
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

	public XMOFIndexingService getModelService() {
		return indexingService;
	}

	public void setModelService(XMOFIndexingService modelService) {
		this.indexingService = modelService;
	}

	public ActivityDiagramHandler getDiagramHandler() {
		return diagramHandler;
	}

	public void setDiagramHandler(ActivityDiagramHandler diagramHandler) {
		this.diagramHandler = diagramHandler;
	}

	public XMOFMatchingService getMseMatcher() {
		return mseMatcher;
	}

	public void setMseMatcher(XMOFMatchingService mseMatcher) {
		this.mseMatcher = mseMatcher;
	}

	public ActivityDiagramDecorator getActiveDecorator() {
		return activeDecorator;
	}

	public void setActiveDecorator(ActivityDiagramDecorator activeDecorator) {
		this.activeDecorator = activeDecorator;
	}

	public XMOFBasedModel getModel() {
		return model;
	}

	public void setModel(XMOFBasedModel model) {
		this.model = model;
	}

}
