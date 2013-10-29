package com.tg.client;

import com.tg.model.Message;
import com.tg.model.UserDevice;
import com.tg.service.PushService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class PushServiceDelegate implements PushService{

	private PushService pushServiceDelegate;
	
	public PushServiceDelegate(){
		pushServiceDelegate=HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), PushService.class);
	}
	
	@Override
	public int bindDevice(int userId, String deviceToken, int deviceType) {
		// TODO Auto-generated method stub
		return pushServiceDelegate.bindDevice(userId, deviceToken, deviceType);
	}

	@Override
	public int unbindDevice(int userId) {
		// TODO Auto-generated method stub
		return pushServiceDelegate.unbindDevice(userId);
	}

	@Override
	public UserDevice getDevice(int userId) {
		// TODO Auto-generated method stub
		return pushServiceDelegate.getDevice(userId);
	}

	@Override
	public int pushMessage(int userId, Message message) {
		// TODO Auto-generated method stub
		return pushServiceDelegate.pushMessage(userId, message);
	}

}
