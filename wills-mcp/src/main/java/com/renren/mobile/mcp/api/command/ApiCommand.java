/**
 * $Id: ApiCommand.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.command;

import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;

/**
 * 命令接口
 * 
 * @author ji.sun
 */
public interface ApiCommand {

    /**
     * Execute the command and return result
     * 
     * @param context
     * @return
     */
    ApiResult execute(ApiCommandContext context);

}
