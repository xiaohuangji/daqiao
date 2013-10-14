package com.tg.service;


public interface ResourceService {

	/**
	 * 资源上传接口
	 * @param is 文件流
	 * @param length  文件长度
	 * @param suffix  文件后缀
	 * @param type  资源类型//先不用这个字段
	 * @return
	 */
	public String uploadResource(byte[] d,String suffix);
}
