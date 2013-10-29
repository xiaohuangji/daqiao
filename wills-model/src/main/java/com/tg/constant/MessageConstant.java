package com.tg.constant;

public class MessageConstant {

	/**
	 * 预约消息
	 * user->guide
	 */
	public static final int MSG_TYPE_INVITE=1;
	
	/**
	 * 广播消息
	 * user->guide
	 */
	public static final int MSG_TYPE_BROADCAST=2;
	
	/**
	 * 预约被接受
	 * guide->user
	 */
	public static final int MSG_TYPE_ACCEPTED=3;
	
	/**
	 * 预约被拒绝
	 * guide->user
	 */
	public static final int MSG_TYPE_REFUSED=4;
	
	/**
	 * 被评价
	 * user->guide
	 */
	public static final int MSG_TYPE_EVALUATED=5;
	
	/**
	 * 发起聊天
	 * user->guide || guide->user
	 */
	public static final int MSG_TYPE_CHAT=6;
	
	
}
