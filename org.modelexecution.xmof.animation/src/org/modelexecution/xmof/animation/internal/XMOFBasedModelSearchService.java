package org.modelexecution.xmof.animation.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.modelexecution.xmof.Syntax.Classes.Kernel.BehavioredEOperation;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class XMOFBasedModelSearchService {
	
	private Set<BehavioredEOperation> mainOperations;
	private XMOFBasedModel model;
	
	public XMOFBasedModelSearchService(XMOFBasedModel model){
		this.model=model;
		initialize();
	}
	
	private void initialize() {
		obtainMainOperations();
		
	}

	private void obtainMainOperations() {
		mainOperations= new HashSet<BehavioredEOperation>();
		for (EObject modelElement:model.getMainEClassObjects()){
			obtainMainOperation(modelElement);
		}
		
	}

	private void obtainMainOperation(EObject modelElement) {
		EClass eClass = modelElement.eClass();
		for(EOperation eOperation : eClass.getEAllOperations()) {
			if(eOperation instanceof BehavioredEOperation && eOperation.getName().equals(XMOFBasedModel.MAIN)) {
				mainOperations.add((BehavioredEOperation)eOperation);
			}
		}
		
	}

	public Set<BehavioredEOperation> getMainOperations() {
		return mainOperations;
	}

	public XMOFBasedModel getModel() {
		return model;
	}
	
	
	
	
	


	
}
	
	
