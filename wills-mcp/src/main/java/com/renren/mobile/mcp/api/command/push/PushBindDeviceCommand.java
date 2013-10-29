package com.renren.mobile.mcp.api.command.push;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.service.PushService;

public class PushBindDeviceCommand extends AbstractApiCommand {

	private static final Log logger = LogFactory.getLog(PushBindDeviceCommand.class);

    private PushService pushService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
       int userId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();

        String deviceToken=stringParams.get("deviceToken");
        String deviceType=stringParams.get("deviceType");
      
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result = pushService.bindDevice(userId, deviceToken, Integer.valueOf(deviceType));
            McpUtils.rpcTimeCost(t, "push.bindDevice");
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
