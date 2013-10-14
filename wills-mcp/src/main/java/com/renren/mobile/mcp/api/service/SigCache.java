/**
 * $Id: SigCache.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.service;

/**
 * 缓存sig，用于防重发机制
 * 
 * @author sunji
 * 
 */
public interface SigCache {

    public String get(String key);

    public void set(String key, String value, int seconds);

    public void del(String key);

}
