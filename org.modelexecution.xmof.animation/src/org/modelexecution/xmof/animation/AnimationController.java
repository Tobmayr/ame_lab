package org.modelexecution.xmof.animation;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.decorator.handler.ActivityElementDecorator;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;
import org.modelexecution.xmof.animation.handler.ActivityDiagramHandler;
import org.modelexecution.xmof.animation.internal.Match;
import org.modelexecution.xmof.animation.internal.XMOFMatchingService;
import org.modelexecution.xmof.animation.internal.XMOFIndexingService;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationController {

	private XMOFIndexingService modelProcessor;
	private ActivityDiagramHandler diagramHandler;
	private XMOFMatchingService mseMatcher;
	private ActivityElementDecorator decorator;
	String s="";

	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		modelProcessor = new XMOFIndexingService(model);
		diagramHandler = new ActivityDiagramHandler(modelResource);
		mseMatcher = new XMOFMatchingService(model);
		
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
		s+=match.getXmofElementName()+"\n";
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
				  decorator.decorateActivityNode(xmofElementName);
			  }

		});

	}

	private void decorateActivityNode(String xmofElementName) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
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
