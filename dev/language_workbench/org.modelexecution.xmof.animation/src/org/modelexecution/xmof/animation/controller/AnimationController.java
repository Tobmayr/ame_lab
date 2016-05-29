package org.modelexecution.xmof.animation.controller;

import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.XMOFModelProcessor;
import org.modelexecution.xmof.animation.ui.Activator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public abstract class AnimationController {

	private XMOFModelProcessor modelProcessor;
	private XMOFBasedModel model;

	public AnimationController(XMOFBasedModel model) {
		this.model = model;
		modelProcessor = new XMOFModelProcessor(model);
	}

	
	public void processMSEOccurrence(MSEOccurrence mseOccurrence, boolean verbose) {
		Match match = modelProcessor.matchMSEOccurence(mseOccurrence.getMse()
				.getName());
		if (verbose) {
			String info = mseOccurrence.getMse().getName()
					+ " has been matched to:\n" + match.getXmofElementName();
			Activator.getDefault().getMessaggingSystem()
					.debug(info, Activator.PLUGIN_ID);
		}
		processType(match);

	}
	
	public abstract void handleMain(Match match);
	public abstract void handleActivity(Match match);
	public abstract void handleExecutableNode(Match match);
	public abstract void handleControlNode(Match match);
	public abstract void handleExpansionRegion(Match match);
	
	
	
	
	private void processType(Match match){
		switch (match.getType()) {
		case MAIN:
			handleMain(match);
			return;
		case ACTITVITY:
			handleActivity(match);
			return;
		case CONTROLNODE:
			handleControlNode(match);
			return;
		case CALLOPERATION:
		case ACTIVITYNODE:
			handleExecutableNode(match);
			return;
		case EXPANSIONREGION:
			handleExpansionRegion(match);
			return;
		default: 
		
		}
	}
	



	public XMOFModelProcessor getModelProcessor() {
		return modelProcessor;
	}


	public void setModelProcessor(XMOFModelProcessor modelProcessor) {
		this.modelProcessor = modelProcessor;
	}


	public XMOFBasedModel getModel() {
		return model;
	}


	public void setModel(XMOFBasedModel model) {
		this.model = model;
	}
	
	protected abstract void decorateActivityNode(Match match);
}
