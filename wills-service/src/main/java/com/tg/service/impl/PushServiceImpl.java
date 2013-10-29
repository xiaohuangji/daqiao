package com.tg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tg.constant.RedisKeyConstant;
import com.tg.constant.ResultConstant;
import com.tg.dao.UserDeviceDAO;
import com.tg.model.Message;
import com.tg.model.UserDevice;
import com.tg.push.PushClientFactory;
import com.tg.service.PushService;
import com.wills.redis.client.RedisClient;

public class PushServiceImpl implements PushService{

	private UserDeviceDAO userDeviceDAO;
	
	private RedisClient redisClient=new RedisClient(RedisKeyConstant.USER_DEVICE_INFO);
	
	@Override
	public int bindDevice(int userId, String deviceToken, int deviceType) {
		// TODO Auto-generated method stub
		int result=userDeviceDAO.addDevice(userId, deviceType, deviceToken);
		redisClient.del(String.valueOf(userId));
		return result>0?ResultConstant.OP_OK:ResultConstant.OP_FAIL;
	}

	@Override
	public int unbindDevice(int userId) {
		// TODO Auto-generated method stub
		userDeviceDAO.removeDevice(userId);
		redisClient.del(String.valueOf(userId));
		return ResultConstant.OP_OK;
	}

	@Override
	public UserDevice getDevice(int userId) {
		// TODO Auto-generated method stub
		UserDevice userDevice=redisClient.get(String.valueOf(userId), UserDevice.class);
		if(userDevice==null){
			userDevice=userDeviceDAO.getDevice(userId);
			if(userDevice!=null){
				redisClient.set(String.valueOf(userId), userDevice);
			}
		}
		return userDevice;
	}
	
	@Override
	public int pushMessage(int userId, Message message) {
		// TODO Auto-generated method stub
		UserDevice userDevice=getDevice(userId);
		//获取到user_device_token
		return PushClientFactory.pushone(userDevice, message);
	}

	@Autowired
	public void setUserDeviceDAO(UserDeviceDAO userDeviceDAO) {
		this.userDeviceDAO = userDeviceDAO;
	}
}
