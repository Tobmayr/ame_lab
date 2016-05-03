package org.modelexecution.xmof.animation.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.modelexecution.xmof.vm.XMOFBasedModel;

public class MatchingService {

	private static final String MSE_PREFIX = "MSE";
	private static final String MAIN = "main";
	private static final String ACTION_SUFFIX = "Action";
	private static final String NODE_SUFFIX = "Node";
	private XMOFBasedModel model;
	private Set<String> allowedEObjects;
	private Set<String> allowedActivities;
	private Match lastMatchAttempt;

	public MatchingService(XMOFBasedModel model) {
		this.model = model;
		initialize();
	}

	private void initialize() {
		obtainAllowedEObjects(model.getModelElements());

	}

	private void obtainAllowedEObjects(List<EObject> modelElements) {
		allowedEObjects = new HashSet<>();
		for (EObject modelElement : modelElements) {
			allowedEObjects.add(modelElement.getClass().getSimpleName());
		}
	}

	public Match matchMSEOccurence(String name) {
		lastMatchAttempt = new Match(name);
		String[] prefixArgs = name.split("_");
		if (hasCorrectPrefix(prefixArgs)) {
			matchToType(prefixArgs[2]);
		}
		return lastMatchAttempt;
	}

	private void matchToType(String name) {
		String[] args = name.split(":");
		switch (args.length) {
		case 1:
			match1ParameteredTypes(args[0]);
			return;
		case 2:
			match2ParameteredTypes(args[0], args[1]);
			return;
		default:

		}

	}

	private void match2ParameteredTypes(String name, String type) {
		if (!type.isEmpty()) {
			if (type.endsWith(ACTION_SUFFIX)) {
				lastMatchAttempt.setType(XMOFType.ACTIVITYNODE);
			} else if (type.endsWith(NODE_SUFFIX)) {
				lastMatchAttempt.setType(XMOFType.NODE);	
			}
			lastMatchAttempt.setXmofElementName(name);
		}

	}

	private void match1ParameteredTypes(String name) {
		if (name.equals(MatchingService.MAIN)) {
			lastMatchAttempt.setType(XMOFType.MAIN);
			lastMatchAttempt.setXmofElementName(MatchingService.MAIN);
		} else if (allowedActivities.contains(name)) {
			lastMatchAttempt.setType(XMOFType.ACTITVITY);
			lastMatchAttempt.setXmofElementName(name);
		}

	}

	private boolean hasCorrectPrefix(String[] args) {
		if (args.length == 3) {
			return MatchingService.MSE_PREFIX.equals(args[0])
					&& allowedEObjects.contains(args[1]);
		}
		return false;
	}

	public Set<String> getAllowedActivities() {
		return allowedActivities;
	}

	public void setAllowedActivities(Set<String> allowedActivities) {
		this.allowedActivities = allowedActivities;
	}

}
