package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.impl.ExpansionRegionImpl;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;

public class ActivityDiagramDecorator {

	private int counter = 0;
	private KernelEditor kernelEditor;
	private DiagramEditor diagramEditor;
	private String activityName;
	private Map<String, ActivityNode> activityNodeMap;
	private ActivityNode previouslyActiveNode;

	public ActivityDiagramDecorator(String activityName) {
		this.activityName = activityName;
	}

	public int getAndIncrementCounter() {
		counter++;
		return counter;
	}

	public String getActivityName() {
		return activityName;
	}

	public boolean decorateActivityNode(String nodeName) {
		if (activityNodeMap == null) {
			intializeActivityNodeMap();
		}

		ActivityNode activeNode = activityNodeMap.get(nodeName.trim());
		if (activeNode != null) {
			if (previouslyActiveNode != null) {
				refreshDecoration(previouslyActiveNode, false);
			}
			refreshDecoration(activeNode, true);
			previouslyActiveNode = activeNode;
			return true;

		}
		return false;
	}

	private void refreshDecoration(ActivityNode node, boolean active) {

		DecoratorService.setDecoratedElement(node, active);

		DiagramBehavior diagramBehavior = getDiagramBehavior(diagramEditor);
		Diagram diagram = getDiagram(diagramEditor);
		List<PictogramElement> pictogramElements = Graphiti.getLinkService()
				.getPictogramElements(diagram, node);
		for (PictogramElement pictogramElement : pictogramElements) {
			diagramBehavior.refreshRenderingDecorators(pictogramElement);
		}
	}

	private void intializeActivityNodeMap() {
		diagramEditor = getActiveDiagramEditor();
		activityNodeMap = new HashMap<String, ActivityNode>();
		if (diagramEditor != null) {
			Activity activity = getActivity(diagramEditor);
			int counterForUnnamedNodes = 0;
			for (ActivityNode node : activity.getNode()) {
				processActivityNode(node, counterForUnnamedNodes);

			}
		}
	}

	private void processActivityNode(ActivityNode node,
			int counterForUnnamedNodes) {
		if (node instanceof ExpansionRegionImpl) {
			getActivityNodes((ExpansionRegionImpl) node, counterForUnnamedNodes);
		} else if (node.getName() != null) {
			activityNodeMap.put(node.getName(), node);

		} else {
			counterForUnnamedNodes++;
			activityNodeMap.put(node.getClass().getSimpleName()
					+ counterForUnnamedNodes, node);
		}
	}

	private void getActivityNodes(ExpansionRegionImpl expNode,
			int counterForUnnamedNodes) {
		for (ActivityNode actNode : expNode.getNode()) {
			processActivityNode(actNode, counterForUnnamedNodes);
		}

	}

	private DiagramBehavior getDiagramBehavior(DiagramEditor editor) {
		DiagramBehavior diagramBehavior = editor.getDiagramBehavior();
		return diagramBehavior;
	}

	private DiagramEditor getActiveDiagramEditor() {
		DiagramEditor diagramEditor = null;
		if (kernelEditor != null) {
			Object selectedPage = kernelEditor.getSelectedPage();
			if (selectedPage instanceof DiagramEditor) {
				diagramEditor = (DiagramEditor) selectedPage;
			}
		}
		return diagramEditor;
	}

	private Activity getActivity(DiagramEditor editor) {
		Diagram diagram = getDiagram(editor);
		return getActivity(diagram);
	}

	private Activity getActivity(ContainerShape shape) {
		for (Shape childShape : shape.getChildren()) {
			Activity activity = getActivity(childShape);
			if (activity != null) {
				return activity;
			}
		}
		return null;
	}

	private Activity getActivity(Shape shape) {
		for (EObject businessObject : shape.getLink().getBusinessObjects()) {
			if (businessObject instanceof Activity) {
				return (Activity) businessObject;
			}
		}
		return null;
	}

	private Diagram getDiagram(DiagramEditor editor) {
		Diagram diagram = editor.getDiagramTypeProvider().getDiagram();
		return diagram;
	}

	public KernelEditor getKernelEditor() {
		return kernelEditor;
	}

	public void setKernelEditor(KernelEditor kernelEditor) {
		this.kernelEditor = kernelEditor;
	}

}
