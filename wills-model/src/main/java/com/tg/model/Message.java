package com.tg.model;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8948645547313472055L;

	private long id;
	
	private int fromId;
	
	private int toId;
	
	private int type;
	
	private String content;
	
	private long createTime;
	
	private MsgExt msgExt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public MsgExt getMsgExt() {
		return msgExt;
	}

	public void setMsgExt(MsgExt msgExt) {
		this.msgExt = msgExt;
	}
	
	
}
