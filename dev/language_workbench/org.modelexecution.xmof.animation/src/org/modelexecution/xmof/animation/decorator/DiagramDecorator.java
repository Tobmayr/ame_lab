package org.modelexecution.xmof.animation.decorator;

import org.modelexecution.xmof.animation.controller.internal.Match;

public abstract class DiagramDecorator {
	private boolean activityFinished = false;
	private String activityName;

	public DiagramDecorator(String activityName) {
		this.activityName = activityName;
	}

	public abstract boolean decorateActivityNode(Match match);

	public boolean isActivityFinished() {
		return activityFinished;
	}

	public void setActivityFinished(boolean activityFinished) {
		this.activityFinished = activityFinished;
	}

	public String getActivityName() {
		return activityName;
	}
	
	

}
