package org.modelexecution.xmof.animation.controller.internal;

public class Match {

	private XMOFType type = XMOFType.UNKOWN;
	private String mseOccurenceName;
	private String xmofElementName;

	public Match() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Match(String mseOccurenceName) {
		super();
		this.mseOccurenceName = mseOccurenceName;
	}

	public String getMseOccurenceName() {
		return mseOccurenceName;
	}

	public void setMseOccurenceName(String mseOccurenceName) {
		this.mseOccurenceName = mseOccurenceName;
	}

	public String getXmofElementName() {
		return xmofElementName;
	}

	public void setXmofElementName(String xmofElementName) {
		this.xmofElementName = xmofElementName;
	}

	public XMOFType getType() {
		return type;
	}

	public void setType(XMOFType type) {
		this.type = type;
	}

}
