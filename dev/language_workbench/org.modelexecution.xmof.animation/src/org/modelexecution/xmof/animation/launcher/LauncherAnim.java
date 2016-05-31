package org.modelexecution.xmof.animation.launcher;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.gemoc.executionframework.engine.commons.EngineContextException;
import org.gemoc.xdsmlframework.api.core.ExecutionMode;
import org.gemoc.xdsmlframework.api.core.IBasicExecutionEngine;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.modelexecution.xmof.animation.XMOFModelAnimator;
import org.modelexecution.xmof.animation.ui.Activator;
import org.modelexecution.xmof.gemoc.engine.XMOFExecutionEngine;
import org.modelexecution.xmof.gemoc.engine.ui.launcher.Launcher;

public class LauncherAnim extends Launcher {

	public final static String TYPE_ID = Activator.PLUGIN_ID + ".launcheranim";
	

	@Override
	protected IBasicExecutionEngine createExecutionEngine(
			org.gemoc.executionframework.engine.ui.commons.RunConfiguration runConfiguration,
			ExecutionMode executionMode) throws CoreException,
			EngineContextException {
		IBasicExecutionEngine executionEngine = super.createExecutionEngine(
				runConfiguration, executionMode);
		initializeAnimationAddon(executionEngine);
		return executionEngine;
	}

	private void initializeAnimationAddon(IBasicExecutionEngine executionEngine) {
		if (executionEngine instanceof XMOFExecutionEngine) {
			XMOFExecutionEngine xmofEngine = (XMOFExecutionEngine) executionEngine;
			Set<IEngineAddon> addons = xmofEngine
					.getAddonsTypedBy(IEngineAddon.class);
			for (IEngineAddon addon : addons) {
				if (addon instanceof XMOFModelAnimator) {
					initializeAnimationAddon((XMOFModelAnimator) addon, xmofEngine);
				}
			}

		}
	}

	private void initializeAnimationAddon(XMOFModelAnimator addon,
			XMOFExecutionEngine xmofEngine) {
		addon.initialize(xmofEngine.getModel(), xmofEngine.getLoader()
				.getXMOFModelResource());

	}

}
