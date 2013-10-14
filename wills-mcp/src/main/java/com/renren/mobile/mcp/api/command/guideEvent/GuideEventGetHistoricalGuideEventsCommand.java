package com.renren.mobile.mcp.api.command.guideEvent;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.service.GuideEventService;

public class GuideEventGetHistoricalGuideEventsCommand extends AbstractApiCommand{

	private static final Log logger = LogFactory.getLog(GuideEventGetHistoricalGuideEventsCommand.class);

    private GuideEventService guideEventService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
        int guideId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();

        String start=stringParams.get("start");
        String rows=stringParams.get("rows");
        
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result = guideEventService.getHistoricalGuideEvents(guideId, NumberUtils.toInt(start), NumberUtils.toInt(rows));
            McpUtils.rpcTimeCost(t, "guideEvent.getHistorical");
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

	public void setGuideEventService(GuideEventService guideEventService) {
		this.guideEventService = guideEventService;
	}

}
