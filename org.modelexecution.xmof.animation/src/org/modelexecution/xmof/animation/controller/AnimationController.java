package org.modelexecution.xmof.animation.controller;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.ActivityDiagramHandler;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.XMOFIndexingService;
import org.modelexecution.xmof.animation.controller.internal.XMOFMatchingService;
import org.modelexecution.xmof.animation.decorator.ActivityDiagramDecorator;
import org.modelexecution.xmof.animation.decorator.ActivityElementDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationController {

	private XMOFIndexingService modelService;
	private ActivityDiagramHandler diagramHandler;
	private XMOFMatchingService mseMatcher;
	private String currentlyActiveActivity;

	public AnimationController(XMOFBasedModel model, Resource modelResource) {

		diagramHandler = new ActivityDiagramHandler(modelResource);
		mseMatcher = new XMOFMatchingService(model);
		modelService = new XMOFIndexingService(model);
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		initialize();
	}

	private void initialize() {
		mseMatcher.setAllowedActivities(modelService.getActivityMap().keySet());

	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence) {
		String fullName = mseOccurrence.getMse().getName();
		Match match = mseMatcher.matchMSEOccurence(fullName);
		processType(match);

	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN: {
			modelService.passEditorToDiagramDecorator(diagramHandler.getKernelEditor());
			Activity activity = modelService.getActivityByName(match
					.getXmofElementName());
			prepareActivty(activity);
			currentlyActiveActivity = activity.getName();
			return;
		}
		case ACTITVITY: {
			Activity activity = modelService.getActivityByName(match
					.getXmofElementName());
			prepareActivty(activity);
			currentlyActiveActivity = activity.getName();
			return;
		}
		case ACTIVITYNODE: {
			decorateActivityNode(match.getXmofElementName());
			return;
		}
		case CONTROLNODE: {
			decorateControlFlowNode(match.getXmofElementName());
			return;
		}
		default: {

		}
		}

	}

	private void prepareActivty(Activity activity) {
		openOrCreateAcitvityDiagram(activity);

	}

	private void decorateControlFlowNode(String xmofElementName) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				ActivityDiagramDecorator decorator = modelService
						.getDiagramDecoratorForActivity(xmofElementName);
				if (decorator != null) {
					decorator.decorateActivityNode(xmofElementName);
				}

			}

		});

	}

	private void decorateActivityNode(String xmofElementName) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				ActivityDiagramDecorator decorator = modelService
						.getDiagramDecoratorForActivity(xmofElementName);
				if (decorator != null) {
					decorator.decorateActivityNode(xmofElementName);
				}

			}

		});

	}

	private void openOrCreateAcitvityDiagram(Activity acitvity) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				diagramHandler.showDiagram(acitvity);
			}

		});
	}

}
