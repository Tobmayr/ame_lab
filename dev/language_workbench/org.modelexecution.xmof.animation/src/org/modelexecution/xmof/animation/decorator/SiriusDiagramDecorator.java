package org.modelexecution.xmof.animation.decorator;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.SiriusAnimationController;
import org.modelexecution.xmof.animation.mapping.XMOFType;

public class SiriusDiagramDecorator extends DiagramDecorator {

	private SiriusAnimationController controller;

	public SiriusDiagramDecorator(Activity activity, SiriusAnimationController controller) {
		super(activity);
		this.controller = controller;
	}

	
	@Override
	public void resetDecorations() {
		super.resetDecorations();
	

	}


	@Override
	protected void refreshDiagram() {
		controller.refresh();
		
	}

}
