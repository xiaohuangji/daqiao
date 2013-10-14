package com.wills.redis.serialize.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson实现json-object的转换
 * @author wills
 *
 */
public class GsonTools {

	static Gson gson=new GsonBuilder().create();
	
	public static <T> String objectToJson(T o){
		return gson.toJson(o);
	}
	
	public static <T> T jsonToObject(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}
