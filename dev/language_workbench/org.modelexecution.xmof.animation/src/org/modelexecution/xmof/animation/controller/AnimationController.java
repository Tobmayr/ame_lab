package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.MappingService;
import org.modelexecution.xmof.animation.decorator.DiagramDecorator;
import org.modelexecution.xmof.animation.ui.Activator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public abstract class AnimationController {

	private MappingService modelProcessor;
	private XMOFBasedModel model;
	protected Map<String, DiagramDecorator> diagramDecoratorMap;
	protected DiagramDecorator activeDecorator;
	protected Map<String, String> activityCallerMap;

	public AnimationController(XMOFBasedModel model) {
		this.model = model;
		modelProcessor = new MappingService(model);
		diagramDecoratorMap = new HashMap<String, DiagramDecorator>();
		activityCallerMap = new HashMap<>();
	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence,
			boolean verbose) {
		Match match = modelProcessor.matchDebugEvent(mseOccurrence.getMse()
				.getName());
		if (verbose) {
			String info = mseOccurrence.getMse().getName()
					+ " has been matched to:\n" + match.getXmofElementName();
			Activator.getDefault().getMessaggingSystem()
					.debug(info, Activator.PLUGIN_ID);
		}
		processType(match);

	}

	public void handleMain(Match match) {
		initializeDecorators();
		Activity activity = getModelProcessor().getActivityByName(
				match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());
	}

	protected abstract void openOrCreateAcitvityDiagram(Activity activity);

	public void handleActivity(Match match) {
		Activity activity = getModelProcessor().getActivityByName(
				match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activityCallerMap.put(activity.getName(),
				activeDecorator.getActivityName());
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());
	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN:
			handleMain(match);
			return;
		case ACTITVITY:
			handleActivity(match);
			return;
		case CONTROLNODE:
		case CALLOPERATION:
		case ACTIVITYNODE:
		case EXPANSIONREGION:
			decorateActivityNode(match);
			return;
		default:

		}
	}

	protected abstract void initializeDecorators();

	public MappingService getModelProcessor() {
		return modelProcessor;
	}

	public void setModelProcessor(MappingService modelProcessor) {
		this.modelProcessor = modelProcessor;
	}

	public XMOFBasedModel getModel() {
		return model;
	}

	public void setModel(XMOFBasedModel model) {
		this.model = model;
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
}
