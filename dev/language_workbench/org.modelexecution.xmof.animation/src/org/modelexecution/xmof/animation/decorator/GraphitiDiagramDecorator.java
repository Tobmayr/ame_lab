package org.modelexecution.xmof.animation.decorator;

import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.service.DecorationType;
import org.modelexecution.xmof.animation.decorator.service.GraphitiDecoratorService;

public class GraphitiDiagramDecorator extends DiagramDecorator {

	private KernelEditor kernelEditor;
	private DiagramEditor diagramEditor;

	public GraphitiDiagramDecorator(Activity activity, KernelEditor kernelEditor) {
		super(activity);
		this.kernelEditor = kernelEditor;
	}

	@Override
	public void resetDecorations() {
		previouslyActiveNode = null;
		GraphitiDecoratorService.clear();
		for (ActivityNode node : activityNodeMap.values()) {
			refreshDecoration(node, DecorationType.UNDECORATED_NODE);
		}

	}

	@Override
	public void initializeMaps() {
		diagramEditor = getActiveDiagramEditor();
		activityNodeMap = new HashMap<String, ActivityNode>();
		if (diagramEditor != null) {
			activity = getActivity(diagramEditor);
			super.initializeMaps();

		}
	}

	@Override
	protected void decorateElement(EObject object, DecorationType type) {

		refreshDecoration(object, type);

	}

	private void refreshDecoration(EObject element, DecorationType type) {

		GraphitiDecoratorService.setDecoratedElement(element, type);

		DiagramBehavior diagramBehavior = getDiagramBehavior(diagramEditor);
		Diagram diagram = getDiagram(diagramEditor);
		List<PictogramElement> pictogramElements = Graphiti.getLinkService().getPictogramElements(diagram, element);
		for (PictogramElement pictogramElement : pictogramElements) {
			diagramBehavior.refreshRenderingDecorators(pictogramElement);
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
