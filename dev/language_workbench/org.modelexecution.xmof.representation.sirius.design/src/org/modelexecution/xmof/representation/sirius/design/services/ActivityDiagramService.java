package org.modelexecution.xmof.representation.sirius.design.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Action;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.InputPin;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Pin;
import org.modelexecution.xmof.Syntax.Actions.IntermediateActions.LinkAction;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEClass;
import org.modelexecution.xmof.Syntax.CommonBehaviors.BasicBehaviors.Behavior;

public class ActivityDiagramService {

	public String generateLabel(Activity activity) {
		String label = activity.getName();
		if (!label.isEmpty()) {
			char first = Character.toUpperCase(label.charAt(0));
			return first + label.substring(1);
		}
		return computeDefaultName(activity);

	}


	public String generateLabel(ActivityNode node) {
		if (node instanceof Pin) {
			return LabelServices.INSTANCE.computeLabel((Pin) node);
		}
		return node.eClass().getName();
	}

	public String computeDefaultName(EObject element) {
		return LabelServices.INSTANCE.computeDefaultName(element);
	}

	public Activity findParentActivity(EObject context) {
		if (context instanceof Activity) {
			return (Activity) context;
		}

		if (context.eContainer() != null) {
			return findParentActivity((EObject) context.eContainer());
		}

		return null;
	}

	public List<EObject> getAllBehaviors(EObject activity) {
		EPackage root = retrieveRoot(activity);
		List<EObject> result = new ArrayList<>();
		for (EClassifier classifier : root.getEClassifiers()) {
			if (classifier instanceof BehavioredEClass) {
				BehavioredEClass bc = (BehavioredEClass) classifier;
				for (Behavior behavior : bc.getOwnedBehavior()) {
					result.add(behavior);
				}
			}

		}
		return result;

	}

	private EPackage retrieveRoot(EObject object) {
		if (object.eContainer() == null) {
			if (object instanceof EPackage) {
				return (EPackage) object;
			}
			return null;
		}

		return retrieveRoot(object.eContainer());
	}

}
