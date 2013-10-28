package com.tg.service;

import com.tg.model.Message;
import com.tg.model.UserDevice;

public interface PushService {

	/**
	 * 绑定手机设备。
	 * 重复绑定需要更新；要把之前的绑定的解绑
	 * 一个用户只能同时存在一个设备
	 * @param userId
	 * @param token
	 * @param deviceType 1表示android,2表示ios
	 * @return
	 */
	public int bindDevice(int userId,String deviceToken,int deviceType);
	
	/**
	 * 解绑设备
	 * @param userId
	 * @param deviceType
	 * @return
	 */
	public int unbindDevice(int userId);
	
	/**
	 * 获取用户device_token
	 * @param userId
	 * @return
	 */
	public UserDevice getDevice(int userId);
	
	/**
	 * 发送消息
	 * 根据userId拿出设备,然后发送
	 */
	public int pushMessage(int userId,Message message);
}
