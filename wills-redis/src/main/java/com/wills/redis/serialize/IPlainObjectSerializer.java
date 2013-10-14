package com.wills.redis.serialize;

import java.util.List;
import java.util.Set;

/**
 * 序列化
 * @author xingxing.feng
 *
 */
public interface IPlainObjectSerializer {
	
	 /**
	  * 把一个对象转换json串
	 * @param <T>
	 * @param obj
	 * @return
	 */
	<T> String serialize(T obj);
	
	 /**
	 * 将一组list对象转化为list的json串
	 * @param <T>
	 * @param objs
	 * @return
	 */
	<T> List<String> serialize(List<T> objs);
	 
	 /**
	  * 将一组set对象转化为set的json串
	 * @param <T>
	 * @param objs
	 * @return
	 */
	<T> Set<String> serialize(Set<T> objs);


	/**
	 * 
	 * @param objs
	 * @return
	 */
    //<T> Map<byte[],byte[]> serialize(Map<String,T> objs);

}
