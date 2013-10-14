package com.renren.mobile.mcp.api.command.inviteEvent;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.service.InviteEventService;

public class InviteEventInviteAllCommand extends AbstractApiCommand{

	private static final Log logger = LogFactory.getLog(InviteEventInviteAllCommand.class);

    private InviteEventService inviteEventService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
        int userId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();

        String scenic=stringParams.get("scenic");
        String startTime=stringParams.get("startTime");
        String endTime=stringParams.get("endTime");
        String location=stringParams.get("location");
        String gender=stringParams.get("gender");
        
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result = inviteEventService.inviteAll(userId,scenic, NumberUtils.toLong(startTime), NumberUtils.toLong(endTime),location,NumberUtils.toInt(gender));
            McpUtils.rpcTimeCost(t, "inviteEvent.inviteAll");
        } catch (Exception e) {
            // 异常记录日志， 返回错误信息
            logger.error("RPC error ", e);
            apiResult = new ApiResult(ApiResultCode.E_SYS_RPC_ERROR);
            return apiResult;
        }

        // 正常返回接口数据
        apiResult = new ApiResult(ApiResultCode.SUCCESS, result);
        return apiResult;
    }

	public void setInviteEventService(InviteEventService inviteEventService) {
		this.inviteEventService = inviteEventService;
	}


}
