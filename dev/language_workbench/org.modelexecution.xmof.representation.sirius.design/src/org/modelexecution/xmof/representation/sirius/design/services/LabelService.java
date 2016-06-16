package org.modelexecution.xmof.representation.sirius.design.services;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;

public class LabelService {
	
	public static String generateLabel(Activity activity){
		String label= activity.getName();
		char first = Character.toUpperCase(label.charAt(0));
		return first+label.substring(1);
		
	}
	
	public static String generateLabel(ActivityNode node){
		return node.eClass().getName();
	}

}
