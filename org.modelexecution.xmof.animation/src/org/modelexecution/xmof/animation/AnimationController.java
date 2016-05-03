package org.modelexecution.xmof.animation;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.handler.ActivityDiagramHandler;
import org.modelexecution.xmof.animation.internal.Match;
import org.modelexecution.xmof.animation.internal.MatchingService;
import org.modelexecution.xmof.animation.internal.XMOFModelProcessor;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationController {

	private XMOFModelProcessor modelProcessor;
	private ActivityDiagramHandler diagramHandler;
	private MatchingService mseMatcher;

	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		modelProcessor = new XMOFModelProcessor(model);
		diagramHandler = new ActivityDiagramHandler(modelResource);
		mseMatcher = new MatchingService(model);
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		initialize();
	}

	private void initialize() {
		mseMatcher
				.setAllowedActivities(modelProcessor.getActivityMap().keySet());
	


	}

	public void processMSEOccurrence(MSEOccurrence mseOccurrence) {
		String fullName = mseOccurrence.getMse().getName();
		Match match = mseMatcher.matchMSEOccurence(fullName);

		processType(match);

	}

	private void processType(Match match) {
		switch (match.getType()) {
		case MAIN: {
			Activity main=modelProcessor.getActivityByName(match.getXmofElementName());
			openOrCreateAcitvityDiagram(main);
			break;
		}
		case ACTITVITY: {
			Activity main=modelProcessor.getActivityByName(match.getXmofElementName());
			openOrCreateAcitvityDiagram(main);
			break;
		}
		case ACTIVITYNODE: {
			decorateActivityNode(match.getXmofElementName());
			break;
		}
		case NODE: {
			decorateControlFlowNode(match.getXmofElementName());
			break;
		}
		default: {

		}
		}

	}
	
	private void decorateControlFlowNode(String xmofElementName) {
		// TODO Auto-generated method stub
		
	}

	private void decorateActivityNode(String xmofElementName) {
		// TODO Auto-generated method stub
		
	}

	private void openOrCreateAcitvityDiagram(Activity acitvity){
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
					diagramHandler.showDiagram(acitvity);
				}
			
		});
	}


}
