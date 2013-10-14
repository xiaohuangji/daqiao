package com.tg.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public class ListStrUtil {

	private static String SPLIT=",";
	public static List<Integer> strToList(String str){
		if(str==null||str.length()==0){
			return null;
		}
		String strs[]=str.split(SPLIT);
		List<Integer> list=new ArrayList<Integer>();
		for(String s:strs){
			if(s!=null&&s.length()!=0)
			  list.add(NumberUtils.toInt(s));
		}
		return list;
	}
	
	public static String listToStr(List<Integer> list){
		if(list==null|| list.size()==0){
			return null;
		}
		StringBuffer sb=new StringBuffer();
		for(Integer i:list){
			sb.append(i);
			sb.append(SPLIT);
		}
		return sb.toString();
	}
}
