package com.capeelectric.model;

import java.util.List;

public class FqaModelSideNav {

	private String node;

	private List<ChildNode> childNode;

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public List<ChildNode> getChildNode() {
		return childNode;
	}

	public void setChildNode(List<ChildNode> childNode) {
		this.childNode = childNode;
	}

}
