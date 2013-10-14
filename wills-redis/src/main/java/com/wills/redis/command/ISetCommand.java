package com.wills.redis.command;

import java.util.Set;

/**redis-set接口
 * @authorxingxing.feng
 */
public interface ISetCommand {

	/**
	 * 往redisset中填加一个element
	 * @param key
	 * @param value
	 */
	<T> void sadd(String key ,T value);
	
	/**
	 * 往redisset中填加一批element
	 * @param key
	 * @param values
	 */
	<T> void sadd(String key ,Set<T> values);
	
	/**
	 * 获取set的长度
	 * @param key
	 * @return
	 */
	Long scard(String key);
	
	/**
	 * 删除set中一个或多个元素
	 * @param key
	 * @param values
	 * @return
	 */
	<T> Long srem(String key,Set<T> values);
	
	/**
	 * 获取set中全部元素
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T> Set<T> smember(String key,Class<T> clazz);
}
