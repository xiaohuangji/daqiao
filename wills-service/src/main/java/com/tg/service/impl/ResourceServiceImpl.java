package com.tg.service.impl;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;

import com.baidu.bcs.BCSClient;
import com.tg.service.ResourceService;

public class ResourceServiceImpl implements ResourceService{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ResourceServiceImpl.class);

	
	@Override
	public String uploadResource(byte[] data,String suffix) {
		// 存储文件 
		String fileName=BCSClient.putObject( new ByteArrayInputStream(data),data.length ,suffix);
		logger.info("upload resource succ:"+fileName);
		return BCSClient.fileUrl(fileName);
	}

}
