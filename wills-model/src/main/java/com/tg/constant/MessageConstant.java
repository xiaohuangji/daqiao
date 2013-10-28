package com.tg.constant;

public class MessageConstant {

	/**
	 * 预约消息
	 * user->guide
	 */
	public static final int MSG_TYPE_INVITE=1;
	
	public static final String MSG_CON_INVITE="收到来自 %s 的预约";
	
	/**
	 * 广播消息
	 * user->guide
	 */
	public static final int MSG_TYPE_BROADCAST=2;
	
	public static final String MSG_CON_BROADCAST="收到来自 %s 的广播预约";
	
	/**
	 * 预约被接受
	 * guide->user
	 */
	public static final int MSG_TYPE_ACCEPTED=3;
	
	public static final String MSG_CON_ACCEPT="%s 接受了您的预约";
	
	/**
	 * 预约被拒绝
	 * guide->user
	 */
	public static final int MSG_TYPE_REFUSED=4;
	
	public static final String MSG_CON_REFUSED="%s 拒绝了您的邀请";
	
	/**
	 * 被评价
	 * user->guide
	 */
	public static final int MSG_TYPE_EVALUATED=5;
	
	public static final String MSG_CON_EVALUATED="收到来自 %s 的评价";
	
	/**
	 * 发起聊天
	 * user->guide || guide->user
	 */
	public static final int MSG_TYPE_CHAT=6;
	
}
