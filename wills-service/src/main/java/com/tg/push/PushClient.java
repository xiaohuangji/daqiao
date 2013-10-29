package com.tg.push;

import com.tg.model.Message;
import com.tg.model.UserDevice;

public interface PushClient {

	public int pushone(UserDevice userDevice,Message message);
}
