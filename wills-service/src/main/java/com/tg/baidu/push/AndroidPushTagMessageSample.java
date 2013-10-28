package com.tg.baidu.push;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class AndroidPushTagMessageSample {

	public static void main(String[] args) {
		
		/*
		 * @brief	�����鲥��Ϣ(��Ϣ����Ϊ͸�����ɿ�����Ӧ���Լ���������Ϣ����)
		 * 			message_type = 0 (Ĭ��Ϊ0)
		 */
		
		// 1. ����developerƽ̨��ApiKey/SecretKey
		String apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		String secretKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		
		// 2. ����BaiduChannelClient����ʵ��
		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
		
		// 3. ��Ҫ�˽⽻��ϸ�ڣ���ע��YunLogHandler��
		channelClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});
		
		try {
			
			// 4. �������������
			PushTagMessageRequest request = new PushTagMessageRequest();
			request.setDeviceType(3); 	// device_type => 1: web 2: pc 3:android 4:ios 5:wp	
			request.setTagName("xxxx");
			request.setMessage("Hello Channel");
			// ��Ҫ֪ͨ��
			//			request.setMessageType(1);
			//			request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");
			
			// 5. ����pushMessage�ӿ�
			PushTagMessageResponse response = channelClient.pushTagMessage(request);
				
			// 6. ��֤���ͳɹ�
			System.out.println("push amount : " + response.getSuccessAmount()); 
			
		} catch (ChannelClientException e) {
			// ����ͻ��˴����쳣
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// �������˴����쳣
			System.out.println(
					String.format("request_id: %d, error_code: %d, error_message: %s" , 
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()
						)
					);
		}
		
	}
	
}
