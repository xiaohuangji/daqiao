package com.tg.service;

import java.util.List;

import com.tg.model.InviteEvent;

public interface InviteEventService {

	/**
	 * 预约
	 * @param userId
	 * @param guideId
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int invite(int userId,int guideId,String scenic,long startTime,long endTime);
	
	/**
	 * 广播预约
	 * @param userId
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int inviteAll(int userId,String scenic,long startTime,long endTime,String location,int gender);
	
	
	/**
	 * 取消预约
	 * @param eventId
	 * @param userId
	 * @return
	 */
	public int cancle(long eventId,int userId,int guideId);
	
	/**
	 * 获取用户预约记录
	 * @param userId
	 * @return
	 */
	public List<InviteEvent> getHistoricalInviteEvents(int userId,int start,int count);
	
	/**
	 * 获取某条预约信息
	 * @param userId
	 * @param eventId
	 * @return
	 */
	public InviteEvent getOneInviteEvent(int userId,long eventId);
	
	/**
	 * 打分
	 * @param eventId
	 * @param satisfaction
	 * @return
	 */
	public int setSatisfaction(long eventId,int satisfaction,int userId,int guideId);
	
	
}
