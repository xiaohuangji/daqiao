package com.wills.redis.command;

import java.util.Map;

/**redis-map接口
 * @author xingxing.feng
 *
 */
public interface IMapCommand {

	/**
	 * 向map中存入一对kv
	 * @param key
	 * @param mkey
	 * @param value
	 * @return
	 */
	<T> Long hset(String key ,String mkey,T value);
	
	/**
	 * 向map中存入一组kv
	 * @param key
	 * @param map
	 * @return
	 */
	String hmset(String key ,Map<String,Object> map);
	
	/**
	 * 获取map中指定key的value
	 * @param key
	 * @param mkey
	 * @return
	 */
	String hget(String key ,String mkey);
	
	/**
	 * 获取map中所有kv。无泛型，返回都是string
	 * @param key
	 * @return
	 */
	Map<String ,String> hgetAll(String key);
	
	/**
	 * 返回map中的kv对数量
	 * @param key
	 * @return
	 */
	Long hlen(String key);
	
	/**
	 * 删除map中的某个key
	 * @param key
	 * @param hkey
	 * @return
	 */
	Long hdel(String key,String hkey);
	
	/**
	 * 增加map中某key
	 * @param key
	 * @param hkey
	 * @return
	 */
	Long hincrBy(String key ,String hkey,long num);
	
	/**
	 * 判断hmap中是否存在hkey
	 * @param key
	 * @param hkey
	 * @return
	 */
    Boolean hexists(String key ,String hkey);
}
