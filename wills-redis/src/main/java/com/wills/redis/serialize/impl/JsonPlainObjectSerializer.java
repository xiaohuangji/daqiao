package com.wills.redis.serialize.impl;

import static com.wills.redis.serialize.impl.GsonTools.objectToJson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wills.redis.serialize.IPlainObjectSerializer;

/**
 * 序列化实现
 * @author xingxing.feng
 *
 */
public class JsonPlainObjectSerializer implements IPlainObjectSerializer {

	@Override
	public <T> String serialize(T obj) {
		return objectToJson(obj);
	}

	@Override
	public <T> List<String> serialize(List<T> objs) {
		if(objs==null||objs.isEmpty()){
			return null;
		}
		List<String> list= new ArrayList();
		for (T t : objs) {
			String jsonStr=serialize(t);
			list.add(jsonStr);
		}
		return list;
	}

	@Override
	public <T> Set<String> serialize(Set<T> objs) {
		if(objs==null||objs.isEmpty()){
			return null;
		}
		Set<String> set= new HashSet();
		for (T t : objs) {
			String jsonStr=serialize(t);
			set.add(jsonStr);
		}
		return set;
	}

/*	@Override
	public <T> Map<byte[], byte[]> serialize(Map<String, T> objs) {
		// TODO Auto-generated method stub
		if(objs==null||objs.isEmpty()){
			return null;
		}
	    Set<String> keyset=objs.keySet();
	    
		Map<byte[],byte[]> map= new HashMap<byte[],byte[]>();
		for(String mkey:keyset){	
			byte[] value=serialize(objs.get(mkey));
			map.put(mkey.getBytes(),value );
		}
		return map;
	}
*/


}
