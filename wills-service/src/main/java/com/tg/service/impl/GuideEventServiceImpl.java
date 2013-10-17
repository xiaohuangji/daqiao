package com.tg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tg.constant.EventConstant;
import com.tg.constant.ResultConstant;
import com.tg.dao.BroadcastEventDAO;
import com.tg.dao.GuideEventDAO;
import com.tg.dao.InviteEventDAO;
import com.tg.model.GuideEvent;
import com.tg.model.InviteEvent;
import com.tg.service.GuideEventService;
import com.tg.service.UserService;
import com.tg.util.ListStrUtil;

public class GuideEventServiceImpl implements GuideEventService {

	private GuideEventDAO guideEventDAO;
	
	private InviteEventDAO inviteEventDAO;
	
	private BroadcastEventDAO broadcastEventDAO;
	
	private UserService userService;
	
	@Override
	public int accept(long eventId, int guideId,int userId) {
		// TODO Auto-generated method stub
		//更新guide库
		guideEventDAO.changeStatus(eventId, EventConstant.GE_STATUS_ACCEPT, guideId);
		//更新invite库
		inviteEventDAO.accepted(userId, eventId, guideId);
		//如果是广播，需要以下逻辑
		InviteEvent iv=inviteEventDAO.getOneInviteEvent(userId, eventId);
		if(iv.getEventType()==EventConstant.EVENT_BROADCAST){
			List<Integer> broadcastIds=ListStrUtil.strToList(broadcastEventDAO.getBroadcastIds(eventId));
			if(broadcastIds!=null){
				for(Integer id:broadcastIds){
					if(id!=guideId)
						guideEventDAO.changeStatus(eventId, EventConstant.GE_STATUS_ACCEPTBYOTHERS, id);
				}
			}
		}
		
		return ResultConstant.OP_OK;
		
	}

	@Override
	public int refuse(long eventId, int guideId,int userId) {
		// TODO Auto-generated method stub
		//更新guide库
		guideEventDAO.changeStatus(eventId, EventConstant.GE_STATUS_REFUSE, guideId);
		//如果是单点预约，处理invite库
		InviteEvent iv=inviteEventDAO.getOneInviteEvent(userId, eventId);
		if(iv.getEventType()==EventConstant.EVENT_SINGLE){
			//更新invite库
			inviteEventDAO.changeStatus(userId, eventId, EventConstant.IE_STATUS_REFUSED);
		}
			
		return ResultConstant.OP_OK;
	}

	@Override
	public List<GuideEvent> getHistoricalGuideEvents(int guideId, int start,
			int count) {
		// TODO Auto-generated method stub
		List<GuideEvent> guideEvents=guideEventDAO.getHistoricalGuideEvents(guideId, start, count);
		renderUserNames(guideEvents);
		return guideEvents;
	}

	@Override
	public GuideEvent getOneGuideEvent(int guideId, long eventId) {
		// TODO Auto-generated method stub
		GuideEvent guideEvent=guideEventDAO.getOneHistoricalGuideEvent(guideId, eventId);
		renderUserName(guideEvent);
		return guideEvent;
	}

	@Autowired
	public void setGuideEventDAO(GuideEventDAO guideEventDAO) {
		this.guideEventDAO = guideEventDAO;
	}

	@Autowired
	public void setInviteEventDAO(InviteEventDAO inviteEventDAO) {
		this.inviteEventDAO = inviteEventDAO;
	}

	@Autowired
	public void setBroadcastEventDAO(BroadcastEventDAO broadcastEventDAO) {
		this.broadcastEventDAO = broadcastEventDAO;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private void renderUserName(GuideEvent guideEvent){
		if(guideEvent==null){
			return ;
		}
		guideEvent.setUserName(userService.getUserInfo(guideEvent.getUserId()).getUserName());
	}
	
	private void renderUserNames(List<GuideEvent> guideEvents){
		if(guideEvents==null||guideEvents.size()==0)
			return ;
		for(GuideEvent guideEvent:guideEvents){
			renderUserName(guideEvent);
		}
	}
	
}
