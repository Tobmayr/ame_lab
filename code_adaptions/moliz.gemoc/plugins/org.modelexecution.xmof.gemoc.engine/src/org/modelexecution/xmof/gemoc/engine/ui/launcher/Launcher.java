package org.modelexecution.xmof.gemoc.engine.ui.launcher;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.emf.ecore.EObject;
import org.gemoc.commons.eclipse.ui.ViewHelper;
import org.gemoc.execution.sequential.javaengine.ui.debug.GenericSequentialModelDebugger;
import org.gemoc.executionframework.engine.commons.EngineContextException;
import org.gemoc.executionframework.engine.commons.ModelExecutionContext;
import org.gemoc.executionframework.engine.ui.debug.AbstractGemocDebugger;
import org.gemoc.executionframework.engine.ui.launcher.AbstractSequentialGemocLauncher;
import org.gemoc.executionframework.ui.views.engine.EnginesStatusView;
import org.gemoc.xdsmlframework.api.core.ExecutionMode;
import org.gemoc.xdsmlframework.api.core.IBasicExecutionEngine;
import org.gemoc.xdsmlframework.api.core.ISequentialExecutionEngine;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.modelexecution.xmof.animation.addon.ModelAnimator;
import org.modelexecution.xmof.configuration.ConfigurationObjectMap;
import org.modelexecution.xmof.gemoc.engine.XMOFExecutionEngine;
import org.modelexecution.xmof.gemoc.engine.XMOFExecutionModelContext;
import org.modelexecution.xmof.gemoc.engine.ui.Activator;
import org.modelexecution.xmof.gemoc.engine.ui.commons.RunConfiguration;
import org.modelexecution.xmof.gemoc.engine.ui.debug.XMOFMutableFieldExtractor;

import fr.inria.diverse.commons.messagingsystem.api.MessagingSystem;
import fr.inria.diverse.trace.commons.model.trace.MSEOccurrence;
import fr.inria.diverse.trace.gemoc.api.IMultiDimensionalTraceAddon;
import fr.obeo.dsl.debug.ide.IDSLDebugger;
import fr.obeo.dsl.debug.ide.event.DSLDebugEventDispatcher;

public class Launcher extends AbstractSequentialGemocLauncher {

	public final static String TYPE_ID = Activator.PLUGIN_ID + ".launcher";

	@Override
	protected IBasicExecutionEngine createExecutionEngine(
			org.gemoc.executionframework.engine.ui.commons.RunConfiguration runConfiguration,
			ExecutionMode executionMode) throws CoreException, EngineContextException {
		IBasicExecutionEngine executionEngine = new XMOFExecutionEngine();
		ModelExecutionContext executioncontext = new XMOFExecutionModelContext(runConfiguration, executionMode);
		executioncontext.initializeResourceModel();
		executionEngine.initialize(executioncontext);
		initializeAnimationAddon(executionEngine, runConfiguration);
		return executionEngine;
	}
	
	private void initializeAnimationAddon(IBasicExecutionEngine executionEngine,
			org.gemoc.executionframework.engine.ui.commons.RunConfiguration runConfiguration) {
		if (executionEngine instanceof XMOFExecutionEngine) {
			XMOFExecutionEngine xmofEngine = (XMOFExecutionEngine) executionEngine;
			Set<IEngineAddon> addons = xmofEngine.getAddonsTypedBy(IEngineAddon.class);
			for (IEngineAddon addon : addons) {
				if (addon instanceof ModelAnimator) {
					initializeAnimationAddon((ModelAnimator) addon, xmofEngine);
				}
			}

		}
		
	}


	private void initializeAnimationAddon(ModelAnimator addon, XMOFExecutionEngine xmofEngine) {
		addon.initialize(xmofEngine.getModel(), xmofEngine.getLoader().getXMOFModelResource());

	}

	@Override
	protected IDSLDebugger getDebugger(ILaunchConfiguration configuration, DSLDebugEventDispatcher dispatcher,
			EObject firstInstruction, IProgressMonitor monitor) {
		AbstractGemocDebugger debugger;
		Set<IMultiDimensionalTraceAddon> traceAddons = _executionEngine
				.getAddonsTypedBy(IMultiDimensionalTraceAddon.class);
		if (traceAddons.isEmpty()) {
			debugger = new GenericSequentialModelDebugger(dispatcher, (ISequentialExecutionEngine) _executionEngine);
		} else {
			debugger = null;
			// res = new OmniscientGenericSequentialModelDebugger(dispatcher,
			// (ISequentialExecutionEngine) executionEngine,
			// traceAddons.iterator().next());
		}

		ConfigurationObjectMap configurationMap = ((XMOFExecutionEngine)_executionEngine).getConfigurationMap();
		debugger.setMutableFieldExtractors(Arrays.asList(new XMOFMutableFieldExtractor(configurationMap)));
		
		// If in the launch configuration it is asked to pause at the start,
		// we add this dummy break
		try {
			if (configuration.getAttribute(RunConfiguration.LAUNCH_BREAK_START, false)) {
				debugger.addPredicateBreak(new BiPredicate<IBasicExecutionEngine, MSEOccurrence>() {
					@Override
					public boolean test(IBasicExecutionEngine t, MSEOccurrence u) {
						return true;
					}
				});
			}
		} catch (CoreException e) {
			Activator.error(e.getMessage(), e);
		}

		_executionEngine.getExecutionContext().getExecutionPlatform().addEngineAddon(debugger);
		return debugger;
	}
	
	@Override
	protected String getLaunchConfigurationTypeID() {
		return TYPE_ID;
	}

	@Override
	protected String getDebugJobName(ILaunchConfiguration arg0, EObject arg1) {
		return "Gemoc debug job";
	}

	@Override
	protected String getModelIdentifier() {
		if (_executionEngine instanceof XMOFExecutionEngine)
			return Activator.PLUGIN_ID + ".debugModel";
		else
			return MODEL_ID;
	}

	@Override
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	protected void prepareViews() {
		ViewHelper.retrieveView(EnginesStatusView.ID);
	}

	@Override
	protected org.gemoc.executionframework.engine.ui.commons.RunConfiguration parseLaunchConfiguration(
			ILaunchConfiguration configuration) throws CoreException {
		return new RunConfiguration(configuration);
	}

	@Override
	protected void error(String message, Exception e) {
		Activator.error(message, e);
	}

	@Override
	protected MessagingSystem getMessagingSystem() {
		return Activator.getDefault().getMessaggingSystem();
	}

	@Override
	protected void setDefaultsLaunchConfiguration(ILaunchConfigurationWorkingCopy configuration) {

	}

}
