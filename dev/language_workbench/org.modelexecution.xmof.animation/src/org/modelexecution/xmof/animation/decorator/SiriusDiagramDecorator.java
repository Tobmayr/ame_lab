/*******************************************************************************
 * Copyright (c) 2016
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.modelexecution.xmof.animation.decorator;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.animation.controller.SiriusAnimationController;

/**
 * Specific DiagramDecorator for Sirius
 * 
 * @author Matthias Hoellthaler (e1025709@student.tuwien.ac.at)
 * @author Tobias Ortmayr (e1026279@student.tuwien.ac.at)
 * @see org.modelexecution.xmof.animation.decorator.DiagramDecorator
 * @version 1.0
 *
 */
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
