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
	 * @filter 过滤标记。true表示只获取预约成功的记录，供其他人看。false表示获取全部，供自已查看。
	 * @param guideId
	 * @return
	 */
	public List<GuideEvent> getHistoricalGuideEvents(int guideId,int start,int count,boolean filter);
	
	/**
	 * 获取单条导游记录
	 * @param guideId
	 * @param eventId
	 * @return
	 */
	public GuideEvent getOneGuideEvent(int guideId,long eventId);
	
}
