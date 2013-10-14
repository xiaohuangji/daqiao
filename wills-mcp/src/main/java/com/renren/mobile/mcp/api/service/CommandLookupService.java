/**
 * $Id: CommandLookupService.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.service;

import com.renren.mobile.mcp.api.command.ApiCommand;

/**
 * @author Marshal(shuai.ma@renren-inc.com) Initial Created at 2012-6-13
 */
public interface CommandLookupService {

    /**
     * lookup api command from methodValue
     * 
     * @param methodValue
     * @return
     */
    public ApiCommand lookupApiCommand(String methodValue);

    /**
     * 是否需要登录
     * 
     * @param methodvalue
     * @return
     */
    public boolean isNeedLogin(String methodValue);

    /**
     * 取所有的接口名称列表
     * 
     * @return
     */
    public String[] getCommands();
}
