package org.modelexecution.xmof.animation.decorator.handler;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityNode;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;
import org.modelexecution.xmof.animation.decorator.service.DecoratorService;

public class ActivityElementDecorator implements Runnable {

	private Activity currentActvity;
	private KernelEditor editor;
	public ActivityElementDecorator(KernelEditor editor){
		this.editor=editor;
		
	}
	public void decorateActivityNode(String nodeName){
		DiagramEditor activeDiagramEditor = getActiveDiagramEditor();
		if (activeDiagramEditor!=null && currentActvity!=null){
			decorate(activeDiagramEditor, currentActvity,nodeName);
		}
		
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
			if (node.getName().equals(nodeName)){
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

	public Activity getCurrentActvity() {
		return currentActvity;
	}

	public void setCurrentActvity(Activity currentActvity) {
		this.currentActvity = currentActvity;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

}
