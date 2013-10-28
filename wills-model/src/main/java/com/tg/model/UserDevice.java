package com.tg.model;

import java.io.Serializable;

public class UserDevice implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9119251888937454393L;

	private int userId;
	
	private String deviceToken;
	
	private int deviceType;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	
	
}
