package com.wills.redis.serialize.impl;

import static com.wills.redis.serialize.impl.GsonTools.jsonToObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.wills.redis.serialize.IPlainObjectDeserializer;

/**
 * 反序列化实现
 * 
 * @author xingxing.feng
 * 
 */
public class JsonPlainObjectDeserializer implements IPlainObjectDeserializer {

	@Override
	public <T> T deserialize(String str, Class<T> clazz) {
		return jsonToObject(str, clazz);
	}

	@Override
	public <T> List<T> deserialize2List(List<String> strList, Class<T> clazz) {
		if ( strList==null || strList.size()==0 )
			return null;
		List<T> list = new ArrayList();
		for (String str : strList) {
			T t = deserialize(str, clazz);
			list.add(t);
		}
		return list;
	}

	@Override
	public <T> Set<T> deserialize2Set(Set<String> strSet, Class<T> clazz) {
		if( strSet==null || strSet.size()==0 )
			return null;
		Set<T> set = new LinkedHashSet();
		for (String str : strSet) {
			T t = deserialize(str, clazz);
			set.add(t);
		}
		return set;
	}

/*	@Override
	public <T> Map<String, T> deserialize2Map(Map<byte[], byte[]> bytesMap,
			Class<T> clazz) {
		Map<String, T> map = new HashMap();
		Set<byte[]> keyset = bytesMap.keySet();
		for (byte[] bs : keyset) {
			String k = null;
			try {

				k = new String(bs, "utf8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			T t = deserialize(bytesMap.get(bs), clazz);
			map.put(k, t);
		}

		return map;
	}*/

}
