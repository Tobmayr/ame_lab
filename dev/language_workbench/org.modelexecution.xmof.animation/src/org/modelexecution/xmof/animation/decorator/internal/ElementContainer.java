package org.modelexecution.xmof.animation.decorator.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ElementContainer {

	private String activeNode;
	private EdgeId activeEdge;
	private Set<String> traversedNodes = new HashSet<>();
	private Set<EdgeId> traversedEdges = new HashSet<>();

	public void setActiveNode(String nodeName) {
		if (nodeName != null) {
			activeNode = nodeName;
			traversedNodes.remove(activeNode);
		}

	}

	public void setActiveEdge(EdgeId edge) {
		if (edge != null) {
			activeEdge = edge;
			traversedEdges.remove(activeEdge);
		}

	}

	public void addTraversedEdge(EdgeId edge) {
		if (edge != null) {
			traversedEdges.add(edge);
		}
	}

	public void addTraversedNode(String node) {
		if (node != null) {
			traversedNodes.add(node);
		}
	}

	public String getActiveNode() {
		return activeNode;
	}

	public EdgeId getActiveEdge() {
		return activeEdge;
	}

	public Set<String> getTraversedNodes() {
		return Collections.unmodifiableSet(traversedNodes);

	}

	public Set<EdgeId> getTraversedEdges() {
		return Collections.unmodifiableSet(traversedEdges);
	}

}
