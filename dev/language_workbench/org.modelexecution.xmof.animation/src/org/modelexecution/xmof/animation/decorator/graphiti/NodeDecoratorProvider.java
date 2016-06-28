package org.modelexecution.xmof.animation.decorator.graphiti;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IDecorator;
import org.modelexecution.xmof.diagram.decoration.IXMOFDecoratorProvider;

public class NodeDecoratorProvider implements IXMOFDecoratorProvider {

	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {

		for (EObject businessObject : pe.getLink().getBusinessObjects()) {
			if (GraphitiDecoratorService.equalsDecoratedElement(businessObject)) {
				return GraphitiDecoratorService.getDecorators();
			}

		}
		return new IDecorator[0];
	}

}
