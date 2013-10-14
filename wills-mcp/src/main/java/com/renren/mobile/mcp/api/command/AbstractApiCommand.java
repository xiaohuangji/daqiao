/**
 * $Id: AbstractApiCommand.java 12837 2013-05-06 06:11:15Z ji.sun $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.renren.mobile.mcp.accesslog.AccessLogUtils;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.api.entity.ClientInfo;
import com.renren.mobile.mcp.api.utils.ParamsUtils;
import com.renren.mobile.mcp.constants.HttpConstants;
//import com.renren.mobile.mcp.interfacelog.InterfaceLogUtils;
import com.renren.mobile.mcp.utils.McpUtils;

/**
 * @author sunji
 * 
 */
public abstract class AbstractApiCommand implements ApiCommand {

    private static final Log userAccesslogger = LogFactory.getLog("mcp_user_access_log");

    private static final Log statAccessLogger = LogFactory.getLog("mcp_stat_access_log");

    private static final Log statInterfaceLogger = LogFactory.getLog("mcp_stat_interface_log");

    /**
     * 子类不可重写
     */
    @Override
    public final ApiResult execute(ApiCommandContext context) {
        long starTime = System.currentTimeMillis();

        ApiResult apiResult = null;

        // ======statAccessLog 统计用 start======
        String methodName = context.getMethodName();
        String extra1 = "";

        int value = 1;
        String identifier = "";
        Map<String, String> stringParams = context.getStringParams();
        String clientIp = stringParams.get(HttpConstants.CLIENT_IP);
        ClientInfo clientInfo = context.getClientInfo();
        if ("user.login".equals(methodName) || "user.newLogin".equals(methodName)) {
            identifier = "login";
            int snsType = NumberUtils.toInt(stringParams.get("snsType"), 0);
            if (snsType == 1) {
                extra1 = "fb";
            } else if (snsType == 2) {
                extra1 = "tw";
            } else if (snsType == 8) {
                extra1 = "gg";
            } else if (snsType == 16) {
                extra1 = "tb";
            }
        }
        if ("sns.connect".equals(methodName)) {
            int snsType = NumberUtils.toInt(stringParams.get("snsType"));
            if (snsType == 1) {
                extra1 = "fb";
            } else if (snsType == 2) {
                extra1 = "tw";
            } else if (snsType == 8) {
                extra1 = "gg";
            } else if (snsType == 16) {
                extra1 = "tb";
            }
        }
        if ("user.completeUserInfo".equals(methodName)) {
            identifier = "register";
            int snsType = NumberUtils.toInt(stringParams.get("snsType"));
            if (snsType == 1) {
                extra1 = "fb";
            } else if (snsType == 2) {
                extra1 = "tw";
            }
        }
        if ("feed.publish".equals(methodName)) {
            identifier = "ugc.feed";
            value = NumberUtils.toInt(stringParams.get("duration"));
        }
        if ("feed.comment".equals(methodName)) {
            identifier = "ugc.comment";
            value = NumberUtils.toInt(stringParams.get("duration"));
        }
        if ("sns.share".equals(methodName)) {
            identifier = "ugc.share";
        }
        if ("feed.like".equals(methodName)) {
            identifier = "ugc.like";
        }
        if ("user.follow".equals(methodName)) {
            identifier = "ugc.follow";
        }
        // ======statAccessLog 统计用 end======

        this.beforeExecute(context);
        apiResult = this.onExecute(context);
        this.afterExecute(context, apiResult);

        // ======statAccessLog 统计用 start======
        String extra2 = "";
        if ("user.login".equals(methodName) || "user.newLogin".equals(methodName)) {
            extra2 = apiResult.getCode() == ApiResultCode.SUCCESS ? "success" : "failure";
        }

        long logTime = System.currentTimeMillis();
        // time|service|service_api|uid|app_id|uniq_id|mac|model|os|from_id|version|ip|business_type|identifier|sample|value|extra1|extra2|extra3|extra4|extra5|expand
//        String statLogStr = AccessLogUtils.log(statAccessLogger, logTime, "mcp", methodName,
//                context.getUserId() + "", context.getMcpAppInfo().getAppId() + "",
//                clientInfo.getUniqid(), clientInfo.getMac(), clientInfo.getModel(),
//                clientInfo.getOs(), clientInfo.getFrom() + "", clientInfo.getVersion(), clientIp,
//                "dubblerServer", identifier, "1", value + "", extra1, extra2, "", "",
//                apiResult.getCode() + "", "");
        // ======statAccessLog 统计用 end======

        // ======statInterfaceLog 统计用 start======
        // time|service|service_api|uid|app_id|uniq_id|mac|model|os|from_id|version|ip|business_type|identifier|sample|value|parameter|return|result|

        // TODO 调用接口时上传的参数转成JSON串
        Map<String, Object> paramsMap = new HashMap<String, Object>();

        for (String key : stringParams.keySet()) {
            if (HttpConstants.platformParams.contains(key)) continue; // 跳过平台级参数
            String param = stringParams.get(key);
            Object obj = ParamsUtils.fromJson(param);
            paramsMap.put(key, obj);
        }

        String params = McpUtils.gson.toJson(paramsMap);
        String rt = "";
        if (apiResult.getCode() == 0) {
            if (ParamsUtils.resultNotPrintByName(methodName)) {
                rt = "";
            } else {
                rt = McpUtils.buildJSONResult(apiResult.getData());
            }
        }
//        InterfaceLogUtils.log(statInterfaceLogger, logTime, "mcp", methodName, context.getUserId()
//                + "", context.getMcpAppInfo().getAppId() + "", clientInfo.getUniqid(),
//                clientInfo.getMac(), clientInfo.getModel(), clientInfo.getOs(),
//                clientInfo.getFrom() + "", clientInfo.getVersion(), clientIp, "dubblerServer",
//                identifier, "1", value + "", params, rt, apiResult.getCode() + "");
        // ======statInterfaceLog 统计用 end======

        // ======userAccessLog mcp自用统计 start======
        // 在stataccess统计的基础上添加了返回结果和消耗时间，也是以“|”分割
//        userAccesslogger.info(statLogStr + apiResult.getCode() + "|"
//                + (System.currentTimeMillis() - starTime));
        // ======userAccessLog mcp自用统计 end======

        return apiResult;
    }

    protected void beforeExecute(ApiCommandContext context) {

    }

    protected void afterExecute(ApiCommandContext context, ApiResult apiResult) {

    }

    public abstract ApiResult onExecute(ApiCommandContext context);

}
