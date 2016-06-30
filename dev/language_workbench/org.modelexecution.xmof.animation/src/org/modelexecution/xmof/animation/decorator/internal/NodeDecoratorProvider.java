package org.modelexecution.xmof.animation.decorator.internal;

import org.eclipse.draw2d.Graphics;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.BorderDecorator;
import org.eclipse.graphiti.tb.ColorDecorator;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.modelexecution.xmof.Syntax.Activities.CompleteStructuredActivities.StructuredActivityNode;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.animation.decorator.DecoratorService;
import org.modelexecution.xmof.diagram.decoration.IXMOFDecoratorProvider;

public class NodeDecoratorProvider implements IXMOFDecoratorProvider {

	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {

		for (EObject businessObject : pe.getLink().getBusinessObjects()) {
			if (businessObject instanceof ActivityNode) {
				return getDecorators((ActivityNode) businessObject);
			} else if (businessObject instanceof ActivityEdge) {
				return getDecorators((ActivityEdge) businessObject);
			}
		}
		return new IDecorator[0];
	}

	private IDecorator[] getDecorators(ActivityEdge edge) {
		if (DecoratorService.isTraversedEdge(edge)) {
			return new IDecorator[] {
					new ColorDecorator(new ColorConstant(51, 205, 153), new ColorConstant(51, 205, 153)) };
		} else if (DecoratorService.isActiveEdge(edge)) {
			return new IDecorator[] {
					new ColorDecorator(new ColorConstant(255, 102, 102), new ColorConstant(255, 102, 102)) };
		}
		return new IDecorator[0];
	}

	private IDecorator[] getDecorators(ActivityNode node) {
		if (DecoratorService.isTraversedNode(node)) {
			if (node instanceof StructuredActivityNode) {
				return new IDecorator[] {
						new BorderDecorator(new ColorConstant(51, 205, 153), 1, Graphics.LINE_DASHDOTDOT) };
			}
			return new IDecorator[] { new ColorDecorator(IColorConstant.GREEN, new ColorConstant(51, 205, 153)) };
		} else if (DecoratorService.isActiveNode(node)) {
			if (node instanceof StructuredActivityNode) {
				return new IDecorator[] {
						new BorderDecorator(new ColorConstant(255, 102, 102), 1, Graphics.LINE_DASHDOTDOT) };
			}
			return new IDecorator[] { new ColorDecorator(IColorConstant.RED, new ColorConstant(255, 102, 102)) };
		}
		return new IDecorator[0];
	}

}
