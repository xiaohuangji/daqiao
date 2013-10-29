package com.tg.push;

import com.tg.constant.DeviceConstant;
import com.tg.constant.ResultConstant;
import com.tg.model.Message;
import com.tg.model.UserDevice;

public class PushClientFactory {

	public static int pushone(UserDevice userDevice ,Message message){
		PushClient  pushClient=null;
		if(userDevice.getDeviceType()==DeviceConstant.D_TYPE_ANDROID){
			pushClient=AndroidPushClient.getInstance();
		}else if(userDevice.getDeviceType()==DeviceConstant.D_TYPE_IOS){
			pushClient=ApplePushClient.getInstance();
		}else{
			return ResultConstant.OP_FAIL;
		}
		return pushClient.pushone(userDevice, message);
	}
}
