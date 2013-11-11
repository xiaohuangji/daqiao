package com.tg.push;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.tg.constant.ResultConstant;
import com.tg.model.Message;
import com.tg.model.UserDevice;
import com.tg.util.CONFIGUtil;

public class ApplePushClient implements PushClient{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ApplePushClient.class);

	private static ApplePushClient pushClient=null;
	
	public final String PASSWORD = "123456";

	public final boolean PUSH_TO_PRODUCT = Boolean.valueOf(CONFIGUtil.getInstance().getConfig("production"));
	
	private static final String apnsKey = CONFIGUtil.getInstance().getConfig("apnsKey");

	public static final String PUSH_CERTIFICATE_FILE_PATH_DEV = Thread.currentThread()
			.getContextClassLoader().getResource("").getPath()
			+ apnsKey;
	
	private static PushNotificationManager pushManager = new PushNotificationManager();
	
	public static ApplePushClient getInstance(){
		if(pushClient==null){
			pushClient=new ApplePushClient();
		}
		return pushClient;
	}

	private ApplePushClient(){
		try {
			pushManager.initializeConnection(new AppleNotificationServerBasicImpl(
					PUSH_CERTIFICATE_FILE_PATH_DEV, PASSWORD,
					PUSH_TO_PRODUCT));
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeystoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int pushone(UserDevice userDevice, Message message) {
		// TODO Auto-generated method stub
		if (null == userDevice.getDeviceToken() || "".equals(userDevice.getDeviceToken())) {
			return ResultConstant.OP_FAIL;
		}
		PushNotificationPayload payload = new PushNotificationPayload();
		try {

			// basic info
			payload.addAlert(message.getContent()); // 消息内容
			payload.addSound("default"); // 声音提示文件
			payload.addBadge(1); // iphone应用图标上小红圈上的数值

			//custom info
			payload.addCustomDictionary("type",message.getType());
			payload.addCustomDictionary("fromId", message.getFromId());
			payload.addCustomDictionary("createTime", String.valueOf(message.getCreateTime()));
			payload.addCustomDictionary("id", String.valueOf(message.getId()));
			
			int Maxlength = payload.getMaximumPayloadSize();
			int length = payload.getPayloadSize();

			// push长度限制
			if (length <= Maxlength) {

				// 设备
				Device device = new BasicDevice();
				device.setToken(userDevice.getDeviceToken());

				//发送到沙箱
				PushedNotification NotificationResult = pushManager
						.sendNotification(device, payload, false);
				
				if (NotificationResult.isSuccessful()) {
					logger.debug("ios push message success.");
				} else {
					logger.warn("ios push message failed.");
				}
				
			} else {
				logger.error("Can't send push message success for total payload size beyond maxSize:256");
			}
		} catch (JSONException e) {
			logger.error("ios push payload jsonException:" , e);
		} catch (CommunicationException e) {
			logger.error("ios push communicationException:" , e);
		} catch (KeystoreException e) {
			logger.error("ios push keystoreException:" ,e);
		} catch (Exception e) {
			logger.error("ios push exception:" , e);
		}
		
		return ResultConstant.OP_OK;
	}

}
