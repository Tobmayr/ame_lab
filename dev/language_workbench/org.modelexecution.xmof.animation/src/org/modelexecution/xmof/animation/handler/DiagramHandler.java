package org.modelexecution.xmof.animation.handler;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;

public  abstract class DiagramHandler {
	public abstract void openOrShowDiagram(Activity activity);
	public abstract void dispose();
}
