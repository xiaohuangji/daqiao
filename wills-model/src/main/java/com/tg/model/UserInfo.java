package com.tg.model;

import java.io.Serializable;

public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6042857163022937170L;

	/**
	 * 系统中唯一
	 */
	protected int userId;
	
	/**
	 * 用户名
	 */
	protected String userName;
	
	/**
	 * 手机号
	 */
	protected String mobile;
	
	/**
	 * 性别
	 */
	protected int gender;
	
	/**
	 * 类型，标识是游客还是导游
	 */
	protected int userType;
	
	/**
	 * 头像URL
	 */
	protected String headUrl;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
}
