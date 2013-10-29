package com.tg.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.tg.constant.MessageConstant;
import com.tg.constant.ResultConstant;
import com.tg.dao.IdSequenceDAO;
import com.tg.dao.MessageDAO;
import com.tg.model.Message;
import com.tg.model.MsgExt;
import com.tg.model.UserInfo;
import com.tg.service.MessageService;
import com.tg.service.PushService;
import com.tg.service.UserService;

public class MessageServiceImpl implements MessageService {

	private MessageDAO messageDAO;
	
	private IdSequenceDAO idSequenceDAO;
	
	private UserService userService;
	
	private PushService pushService;
	
	@Override
	public List<Message> getMessage(int userId, int start, int rows) {
		// TODO Auto-generated method stub
		List<Message> messages=messageDAO.getMessage(userId, start, rows);
		renderMsg(messages);
		return messages;
	}

	@Override
	public int sendMessage(int fromId, int toId, int type, Object payload) {
		// TODO Auto-generated method stub
		Message msg=new Message();
		long id=idSequenceDAO.getMsgId();
		msg.setId(id);
		msg.setFromId(fromId);
		msg.setToId(toId);
		msg.setType(type);
		msg.setContent(genMsgContent(fromId,toId,type,payload));
		int result=messageDAO.insertMessage(msg);
		//发送push
		pushService.pushMessage(toId, msg);
		return result==1?ResultConstant.OP_OK:ResultConstant.OP_FAIL;
	}

	private String genMsgContent(int fromId,int toId,int type,Object payload){
	
		UserInfo userInfo=userService.getUserInfo(fromId);
		String content="";
		switch (type) {
		case MessageConstant.MSG_TYPE_ACCEPTED:
			content=String.format(MessageConstant.MSG_CON_ACCEPT, userInfo.getUserName());
			break;
		case MessageConstant.MSG_TYPE_BROADCAST:
			content=String.format(MessageConstant.MSG_CON_BROADCAST, userInfo.getUserName());
			break;
		case MessageConstant.MSG_TYPE_CHAT:
			content=String.valueOf(payload);
			break;
		case MessageConstant.MSG_TYPE_EVALUATED:
			content=String.format(MessageConstant.MSG_CON_EVALUATED, userInfo.getUserName());
			break;
		case MessageConstant.MSG_TYPE_INVITE:
			content=String.format(MessageConstant.MSG_CON_INVITE, userInfo.getUserName());
			break;
		case MessageConstant.MSG_TYPE_REFUSED:
			content=String.format(MessageConstant.MSG_CON_REFUSED, userInfo.getUserName());
			break;
		default:
			break;
		}
		return content;
	}
	
	private void renderMsg(List<Message> messages){
		if(messages==null || messages.size()==0)
			return;
		for(Message msg:messages){
			UserInfo userInfo=userService.getUserInfo(msg.getFromId());
			MsgExt msgExt=new MsgExt();
			msgExt.setFromHeadUrl(userInfo.getHeadUrl());
			msgExt.setFromName(userInfo.getUserName());
			msg.setMsgExt(msgExt);
		}
	}
	@Autowired
	public void setMessageDAO(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	@Autowired
	public void setIdSequenceDAO(IdSequenceDAO idSequenceDAO) {
		this.idSequenceDAO = idSequenceDAO;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
}
