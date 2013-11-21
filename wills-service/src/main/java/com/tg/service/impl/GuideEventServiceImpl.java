package com.tg.service.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tg.constant.EventConstant;
import com.tg.constant.MessageConstant;
import com.tg.constant.RedisKeyConstant;
import com.tg.constant.ResultConstant;
import com.tg.dao.BroadcastEventDAO;
import com.tg.dao.GuideEventDAO;
import com.tg.dao.InviteEventDAO;
import com.tg.model.GuideEvent;
import com.tg.model.InviteEvent;
import com.tg.model.UserInfo;
import com.tg.service.GuideEventService;
import com.tg.service.MessageService;
import com.tg.service.UserService;
import com.tg.util.ListStrUtil;
import com.wills.redis.client.RedisClient;

public class GuideEventServiceImpl implements GuideEventService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GuideEventServiceImpl.class);

	private GuideEventDAO guideEventDAO;
	
	private InviteEventDAO inviteEventDAO;
	
	private BroadcastEventDAO broadcastEventDAO;
	
	private UserService userService;
	
	private MessageService messageService;
	
	private RedisClient unreadMsgClient=new RedisClient(RedisKeyConstant.USER_UNREADMSG_COUNT);
	
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
		//发送消息
		messageService.sendMessage(guideId, userId, MessageConstant.MSG_TYPE_ACCEPTED, null);
		logger.info("accept invite succ,guideId--eventId"+guideId+"--"+eventId);
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
		//发送消息
		messageService.sendMessage(guideId, userId, MessageConstant.MSG_TYPE_REFUSED, null);
		logger.info("refuse invite succ,guideId--eventId"+guideId+"--"+eventId);
		return ResultConstant.OP_OK;
	}

	@Override
	public List<GuideEvent> getHistoricalGuideEvents(int guideId, int start,
			int count,boolean filter) {
		// TODO Auto-generated method stub
		List<GuideEvent> guideEvents=null;
		if(filter){
			guideEvents=guideEventDAO.getFinishedGuideEvents(guideId, start, count);
		}else{
			guideEvents=guideEventDAO.getHistoricalGuideEvents(guideId, start, count);
			//将未读消息计数置0
			unreadMsgClient.set(String.valueOf(guideId), 0);
		}
		renderUserInfos(guideEvents);
		return guideEvents;
	}

	@Override
	public GuideEvent getOneGuideEvent(int guideId, long eventId) {
		// TODO Auto-generated method stub
		GuideEvent guideEvent=guideEventDAO.getOneHistoricalGuideEvent(guideId, eventId);
		renderUserInfo(guideEvent);
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
	
	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	private void renderUserInfo(GuideEvent guideEvent){
		if(guideEvent==null){
			return ;
		}
		UserInfo userInfo=userService.getUserInfo(guideEvent.getUserId());
		if(userInfo==null){
			return;
		}
		guideEvent.setUserName(userInfo.getUserName());
		guideEvent.setUserHeadUrl(userInfo.getHeadUrl());
		guideEvent.setMobile(userInfo.getMobile());
	}
	
	private void renderUserInfos(List<GuideEvent> guideEvents){
		if(guideEvents==null||guideEvents.size()==0)
			return ;
		for(GuideEvent guideEvent:guideEvents){
			renderUserInfo(guideEvent);
		}
	}
	
}
