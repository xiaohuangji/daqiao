package com.wills.redis.serialize;

import java.util.List;
import java.util.Set;

/**
 * 反序列化
 * @author xingxing.feng
 *
 */
public interface IPlainObjectDeserializer {
	
	 <T> T deserialize(String str, Class<T> clazz);
	
	 /**
	  * 不返回全部字节数组，而是集合化的数组，有利于使用REDIS的存储结构和方法
	 * @param <T>
	 * @param bytesList
	 * @param clazz
	 * @return
	 */
	<T> List<T> deserialize2List(List<String> strList,Class<T> clazz);
	 
	<T> Set<T> deserialize2Set(Set<String> strList,Class<T> clazz);

   // <T> Map<String,T> deserialize2Map(Map<byte[],byte[]> bytesMap,Class<T> clazz);


}
