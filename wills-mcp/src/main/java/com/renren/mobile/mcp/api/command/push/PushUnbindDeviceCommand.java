package com.renren.mobile.mcp.api.command.push;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.service.PushService;

public class PushUnbindDeviceCommand extends AbstractApiCommand {

	private static final Log logger = LogFactory.getLog(PushUnbindDeviceCommand.class);

    private PushService pushService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
       int userId = context.getUserId();
       // Map<String, String> stringParams = context.getStringParams();
      
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result = pushService.unbindDevice(userId);
            McpUtils.rpcTimeCost(t, "push.unbindDevice");
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

    @Autowired
	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}
}
