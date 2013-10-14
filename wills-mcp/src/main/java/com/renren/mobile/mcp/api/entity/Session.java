/**
 * $Id: Session.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

import java.io.Serializable;

/**
 * @author sunji
 * 
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final long TIMEOUT = 30L * 24 * 60 * 60 * 1000L;

    private int userId;

    private long timeStamp;

    private String key;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
