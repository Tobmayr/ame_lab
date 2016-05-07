package org.modelexecution.xmof.animation.decorator.provider;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ColorDecorator;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.ui.actions.BuildAction;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;
import org.modelexecution.xmof.diagram.decoration.IXMOFDecoratorProvider;

public class NodeDecoratorProvider implements IXMOFDecoratorProvider {

	@Override
	public IDecorator[] getDecorators(PictogramElement pe) {

		for (EObject businessObject : pe.getLink().getBusinessObjects()) {
			if (DecoratorService.equalsDecoratedElement(businessObject)) {
				if (DecoratorService.isActiveElement()) {
					return new IDecorator[] { new ColorDecorator(
							IColorConstant.YELLOW, IColorConstant.RED) };
				}
				return new IDecorator[] { new ColorDecorator(
						IColorConstant.YELLOW, IColorConstant.GREEN) };
			}

		}
		return new IDecorator[0];
	}

}
