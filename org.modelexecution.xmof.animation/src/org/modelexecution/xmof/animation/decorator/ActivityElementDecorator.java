package org.modelexecution.xmof.animation.decorator;

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
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;

public class ActivityElementDecorator  {

	private KernelEditor editor;
	public ActivityElementDecorator(KernelEditor editor){
		this.editor=editor;
		
	}
	public void decorateActivityNode(String nodeName){
		DiagramEditor activeDiagramEditor = getActiveDiagramEditor();
		if (activeDiagramEditor!=null){
			Activity activity = getActivity(activeDiagramEditor);
			decorate(activeDiagramEditor, activity,nodeName);
		}
		
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

	private DiagramEditor getActiveDiagramEditor() {
		DiagramEditor diagramEditor = null;
		if (editor !=null){
			Object selectedPage = editor.getSelectedPage();
			if (selectedPage instanceof DiagramEditor) {
				diagramEditor = (DiagramEditor) selectedPage;
			}
		}
		return diagramEditor;
	}

	

	private Diagram getDiagram(DiagramEditor editor) {
		Diagram diagram = editor.getDiagramTypeProvider().getDiagram();
		return diagram;
	}

	
	private void decorate(DiagramEditor editor, Activity activity, String nodeName) {
		for (ActivityNode node : activity.getNode()) {
			String name = node.getName();
			if(name==null) continue;
			if (name.equals(nodeName.trim())){
				refreshDecoration(editor, node);
				return;
			}
			
		}
	}

	private void refreshDecoration(DiagramEditor editor, ActivityNode node) {
		DecoratorService.addDecoratedElement(node);
		DiagramBehavior diagramBehavior = getDiagramBehavior(editor);
		Diagram diagram = getDiagram(editor);
		List<PictogramElement> pictogramElements = Graphiti.getLinkService()
				.getPictogramElements(diagram, node);
		for (PictogramElement pictogramElement : pictogramElements) {
			diagramBehavior.refreshRenderingDecorators(pictogramElement);
		}
	}

	private DiagramBehavior getDiagramBehavior(DiagramEditor editor) {
		DiagramBehavior diagramBehavior = editor.getDiagramBehavior();
		return diagramBehavior;
	}

	

	

}
