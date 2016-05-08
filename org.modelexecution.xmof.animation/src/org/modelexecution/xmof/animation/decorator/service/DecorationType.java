package org.modelexecution.xmof.animation.decorator.service;

import org.eclipse.draw2d.Graphics;
import org.eclipse.graphiti.tb.BorderDecorator;
import org.eclipse.graphiti.tb.ColorDecorator;
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.util.IColorConstant;

public enum DecorationType {

	NODE(new IDecorator[] { new ColorDecorator(IColorConstant.YELLOW,
			IColorConstant.RED) }, new IDecorator[] { new ColorDecorator(
			IColorConstant.YELLOW, IColorConstant.GREEN) }),

	EXPANSION_REGION(new IDecorator[] { new BorderDecorator(IColorConstant.RED,
			1, Graphics.LINE_DASHDOTDOT) },
			new IDecorator[] { new BorderDecorator(IColorConstant.YELLOW, 1,
					Graphics.LINE_DASHDOTDOT) });

	private IDecorator[] active;
	private IDecorator[] traversed;

	DecorationType(IDecorator[] active, IDecorator[] traversed) {
		this.active = active;
		this.traversed = traversed;

	}

	public IDecorator[] getDecorators(boolean active) {
		if (active)
			return this.active;
		else
			return traversed;
	}

}
