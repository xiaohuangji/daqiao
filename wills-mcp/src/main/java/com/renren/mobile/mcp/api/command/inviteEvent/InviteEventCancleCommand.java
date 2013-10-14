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

public class InviteEventCancleCommand extends AbstractApiCommand{

	private static final Log logger = LogFactory.getLog(InviteEventCancleCommand.class);

    private InviteEventService inviteEventService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
        int userId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();

        String eventId=stringParams.get("eventId");
        String guideId=stringParams.get("guideId");
        
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result = inviteEventService.cancle(NumberUtils.toLong(eventId), userId, NumberUtils.toInt(guideId));
            McpUtils.rpcTimeCost(t, "inviteEvent.invite");
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
