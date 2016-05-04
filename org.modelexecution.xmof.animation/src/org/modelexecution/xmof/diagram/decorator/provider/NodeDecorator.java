package org.modelexecution.xmof.diagram.decorator.provider;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ColorDecorator;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.util.IColorConstant;
import org.modelexecution.xmof.diagram.decoration.IXMOFDecoratorProvider;
import org.modelexecution.xmof.diagram.decorator.service.DecoratorService;

public class NodeDecorator implements IXMOFDecoratorProvider {

	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {
		for (EObject businessObject : pe.getLink().getBusinessObjects()) {
			if (DecoratorService.isdecoratedElement(businessObject)) {
				return new IDecorator[] { new ColorDecorator(
						IColorConstant.YELLOW, IColorConstant.RED) };
			}
		}
		return new IDecorator[0];
	}

}
