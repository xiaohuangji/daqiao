package com.tg.service;

import java.util.List;

import com.tg.model.Message;

public interface MessageService {

	/**
	 * 获取指定用户的id
	 * @param userId
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<Message> getMessage(int userId,int start,int rows);
	
	/**
	 * 发送消息
	 * @param fromId
	 * @param toId
	 * @param payload 根据type不同，有效负载也不同。可能是String,也可能是long等
	 * @param type
	 * @return
	 */
	public int sendMessage(int fromId,int toId,int type ,Object payload);
}
