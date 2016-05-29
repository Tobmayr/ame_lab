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

public class AnimationController {

	private XMOFModelProcessor modelProcessor;
	private GraphitiActivityDiagramHandler diagramHandler;
	private GraphitiActivityDiagramDecorator activeDecorator;
	private Map<String, String> activityCallerMap;
	private XMOFBasedModel model;
	private Map<String, GraphitiActivityDiagramDecorator> diagramDecoratorMap;

	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		this.model = model;
		diagramHandler = new GraphitiActivityDiagramHandler(modelResource);
		modelProcessor = new XMOFModelProcessor(model);
		activityCallerMap = new HashMap<>();
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		
	}

	
	private void initializeDecorators(){
		diagramDecoratorMap= new HashMap<>();
		for (String activityName:modelProcessor.getActivityNames()){
			diagramDecoratorMap.put(activityName, new GraphitiActivityDiagramDecorator(activityName,diagramHandler.getKernelEditor()));
		}
	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence) {
		String fullName = mseOccurrence.getMse().getName();
		Match match = modelProcessor.matchMSEOccurence(fullName);
		Activator.getDefault().getMessaggingSystem()
				.debug(fullName, Activator.PLUGIN_ID);
		processType(match);

	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN: {
			initializeDecorators();
			Activity activity = modelProcessor.getActivityByName(match
					.getXmofElementName());
			openOrCreateAcitvityDiagram(activity);
			activeDecorator = diagramDecoratorMap.get(activity.getName().trim());

			return;
		}
		case ACTITVITY: {
			Activity activity = modelProcessor.getActivityByName(match
					.getXmofElementName());
			openOrCreateAcitvityDiagram(activity);
			activityCallerMap.put(activity.getName(),
					activeDecorator.getActivityName());
			activeDecorator = 
					diagramDecoratorMap.get(activity.getName().trim());
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
					activeDecorator = diagramDecoratorMap.get(callingActivity.trim());
					if (activeDecorator.decorateActivityNode(
							xmofElementName, type)) {
						openOrCreateAcitvityDiagram(modelProcessor
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

	public XMOFModelProcessor getModelService() {
		return modelProcessor;
	}

	public void setModelService(XMOFModelProcessor modelService) {
		this.modelProcessor = modelService;
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

	public void setActiveDecorator(GraphitiActivityDiagramDecorator activeDecorator) {
		this.activeDecorator = activeDecorator;
	}

	public XMOFBasedModel getModel() {
		return model;
	}

	public void setModel(XMOFBasedModel model) {
		this.model = model;
	}

}
