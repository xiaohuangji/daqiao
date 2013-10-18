package com.tg.model;

import java.io.Serializable;

public class GuideEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8948645547313472055L;

	/**
	 * 导游id
	 */
	private int guideId;
	
	/**
	 * 预约id
	 */
	private long eventId;
	
	/**
	 * 预约者id
	 */
	private int userId;
	
	/**
	 * 状态，等待接收，已接收，已拒绝，未评价，已评价等
	 */
	private int status;	
	
	/**
	 * 满意度
	 */
	private int satisfaction;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 同inviteEvent中的eventType，标识是广播还是单点
	 */
	private int eventType;
	
	private long startTime;
	
	private long endTime;
	
	private String scenic;
	
	/**
	 * 游客名字，只在下发客户端时填充
	 */
	private String userName;
	
	private String userHeadUrl;

	public int getGuideId() {
		return guideId;
	}

	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeadUrl() {
		return userHeadUrl;
	}

	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getScenic() {
		return scenic;
	}

	public void setScenic(String scenic) {
		this.scenic = scenic;
	}
	
	
}
