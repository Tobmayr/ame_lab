package org.modelexecution.xmof.animation.decorator.service;

import org.eclipse.draw2d.Graphics;
import org.eclipse.graphiti.tb.BorderDecorator;
import org.eclipse.graphiti.tb.ColorDecorator;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.util.IColorConstant;

public class GraphitiDecoratorService {

	private static Object decoratedElement;

	private static DecorationType type = DecorationType.UNDECORATED_NODE;

	public static void setDecoratedElement(Object element, DecorationType decoType) {
		decoratedElement = element;
		GraphitiDecoratorService.type = decoType;

	}

	public static IDecorator[] getDecorators() {
		switch (type) {
		case NODE_ACTIVE:
			return new IDecorator[] { new ColorDecorator(IColorConstant.YELLOW, IColorConstant.RED) };
		case NODE_TRAVERSED:
			return new IDecorator[] { new ColorDecorator(IColorConstant.YELLOW, IColorConstant.GREEN) };
		case STRUCTURED_NODE_ACTIVE:
			return new IDecorator[] { new BorderDecorator(IColorConstant.RED, 1, Graphics.LINE_DASHDOTDOT) };
		case STRUCTURED_NODE_TRAVERSED:
			return new IDecorator[] { new BorderDecorator(IColorConstant.YELLOW, 1, Graphics.LINE_DASHDOTDOT) };
		case EDGE_ACTIVE:
			return new IDecorator[] { new ColorDecorator(IColorConstant.RED, IColorConstant.RED) };
		case EDGE_TRAVERSED:
			return new IDecorator[] { new ColorDecorator(IColorConstant.GREEN, IColorConstant.GREEN) };
		default:
			return new IDecorator[0];
		}
	}

	public static boolean equalsDecoratedElement(Object element) {
		if (decoratedElement == null)
			return false;
		return decoratedElement.equals(element);
	}

	public static void clear() {
		decoratedElement = null;
		type = DecorationType.UNDECORATED_NODE;

	}

}
