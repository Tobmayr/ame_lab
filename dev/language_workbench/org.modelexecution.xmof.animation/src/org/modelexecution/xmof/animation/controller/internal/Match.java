package org.modelexecution.xmof.animation.controller.internal;

public class Match {

	private XMOFType type = XMOFType.UNKOWN;
	private String mseName;
	private String xmofElementName;
	private String callerObjectName;



	public Match() {
		super();
	}

	public Match(String mseName) {
		super();
		this.mseName = mseName;
	}

	public String getMseName() {
		return mseName;
	}

	public void setMseName(String mseOccurenceName) {
		this.mseName = mseOccurenceName;
	}

	public String getXmofElementName() {
		return xmofElementName;
	}

	public void setXmofElementName(String xmofElementName) {
		this.xmofElementName = xmofElementName.trim();
	}

	public XMOFType getType() {
		return type;
	}

	public void setType(XMOFType type) {
		this.type = type;
	}
	
	public String getCallerObjectName() {
		return callerObjectName;
	}

	public void setCallerObjectName(String invokerObjectName) {
		this.callerObjectName = invokerObjectName;
	}

}
