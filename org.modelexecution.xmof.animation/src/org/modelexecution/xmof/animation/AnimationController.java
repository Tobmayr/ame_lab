package org.modelexecution.xmof.animation;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.handler.ActivityElementDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;
import org.modelexecution.xmof.animation.handler.ActivityDiagramHandler;
import org.modelexecution.xmof.animation.internal.Match;
import org.modelexecution.xmof.animation.internal.MatchingService;
import org.modelexecution.xmof.animation.internal.XMOFModelProcessor;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationController {

	private XMOFModelProcessor modelProcessor;
	private ActivityDiagramHandler diagramHandler;
	private MatchingService mseMatcher;
	private ActivityElementDecorator decorator;

	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		modelProcessor = new XMOFModelProcessor(model);
		diagramHandler = new ActivityDiagramHandler(modelResource);
		mseMatcher = new MatchingService(model);
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		initialize();
	}

	private void initialize() {
		mseMatcher.setAllowedActivities(modelProcessor.getActivityMap()
				.keySet());

	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence) {
		String fullName = mseOccurrence.getMse().getName();
		Match match = mseMatcher.matchMSEOccurence(fullName);

		processType(match);

	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN: {
			decorator = new ActivityElementDecorator(diagramHandler.getKernelEditor());
			Activity activity = modelProcessor.getActivityByName(match
					.getXmofElementName());
			prepareActivty(activity);
		
			return;
		}
		case ACTITVITY: {
			Activity activity = modelProcessor.getActivityByName(match
					.getXmofElementName());
			prepareActivty(activity);
			return;
		}
		case ACTIVITYNODE: {
			decorateActivityNode(match.getXmofElementName());
			return;
		}
		case NODE: {
			decorateControlFlowNode(match.getXmofElementName());
			return;
		}
		default: {

		}
		}

	}

	private void prepareActivty(Activity activity) {
		openOrCreateAcitvityDiagram(activity);
		decorator.setCurrentActvity(activity);
	}

	private void decorateControlFlowNode(String xmofElementName) {
		// DecoratorService.decorateNode(xmofElementName);

	}

	private void decorateActivityNode(String xmofElementName) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			  @Override
			  public void run() {
				  decorator.decorateActivityNode(xmofElementName);
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
