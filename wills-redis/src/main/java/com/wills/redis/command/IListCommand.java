package com.wills.redis.command;

import java.util.List;

/**
 * @xingxing.feng
 */
public interface IListCommand {

	
	/**
	 * 从左向redislist中增加一个element
	 * @param key
	 * @param value
	 */
	<T> long lpush(String key,T value);
	
	/**
	 * 从左向redislist中增加一组element
	 * @param key
	 * @param values
	 */
	<T> long lpush(String key,List<T> values);
	
	/**
	 * 从右向redislist中增加一个element
	 * @param key
	 * @param value
	 */
	<T> long rpush(String key,T value);
	
	/**
	 * 从右向redislist中增加一组element
	 * @param key
	 * @param values
	 */
	<T> long rpush(String key,List<T> values);
	
	/**
	 * 获取list的长度
	 * @param key
	 * @return
	 */
	long llen(String key);
	
	/**
	 * 获取list中指定区间的元素
	 * @param key
	 * @param start
	 * @param offset
	 * @return
	 */
	<T> List<T> lrange(String key ,long start,long offset,Class<T> clazz);
	
	/**
	 * 移除list中==value的元素
	 * 	count > 0: Remove elements equal to value moving from head to tail.
	 *	count < 0: Remove elements equal to value moving from tail to head.
	 *	count = 0: Remove all elements equal to value.
	 * @param key
	 * @param l 
	 * @param value
	 * @return
	 */
	<T> Long lrem(String key ,long count,T value);
	
	
	/**
	 * 从左向redislist中增加一个element
	 * 如果list存在的话才会执行
	 * @param key
	 * @param value
	 */
	<T> long lpushx(String key,T value);
	
	/**
	 * 从右向redislist中增加一个element
	 * 如果list存在的话才会执行
	 * @param key
	 * @param value
	 */
	<T> long rpushx(String key,T value);
	
	
}
