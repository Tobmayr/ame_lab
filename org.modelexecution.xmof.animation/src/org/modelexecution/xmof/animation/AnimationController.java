package org.modelexecution.xmof.animation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PlatformUI;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEOperation;
import org.modelexecution.xmof.animation.internal.XMOFBasedModelSearchService;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class AnimationController {
	

	private XMOFBasedModelSearchService modelSearcher;
	private ActivityDiagramHandler diagramHandler;
	
	public AnimationController(XMOFBasedModel model, Resource modelResource) {
		modelSearcher= new XMOFBasedModelSearchService(model);
		diagramHandler= new ActivityDiagramHandler(modelResource);
		PlatformUI.getWorkbench().getDisplay().asyncExec(diagramHandler);
		
	}


	
	public void openMainDiagram(){
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				for (BehavioredEOperation operation:modelSearcher.getMainOperations()){
					diagramHandler.showDiagram(operation);
				}
				
			}
		});
		
	}

	

	
	

}
