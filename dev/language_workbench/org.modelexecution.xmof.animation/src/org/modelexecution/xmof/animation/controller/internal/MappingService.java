package org.modelexecution.xmof.animation.controller.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEOperation;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.GraphitiDiagramDecorator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

import fUML.Syntax.Actions.BasicActions.CallOperationAction;
import fUML.Syntax.Activities.ExtraStructuredActivities.ExpansionRegion;

public class MappingService {
	private static final String MSE_PREFIX = "MSE";
	private static final String MAIN = "main";
	private static final String ACTION_SUFFIX = "Action";
	private static final String NODE_SUFFIX = "Node";
	private static final String EXPANSION_SUFFIX = ExpansionRegion.class
			.getSimpleName();
	private static final String CALL_OPERATION = CallOperationAction.class
			.getSimpleName();
	private XMOFBasedModel model;
	private Set<String> allowedEObjects;
	private Map<String, Activity> activityMap;
	private Match lastMatchAttempt;

	public MappingService(XMOFBasedModel model) {
		this.model = model;
		prepareMap();
		obtainAllowedEObjects();
	}

	public Activity getActivityByName(String name) {
		return activityMap.get(name);
	}

	private void obtainAllowedEObjects() {
		allowedEObjects = new HashSet<>();
		for (EObject modelElement : model.getModelElements()) {
			allowedEObjects.add(modelElement.getClass().getSimpleName());
		}
	}

	private Map<String, EObject> obtainDistinctModelElements() {
		Map<String, EObject> elementMap = new HashMap<>();
		for (EObject element : model.getModelElements()) {
			if (!elementMap.containsKey(element.getClass().getName())) {
				elementMap.put(element.getClass().getName(), element);
			}
		}
		return elementMap;

	}

	private void prepareMap() {
		activityMap = new HashMap<>();

		for (Activity activity : obtainActivities(obtainDistinctModelElements()
				.values())) {
			String name = activity.getName();
			activityMap.put(name, activity);

		}

		return;

	}

	public Set<String> getActivityNames() {
		return activityMap.keySet();
	}

	private Set<Activity> obtainActivities(Collection<EObject> modelElements) {
		Set<Activity> activities = new HashSet<>();
		for (EObject element : modelElements) {
			activities.addAll(obtainActivities(element));
		}
		return activities;
	}

	private Set<Activity> obtainActivities(EObject modelElement) {
		Set<Activity> activities = new HashSet<>();
		EClass eClass = modelElement.eClass();
		for (EOperation eOperation : eClass.getEOperations()) {
			if (eOperation instanceof BehavioredEOperation) {
				activities.add(getActivity((BehavioredEOperation) eOperation));
			}

		}
		return activities;

	}

	private Activity getActivity(BehavioredEOperation eOperation) {
		if (!eOperation.getMethod().isEmpty())
			return (Activity) eOperation.getMethod().get(0);
		return null;
	}


	private void tryToFindMatch(String name) {
		String[] args = name.split(":");
		switch (args.length) {
		case 1:
			matchName(args[0]);
			return;
		case 2:
			matchNameAndType(args[0], args[1]);
			return;
		default:

		}

	}

	private void matchNameAndType(String name, String type) {
		if (!type.isEmpty()) {
			if (type.endsWith(MappingService.ACTION_SUFFIX)) {
				if (type.equals(MappingService.CALL_OPERATION)) {
					lastMatchAttempt.setType(XMOFType.CALLOPERATION);
				} else {
					lastMatchAttempt.setType(XMOFType.ACTIVITYNODE);
				}
			} else if (type.endsWith(MappingService.NODE_SUFFIX)) {
				lastMatchAttempt.setType(XMOFType.CONTROLNODE);
			} else if (type.endsWith(MappingService.EXPANSION_SUFFIX)) {
				lastMatchAttempt.setType(XMOFType.EXPANSIONREGION);
			}
			lastMatchAttempt.setXmofElementName(name);
		}

	}

	private void matchName(String name) {
		if (name.equals(MappingService.MAIN)) {
			lastMatchAttempt.setType(XMOFType.MAIN);
			lastMatchAttempt.setXmofElementName(MappingService.MAIN);
		} else if (getActivityNames().contains(name)) {
			lastMatchAttempt.setType(XMOFType.ACTITVITY);
			lastMatchAttempt.setXmofElementName(name);
		}
	}

	private boolean hasCorrectPrefix(String[] args) {
		if (args.length == 3) {
			return MappingService.MSE_PREFIX.equals(args[0])
					&& allowedEObjects.contains(args[1]);
		}
		return false;
	}

	public XMOFBasedModel getModel() {
		return model;
	}


	public Match matchDebugEvent(String debugevent) {
		lastMatchAttempt = new Match(debugevent);
		String[] prefixArgs = debugevent.split("_");
		if (hasCorrectPrefix(prefixArgs)) {
			tryToFindMatch(prefixArgs[2]);
		}
		return lastMatchAttempt;
	}

}
