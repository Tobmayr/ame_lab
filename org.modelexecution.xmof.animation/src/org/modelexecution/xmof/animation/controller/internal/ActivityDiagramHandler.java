package org.modelexecution.xmof.animation.controller.internal;



import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEOperation;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;

import fUML.Syntax.Activities.IntermediateActivities.ActivityNode;


public class ActivityDiagramHandler implements Runnable {

	private static final String KERNEL_EDITOR_ID="org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditorID";
	
	private KernelEditor activeEditor;
	private Resource modelResource;

	public ActivityDiagramHandler(Resource modelResource) {
		this.modelResource=modelResource;
	
	}
	
	public void showDiagram(Activity activity){
		activeEditor.showDiagram(activity);
	}
	public void showDiagram(BehavioredEOperation operation){
		activeEditor.showDiagram(operation);
	}

	private boolean openModel(){
		IEditorInput editorInput= getEditorInput();
		
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
	
			try {
				activeEditor = (KernelEditor)page.openEditor(editorInput, KERNEL_EDITOR_ID);
				
			
			} catch (PartInitException e) {
				e.printStackTrace();
				return false;
				
			}
			
			return true;
		

	}

	private IEditorInput getEditorInput() {
		URI modelPathURI= modelResource.getURI();
		return new URIEditorInput(modelPathURI);
	}


	@Override
	public void run() {
		openModel();
		
	}
	
	public KernelEditor getKernelEditor(){
		return activeEditor;
	}
	
	
	
}
