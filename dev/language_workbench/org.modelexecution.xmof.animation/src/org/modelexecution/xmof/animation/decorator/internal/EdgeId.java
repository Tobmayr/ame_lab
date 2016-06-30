package org.modelexecution.xmof.animation.decorator.internal;

import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.ActivityEdge;

public class EdgeId {

	private String sourceNodeId;
	private String targetNodeId;

	public EdgeId(ActivityEdge edge) {
		if (edge.getSource() != null) {
			sourceNodeId = edge.getSource().getName();
		}
		if (edge.getTarget() != null) {
			targetNodeId = edge.getTarget().getName();
		}
	}

	public EdgeId(String sourceNodeName, String targetNodeName) {
		super();

		this.sourceNodeId = sourceNodeName;
		this.targetNodeId = targetNodeName;

	}

	public String getSourceNodeName() {
		return sourceNodeId;
	}

	public String getTargetNodeName() {
		return targetNodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceNodeId == null) ? 0 : sourceNodeId.hashCode());
		result = prime * result + ((targetNodeId == null) ? 0 : targetNodeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeId other = (EdgeId) obj;
		if (sourceNodeId == null) {
			if (other.sourceNodeId != null)
				return false;
		} else if (!sourceNodeId.equals(other.sourceNodeId))
			return false;
		if (targetNodeId == null) {
			if (other.targetNodeId != null)
				return false;
		} else if (!targetNodeId.equals(other.targetNodeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "_" + "(" + sourceNodeId + "|" + targetNodeId + ")";
	}

}
