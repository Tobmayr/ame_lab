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
import org.eclipse.graphiti.tb.IDecorator;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.modelexecution.xmof.Syntax.Activities.ExtraStructuredActivities.impl.ExpansionRegionImpl;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.decorator.service.GraphitiDecoratorService;

public class GraphitiActivityDiagramDecorator {

	
	private KernelEditor kernelEditor;
	private DiagramEditor diagramEditor;
	private String activityName;
	private Map<String, ActivityNode> activityNodeMap;
	private ActivityNode previouslyActiveNode;
	private DecorationType previouslyDecorationType;
	private boolean activityFinished = false;


	public GraphitiActivityDiagramDecorator(String activityName, KernelEditor kernelEditor) {
		this.activityName = activityName;
		this.kernelEditor=kernelEditor;
	}

	public String getActivityName() {
		return activityName;
	}

	public boolean decorateActivityNode(String nodeName, DecorationType type) {
		if (activityNodeMap == null) {
			intializeActivityNodeMap();
		}
		if (activityFinished) {
			resetDecorations();
			activityFinished = false;
		}
		ActivityNode activeNode = activityNodeMap.get(nodeName.trim());
		if (previouslyActiveNode != null) {
			refreshDecoration(previouslyActiveNode,
					previouslyDecorationType.getDecorators(false));
		}
		if (activeNode != null) {

			refreshDecoration(activeNode, type.getDecorators(true));
			previouslyActiveNode = activeNode;
			previouslyDecorationType = type;
			return true;

		}

		return false;
	}

	private void resetDecorations() {
		previouslyActiveNode=null;
		GraphitiDecoratorService.clear();
		for (ActivityNode node:activityNodeMap.values()){
			
			refreshDecoration(node, new IDecorator[0]);
		}
		
	}

	private void refreshDecoration(ActivityNode node, IDecorator[] decorators) {

		GraphitiDecoratorService.setDecoratedElement(node, decorators);

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
			for (ActivityNode node : activity.getNode()) {
				processActivityNode(node);

			}
		}
	}

	private void processActivityNode(ActivityNode node) {
		if (node.getName() != null) {
			if (node instanceof ExpansionRegionImpl) {

				getActivityNodes((ExpansionRegionImpl) node);
			}
			activityNodeMap.put(node.getName(), node);
		}
	}

	private void getActivityNodes(ExpansionRegionImpl expNode) {
		for (ActivityNode actNode : expNode.getNode()) {
			processActivityNode(actNode);
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

	public void setActivityFinished(boolean activityFinshed) {
		this.activityFinished = activityFinshed;

	}

	

}
