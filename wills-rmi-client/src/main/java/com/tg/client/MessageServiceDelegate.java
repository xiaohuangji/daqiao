package com.tg.client;

import java.util.List;

import com.tg.model.Message;
import com.tg.service.MessageService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class MessageServiceDelegate implements MessageService{

	private MessageService messageServiceDelegate;
	
	public MessageServiceDelegate(){
		messageServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), MessageService.class);

	}
	@Override
	public List<Message> getMessage(int userId, int start, int rows) {
		// TODO Auto-generated method stub
		return messageServiceDelegate.getMessage(userId, start, rows);
	}

	@Override
	public int sendMessage(int fromId, int toId, int type, Object payload) {
		// TODO Auto-generated method stub
		return messageServiceDelegate.sendMessage(fromId, toId, type, payload);
	}

}
