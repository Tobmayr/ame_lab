package org.modelexecution.xmof.animation.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEOperation;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class XMOFModelProcessor {

	private XMOFBasedModel model;
	private Map<String,Activity> activityMap;
	public XMOFModelProcessor(XMOFBasedModel model) {
		this.model = model;
		prepareActivityMap();
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

	public Activity getActivityByName(String name){
		return activityMap.get(name);
	}
	private void prepareActivityMap() {
		activityMap=new HashMap<String, Activity>();
		for (Activity activity:obtainActivities(obtainDistinctModelElements().values())){
			activityMap.put(activity.getName(), activity);
		}
		
		return;

	}
	
	
	public Map<String, Activity> getActivityMap() {
		return Collections.unmodifiableMap(activityMap);
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

	

	public XMOFBasedModel getModel() {
		return model;
	}

}