package org.modelexecution.xmof.animation.handler;



import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;
import org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditor;


public class GraphitiDiagramHandler implements DiagramHandler,Runnable {

	private static final String KERNEL_EDITOR_ID="org.modelexecution.xmof.Syntax.Classes.Kernel.presentation.KernelEditorID";
	
	private KernelEditor activeEditor;
	private Resource modelResource;

	public GraphitiDiagramHandler(Resource modelResource) {
		this.modelResource=modelResource;
	
	}
	
	public void openOrShowDiagram(Activity activity){
		activeEditor.showDiagram(activity);
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
