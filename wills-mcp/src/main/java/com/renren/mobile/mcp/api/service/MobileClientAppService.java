/**
 * $Id: MobileClientAppService.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.service;

import com.renren.mobile.mcp.api.entity.MobileClientAppInfo;

/**
 * @author sunji
 * 
 */
public interface MobileClientAppService {

    public MobileClientAppInfo getAppInfo(int appId);

    public boolean isAllowedApiMethod(int appId, String methodName);
}
