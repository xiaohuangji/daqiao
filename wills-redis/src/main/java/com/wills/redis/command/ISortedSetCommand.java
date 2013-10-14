package com.wills.redis.command;

import java.util.Map;
import java.util.Set;

/**
 * redis-zset接口
 * @author xingxing.feng
 * 
 */
public interface ISortedSetCommand {

	/**
	 * 往rediszset中存入一个element
	 * @param key
	 * @param value
	 * @param score
	 */
	<T> void  zadd(String key,T value,double score);
	
	/**
	 * rediszet中存入一批elements
	 * @param key
	 * @param map
	 */
	<T> void  zadd(String key,Map<Double,T> map);
	
	/**
	 * 获取zset中的元素个数
	 * @param key
	 * @return
	 */
	Long zcard(String key);
	
	/**
	 * 移除zset中一个或多个元素
	 * @param key
	 * @param values
	 * @return
	 */
	<T> Long zrem(String key ,Set<T> values);
	
	/**
	 * 获取zset中指定区间的元素
	 * @param key
	 * @param start
	 * @param offset
	 * @return
	 */
	<T> Set<T> zrange(String key ,long start,long offset,Class<T> clazz);
	
	/**
	 * 逆序获取zset中指定区间的元素
	 * @param key
	 * @param start
	 * @param offset
	 * @return
	 */
	<T> Set<T> zrevrange(String key ,long start,long offset,Class<T> clazz);
	
	/**
	 * 根据min-max区间，正序获取zset中元素
	 * @param key
	 * @param start
	 * @param offset
	 * @return
	 */
	<T> Set<T> zrangeByScore(String key ,double min,double max,Class<T> clazz);
	
	/**
	 * 根据min-max区间，逆序获取zset中元素
	 * @param key
	 * @param start
	 * @param offset
	 * @return
	 */
	<T> Set<T> zrevrangeByScore(String key ,double min,double max,Class<T> clazz);
	
	
}
