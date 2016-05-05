package org.modelexecution.xmof.animation;

import java.util.Collection;
import java.util.List;

import javafx.scene.AmbientLight;

import org.eclipse.emf.ecore.resource.Resource;
import org.gemoc.executionframework.engine.mse.LogicalStep;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.gemoc.executionframework.engine.mse.impl.MSEImpl;
import org.gemoc.xdsmlframework.api.core.EngineStatus.RunStatus;
import org.gemoc.xdsmlframework.api.core.IBasicExecutionEngine;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.modelexecution.xmof.animation.controller.AnimationController;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationAddon implements IEngineAddon {

	private AnimationController animationController;
	String s="";

	@Override
	public void engineAboutToStart(IBasicExecutionEngine engine) {

		return;

	}

	@Override
	public void engineStarted(IBasicExecutionEngine executionEngine) {
	

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
		// TODO Auto-generated method stub

	}

	@Override
	public void aboutToSelectLogicalStep(IBasicExecutionEngine engine,
			Collection<LogicalStep> logicalSteps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void proposedLogicalStepsChanged(IBasicExecutionEngine engine,
			Collection<LogicalStep> logicalSteps) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logicalStepSelected(IBasicExecutionEngine engine,
			LogicalStep selectedLogicalStep) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aboutToExecuteLogicalStep(IBasicExecutionEngine engine,
			LogicalStep logicalStepToExecute) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logicalStepExecuted(IBasicExecutionEngine engine,
			LogicalStep logicalStepExecuted) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void aboutToExecuteMSEOccurrence(IBasicExecutionEngine engine,
			MSEOccurrence mseOccurrence) {
		s+=mseOccurrence.getMse().getName()+"\n";
		animationController.processMSEOccurrence(mseOccurrence);
		return;
	}

	@Override
	public void mseOccurrenceExecuted(IBasicExecutionEngine engine,
			MSEOccurrence mseOccurrence) {
	

	}

	@Override
	public void engineStatusChanged(IBasicExecutionEngine engine,
			RunStatus newStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> validate(List<IEngineAddon> otherAddons) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize(XMOFBasedModel model, Resource resource) {
		animationController = new AnimationController(model, resource);

	}

}
