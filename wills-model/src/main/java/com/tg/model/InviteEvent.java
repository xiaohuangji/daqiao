package com.tg.model;

import java.io.Serializable;

public class InviteEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8022456083407945600L;

	/**
	 * 事件id
	 */
	private long eventId;
	
	/**
	 * 游客id
	 */
	private int userId;
	
	/**
	 * 导游id
	 */
	private int guideId;
	
	/**
	 * 事件类型，标识是单点预约还是广播
	 */
	private int eventType;
	
	/**
	 * 事件状态，标识该事件预约等待中，被接收，被拒绝等
	 */
	private int eventStatus;
	
	/**
	 * 开始时间
	 */
	private long startTime;
	
	/**
	 * 结束时间
	 */
	private long endTime;
	
	/**
	 * 目的地
	 */
	private String scenic;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 满意度
	 */
	private int satisfaction;

	/**
	 * guide名字。只在下发时填充此字段
	 */
	private String guideName;

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

	public int getGuideId() {
		return guideId;
	}

	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	
}
