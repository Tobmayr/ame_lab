package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.modelexecution.xmof.animation.controller.internal.Match;
import org.modelexecution.xmof.animation.controller.internal.XMOFType;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.decorator.service.GraphitiDecoratorService;

public class GraphitiDiagramDecorator extends DiagramDecorator{

	
	private KernelEditor kernelEditor;
	private DiagramEditor diagramEditor;
	private Map<String, ActivityNode> activityNodeMap;
	private ActivityNode previouslyActiveNode;
	private DecorationType previouslyDecorationType;


	public GraphitiDiagramDecorator(String activityName, KernelEditor kernelEditor) {
		super(activityName);
		this.kernelEditor=kernelEditor;
	}


	public boolean decorateActivityNode(Match match) {
		if (activityNodeMap == null) {
			intializeActivityNodeMap();
		}
		if (isActivityFinished()) {
			resetDecorations();
			setActivityFinished(false);
		}
		ActivityNode activeNode = activityNodeMap.get(match.getXmofElementName().trim());
		if (previouslyActiveNode != null) {
			refreshDecoration(previouslyActiveNode,
					previouslyDecorationType.getDecorators(false));
		}
		if (activeNode != null) {
			DecorationType type= convertToDecorationType(match.getType());
			refreshDecoration(activeNode, type.getDecorators(true));
			previouslyActiveNode = activeNode;
			previouslyDecorationType = type;
			return true;

		}

		return false;
	}

	private DecorationType convertToDecorationType(XMOFType type) {
		switch (type) {
		case EXPANSIONREGION:
			return DecorationType.EXPANSION_REGION;
		default:
			return DecorationType.NODE;
		}
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


	

}
