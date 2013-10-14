package com.tg.service;
import java.util.List;

import com.tg.model.GuideEvent;

public interface GuideEventService {

	/**
	 * 导游接收邀请
	 * @param eventId
	 * @param guideId
	 * @return
	 */
	public int accept(long eventId,int guideId,int userId);
	
	/**
	 * 导游拒绝邀请
	 * @param eventId
	 * @param guideId
	 * @return
	 */
	public int refuse(long eventId,int guideId,int userId);
	
	/**
	 * 获取导游历史记录
	 * @param guideId
	 * @return
	 */
	public List<GuideEvent> getHistoricalGuideEvents(int guideId,int start,int count);
	
	/**
	 * 获取单条导游记录
	 * @param guideId
	 * @param eventId
	 * @return
	 */
	public GuideEvent getOneGuideEvent(int guideId,long eventId);
	
}
