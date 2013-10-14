/**
 * $Id: MobileClientAppInfo.java 4782 2013-03-06 10:04:35Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 应用的基本信息
 * 
 * @author sunji
 * 
 */
public class MobileClientAppInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 接入方编号 */
    private int appId;

    /** 接入方名称 */
    private String appName;

    /** 接入方私钥 */
    private String secretKey;

    /** 接入方的主页 */
    private String appUrl;

    private Date createTime;
 
    /** 接入方编号 */
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    /** 接入方名称 */
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /** 接入方私钥 */
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /** 接入方的主页,已经被current.getFeedDisplayUrl()取代 */
    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MobileClientAppInfo [appId=" + appId + ", appName=" + appName + ", secretKey="
                + secretKey + ", appUrl=" + appUrl + ", createTime=" + createTime + "]";
    }

}
