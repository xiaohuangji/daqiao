package com.tg.push;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tg.constant.ResultConstant;
import com.tg.model.Message;
import com.tg.model.UserDevice;
import com.tg.util.CONFIGUtil;

public class AndroidPushClient implements PushClient {

	private static AndroidPushClient pushClient = null;

	public static AndroidPushClient getInstance() {
		if (pushClient == null) {
			pushClient = new AndroidPushClient();
		}
		return pushClient;
	}

	private static String apiKey = CONFIGUtil.getInstance().getConfig("bpush_apiKey");
	private static String secretKey = CONFIGUtil.getInstance().getConfig("bpush_secretKey");
	private static int ANDROID_DEVICE_TYPE = 3;
	private static String ANDROID_TOKEN_SPLITSTR = "\\@\\#\\$";
	private static Gson gson=new GsonBuilder().create();

	private BaiduChannelClient channelClient = null;

	private AndroidPushClient() {
		channelClient = new BaiduChannelClient(new ChannelKeyPair(apiKey,
				secretKey));
	}

	@Override
	public int pushone(UserDevice userDevice, Message message) {
		// TODO Auto-generated method stub
		PushUnicastMessageRequest request = new PushUnicastMessageRequest();
		request.setDeviceType(ANDROID_DEVICE_TYPE);
		BaiduChannel channel = new BaiduChannel(userDevice);

		request.setChannelId(channel.channelId);
		request.setUserId(channel.baiduUserId);

		// 组装消息格式,将message转成json
		request.setMessage(gson.toJson(message));

		PushUnicastMessageResponse response = null;

		try {
			response = channelClient.pushUnicastMessage(request);
			int result = response.getSuccessAmount();
			if (result == 1) {
				return ResultConstant.OP_OK;
			}
		} catch (ChannelClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResultConstant.OP_FAIL;
	}

	/**
	 * baidupush单播推送需要的标记 从客户端上传的token中解析而来 此userId与系统中userId是两回事
	 * 
	 * @author
	 * 
	 */
	class BaiduChannel {
		public long channelId;
		public String baiduUserId;

		public BaiduChannel(UserDevice userDevice) {
			// TODO Auto-generated constructor stub
			String token = userDevice.getDeviceToken();
			String[] ts = token.split(ANDROID_TOKEN_SPLITSTR);
			channelId = Long.valueOf(ts[0]);
			baiduUserId = ts[1];
		}
	}
}
