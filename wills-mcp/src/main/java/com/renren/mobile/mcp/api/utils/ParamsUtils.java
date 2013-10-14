/**
 * $Id: ParamsUtils.java 6685 2013-03-20 12:31:36Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.renren.mobile.mcp.utils.McpUtils;

/**
 * 
 * @author chengwei
 * 
 */
public class ParamsUtils {

    private static final Set<String> methodNames = new HashSet<String>(); // stat_interface.log不打印result结果的方法列表

    static {
        methodNames.add("feed.getDefaultList");
        methodNames.add("feed.getAll");
        methodNames.add("feed.list");
        methodNames.add("feed.getByPerson");
        methodNames.add("feed.getFeedList");
        methodNames.add("feed.getFeedsByPerson");

        methodNames.add("feed.getListeners");
        methodNames.add("feed.getLikers");

        methodNames.add("feed.getComments");

        methodNames.add("user.getCrossFollow");
        methodNames.add("user.getFollowings");
        methodNames.add("user.getFollowers");
        methodNames.add("user.getSNSFriends4Guide");
        methodNames.add("user.search");

        methodNames.add("user.suggestToFollow");
        methodNames.add("user.getSNSFriends");
    }

    public static Object fromJson(String s) {
        if ((s.startsWith("{") && s.endsWith("}")) || (s.startsWith("[") && s.endsWith("]"))
                || StringUtils.isNumeric(s)) {
            try {
                Object object = McpUtils.gson.fromJson(s, Object.class);
                return object;
            } catch (Exception e) {
                return s;
            }
        } else {
            return s;
        }
    }

    public static boolean resultNotPrintByName(String methodName) {
        if (methodNames.contains(methodName)) {
            return true;
        } else {
            return false;
        }
    }

}
