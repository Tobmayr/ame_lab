package org.modelexecution.xmof.animation;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.gemoc.xdsmlframework.api.core.EngineStatus.RunStatus;
import org.gemoc.xdsmlframework.api.core.IBasicExecutionEngine;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.modelexecution.xmof.animation.controller.AnimationController;
import org.modelexecution.xmof.animation.controller.GraphitiAnimationController;
import org.modelexecution.xmof.animation.controller.SiriusAnimationController;
import org.modelexecution.xmof.gemoc.engine.ui.commons.RunConfiguration;
import org.modelexecution.xmof.gemoc.engine.ui.commons.XMOFRepresentation;
import org.modelexecution.xmof.vm.XMOFBasedModel;

import fr.inria.diverse.trace.commons.model.trace.Step;

public class ModelAnimator implements IEngineAddon {

	private AnimationController animationController;

	public void initialize(XMOFBasedModel model, Resource resource, RunConfiguration animConfig) {
		String representation = animConfig.getXMOFRepresentation();
		if (representation.equals(XMOFRepresentation.REPRESENTATION_GRAPHITI)) {
			animationController = new GraphitiAnimationController(model, resource);
		} else if (representation.equals(XMOFRepresentation.REPRESENTATION_SIRIUS)) {
			String airdURIString = "platform:/resource"+animConfig.getSiriusRepresentationModelPath();
			URI airdURI = URI.createURI(airdURIString);
			animationController = new SiriusAnimationController(model, airdURI);
		}
	}

	@Override
	public void engineAboutToStart(IBasicExecutionEngine engine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void engineStarted(IBasicExecutionEngine executionEngine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void engineAboutToStop(IBasicExecutionEngine engine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void engineStopped(IBasicExecutionEngine engine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void engineAboutToDispose(IBasicExecutionEngine engine) {
		if (animationController!=null){
			animationController.dispose();
			animationController=null;
		}

	}

	@Override
	public void aboutToSelectStep(IBasicExecutionEngine engine, Collection<Step> steps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void proposedStepsChanged(IBasicExecutionEngine engine, Collection<Step> steps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepSelected(IBasicExecutionEngine engine, Step selectedStep) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aboutToExecuteStep(IBasicExecutionEngine engine, Step stepToExecute) {
		if (animationController != null) {
			animationController.processMSE(stepToExecute.getMseoccurrence(), true);
		}

	}

	@Override
	public void stepExecuted(IBasicExecutionEngine engine, Step stepExecuted) {
		// TODO Auto-generated method stub

	}

	@Override
	public void engineStatusChanged(IBasicExecutionEngine engine, RunStatus newStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> validate(List<IEngineAddon> otherAddons) {
		// TODO Auto-generated method stub
		return null;
	}
}
