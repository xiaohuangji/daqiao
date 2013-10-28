package com.tg.model;

import java.io.Serializable;

public class MsgExt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7280059876460910456L;

	
	private String fromName;
	
	private String fromHeadUrl;

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromHeadUrl() {
		return fromHeadUrl;
	}

	public void setFromHeadUrl(String fromHeadUrl) {
		this.fromHeadUrl = fromHeadUrl;
	}

	
	
}
