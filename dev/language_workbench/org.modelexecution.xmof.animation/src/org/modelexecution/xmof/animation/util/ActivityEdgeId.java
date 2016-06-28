package org.modelexecution.xmof.animation.util;

public class ActivityEdgeId {

	private String sourceNodeId;
	private String targetNodeId;

	public ActivityEdgeId(String sourceNodeName, String targetNodeName) {
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
		ActivityEdgeId other = (ActivityEdgeId) obj;
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
		return "("+sourceNodeId + "|" + targetNodeId+")";
	}

}
