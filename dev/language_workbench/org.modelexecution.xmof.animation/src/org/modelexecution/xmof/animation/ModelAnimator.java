package org.modelexecution.xmof.animation;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.gemoc.executionframework.engine.mse.LogicalStep;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.gemoc.xdsmlframework.api.core.EngineStatus.RunStatus;
import org.gemoc.xdsmlframework.api.core.IBasicExecutionEngine;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.modelexecution.xmof.animation.controller.AnimationController;
import org.modelexecution.xmof.animation.controller.GraphitiAnimationController;
import org.modelexecution.xmof.animation.controller.SiriusAnimationController;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class ModelAnimator implements IEngineAddon {
	private static final boolean USE_GRAPHITI = false;
	private AnimationController animationController;

	public void initialize(XMOFBasedModel model, Resource resource) {
		if (USE_GRAPHITI) {
			animationController = new GraphitiAnimationController(model, resource);
		} else {
			initializeSiriusController(model,resource);
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
		animationController.dispose();

	}

	@Override
	public void aboutToSelectLogicalStep(IBasicExecutionEngine engine, Collection<LogicalStep> logicalSteps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void proposedLogicalStepsChanged(IBasicExecutionEngine engine, Collection<LogicalStep> logicalSteps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logicalStepSelected(IBasicExecutionEngine engine, LogicalStep selectedLogicalStep) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aboutToExecuteLogicalStep(IBasicExecutionEngine engine, LogicalStep logicalStepToExecute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logicalStepExecuted(IBasicExecutionEngine engine, LogicalStep logicalStepExecuted) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void aboutToExecuteMSEOccurrence(IBasicExecutionEngine engine, MSEOccurrence mseOccurrence) {
		if (animationController != null) {
			animationController.processMSEOccurrence(mseOccurrence, true);
		}

	}

	@Override
	public void mseOccurrenceExecuted(IBasicExecutionEngine engine, MSEOccurrence mseOccurrence) {
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

	private void initializeSiriusController(XMOFBasedModel model, Resource resource) {
		String uriString = resource.getURI().toString();
		URI airdURI;
		if (uriString.contains("petrinet2")) {
			 airdURI= URI.createURI(
					"platform:/resource/org.modelexecution.xmof.examples.petrinet2.xmof.sirius/representations.aird");
		}else{
			airdURI = URI.createURI(
					"platform:/resource/org.modelexecution.xmof.examples.petrinet.xmof.sirius/representations.aird");
		}
		
		animationController = new SiriusAnimationController(model, airdURI);
	}
}
