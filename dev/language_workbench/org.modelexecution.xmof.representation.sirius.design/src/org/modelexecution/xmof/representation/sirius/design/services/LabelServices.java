package org.modelexecution.xmof.representation.sirius.design.services;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Action;
import org.modelexecution.xmof.Syntax.Actions.BasicActions.Pin;
import org.modelexecution.xmof.Syntax.Actions.IntermediateActions.LinkAction;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class LabelServices {
	public static final LabelServices INSTANCE = new LabelServices();

	private LabelServices() {

	}

	public String computeDefaultName(final EObject element) {

		Predicate<EObject> predicate = null;
		String name = element.getClass().getSimpleName();
		name = name.substring(0, name.indexOf("Impl"));
		predicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input.getClass().getName().equals(element.getClass().getName());
			}
		};
		final List<EObject> existingElements = Lists
				.newArrayList(Iterables.filter(element.eContainer().eContents(), predicate));

		return name + existingElements.size();
	}

	public String computeLabel(final Pin pin){
		if (pin.eContainer() instanceof LinkAction){
			return computeInputLabel((LinkAction) pin.eContainer() );
		}
		return "";
	}
	
	private String computeInputLabel(LinkAction action){
		String label="inputValue";
		if (action.getInputValue()!=null){
			label+=action.getInputValue().size();
		}
		return label;
	}
}
