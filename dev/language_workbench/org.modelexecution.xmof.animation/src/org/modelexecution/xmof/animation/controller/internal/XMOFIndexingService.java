package org.modelexecution.xmof.animation.controller.internal;

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
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.GraphitiActivityDiagramDecorator;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class XMOFIndexingService {

	private XMOFBasedModel model;
	private Map<String, Activity> activityMap;
	private Map<String, GraphitiActivityDiagramDecorator> diagramDecoratorMap;

	public XMOFIndexingService(XMOFBasedModel model) {
		this.model = model;
		prepareMaps();
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

	public GraphitiActivityDiagramDecorator getDiagramDecoratorForActivity(String name) {
		return diagramDecoratorMap.get(name.trim());
	}

	public Activity getActivityByName(String name) {
		return activityMap.get(name);
	}

	private void prepareMaps() {
		activityMap = new HashMap<>();
		diagramDecoratorMap = new HashMap<>();
		for (Activity activity : obtainActivities(obtainDistinctModelElements()
				.values())) {
			String name = activity.getName();
			activityMap.put(name, activity);
			diagramDecoratorMap.put(name, new GraphitiActivityDiagramDecorator(name));
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

	public void passEditorToDiagramDecorator(KernelEditor kernelEditor) {
		for (GraphitiActivityDiagramDecorator decorator : diagramDecoratorMap.values()) {
			decorator.setKernelEditor(kernelEditor);
		}

	}

}
