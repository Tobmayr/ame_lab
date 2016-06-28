package org.modelexecution.xmof.animation.decorator.sirius;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.controller.SiriusAnimationController;
import org.modelexecution.xmof.animation.decorator.DecorationType;
import org.modelexecution.xmof.animation.decorator.DiagramDecorator;

public class SiriusDiagramDecorator extends DiagramDecorator {

	private SiriusAnimationController controller;

	public SiriusDiagramDecorator(Activity activity, SiriusAnimationController controller) {
		super(activity);
		this.controller = controller;
	}

	@Override
	protected void decorateElement(EObject element, DecorationType type) {
		switch (type) {
		case NODE_ACTIVE:
		case STRUCTURED_NODE_ACTIVE:
			SiriusDecoratorService.setActiveNode(activity, (ActivityNode) element);
			break;
		case NODE_TRAVERSED:
		case STRUCTURED_NODE_TRAVERSED:
			SiriusDecoratorService.addTraversedNode(activity, (ActivityNode) element);
			break;
		case EDGE_ACTIVE:
			SiriusDecoratorService.setActiveEdge(activity, (ActivityEdge) element);
			break;
		case EDGE_TRAVERSED:
			SiriusDecoratorService.addTraversedEdge(activity, (ActivityEdge) element);
		default:
			break;
		}

	}

	@Override
	public void resetDecorations() {
		SiriusDecoratorService.clear(activity.getName());
		controller.refresh();

	}

}
