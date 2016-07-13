package org.modelexecution.xmof.animation.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.PlatformUI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.DecoratorService;
import org.modelexecution.xmof.animation.decorator.DiagramDecorator;
import org.modelexecution.xmof.animation.handler.DiagramHandler;
import org.modelexecution.xmof.animation.mapping.MappingService;
import org.modelexecution.xmof.animation.mapping.Match;
import org.modelexecution.xmof.animation.ui.Activator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

import fr.inria.diverse.trace.commons.model.trace.MSEOccurrence;

public abstract class AnimationController {

	private MappingService mappingService;
	private XMOFBasedModel model;
	protected Map<String, DiagramDecorator> diagramDecoratorMap;
	protected DiagramDecorator activeDecorator;
	protected Map<String, String> activityCallerMap;
	protected DiagramHandler diagramHandler;

	public AnimationController(XMOFBasedModel model, DiagramHandler concreteHandler) {
		this.model = model;
		mappingService = new MappingService(model);
		diagramDecoratorMap = new HashMap<String, DiagramDecorator>();
		activityCallerMap = new HashMap<>();
		this.diagramHandler = concreteHandler;
		DecoratorService.reset();
	}

	public void processMSE(MSEOccurrence mseOccurrence, boolean verbose) {
		Match match = mappingService.matchDebugEvent(mseOccurrence.getMse().getName());
		if (verbose) {
			String info = mseOccurrence.getMse().getName() + " has been matched to:\n" + match.getXmofElementName();
			Activator.getDefault().getMessaggingSystem().debug(info, Activator.PLUGIN_ID);
		}
		processType(match);

	}

	public void handleMain(Match match) {
		initializeDecorators();
		Activity activity = getModelProcessor().getActivityByName(match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());
		DecoratorService.intializeContainer(activity.getName());
	}

	protected void openOrCreateAcitvityDiagram(Activity activity) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				diagramHandler.openOrShowDiagram(activity);
			}

		});
	}

	public  void dispose(){
		DecoratorService.reset();
	}

	public void handleActivity(Match match) {
		Activity activity = getModelProcessor().getActivityByName(match.getXmofElementName());
		openOrCreateAcitvityDiagram(activity);
		activityCallerMap.put(activity.getName(), activeDecorator.getActivity().getName());
		activeDecorator = diagramDecoratorMap.get(activity.getName().trim());
		DecoratorService.intializeContainer(activity.getName());
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
		case STRUCTUREDACTIVITYNODE:
			decorateActivityNode(match);
			return;
		default:

		}
	}

	protected abstract void initializeDecorators();

	public MappingService getModelProcessor() {
		return mappingService;
	}

	public void setModelProcessor(MappingService modelProcessor) {
		this.mappingService = modelProcessor;
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
				String callingActivity = activityCallerMap.get(activeDecorator.getActivity().getName());
				if (callingActivity != null) {
					activeDecorator = diagramDecoratorMap.get(callingActivity.trim());
					if (activeDecorator.decorateActivityElement(match)) {
						openOrCreateAcitvityDiagram(getModelProcessor().getActivityByName(callingActivity));
					}
					return true;
				}
				return false;

			}

			private boolean tryDecorateInCurrentActivity(Match match) {
				return activeDecorator.decorateActivityElement(match);
			}

		});

	}

}
