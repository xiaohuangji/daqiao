package com.wills.redis.command;

import java.util.List;

/**
 * string 类型的redis接口
 * @author xingxing.feng
 * 
 */
public interface IStringCommand {
	
	/**
	 * set一个key
	 */
	<T> void set(String key, T value);

	/**
	 * set一个key,带过期时间
	 */
	<T> void setex(String key, T value,  int seconds);

	/**
	 * 获取对象
	 */
	<T> T get(String key, Class<T> clazz);
	
	/**
	 * 删除一组key
	 * @param 
	 */
	void del(String key);


	/**
	 * 判断一个key是否存在
	 * @param key
	 * @return
	 */
	Boolean exists(String key);

	/**
	 * 单独设置过期时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	void expire(String key, int seconds);
	
	/**
	 * 获取对象
	 */
	<T> List<T> mget(List<String> keys, Class<T> clazz);
	
	/**
	 * ++1
	 * @param key
	 * @return
	 */
	Long incr(String key);

}
