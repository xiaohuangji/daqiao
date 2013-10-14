package com.tg.service.impl;

import java.io.ByteArrayInputStream;

import com.baidu.bcs.BCSClient;
import com.tg.service.ResourceService;

public class ResourceServiceImpl implements ResourceService{

	@Override
	public String uploadResource(byte[] data,String suffix) {
		// 存储文件 
		String fileName=BCSClient.putObject( new ByteArrayInputStream(data),data.length ,suffix);
		return BCSClient.fileUrl(fileName);
	}

}
