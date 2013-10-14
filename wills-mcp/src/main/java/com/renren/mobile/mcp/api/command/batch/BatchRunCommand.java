///**
// * $Id: BatchRunCommand.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
// * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
// */
//package com.renren.mobile.mcp.api.command.batch;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.net.URLCodec;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.util.Assert;
//
//import com.renren.intl.soundsns.model.UserStatus;
//import com.renren.mobile.mcp.api.command.AbstractApiCommand;
//import com.renren.mobile.mcp.api.command.ApiCommand;
//import com.renren.mobile.mcp.api.command.user.UserLoginCommand;
//import com.renren.mobile.mcp.api.entity.ApiCommandContext;
//import com.renren.mobile.mcp.api.entity.ApiResult;
//import com.renren.mobile.mcp.api.entity.ApiResultCode;
//import com.renren.mobile.mcp.api.service.CommandLookupService;
//import com.renren.mobile.mcp.api.service.MobileClientAppService;
//import com.renren.mobile.mcp.constants.HttpConstants;
//
///**
// * 批量处理接口
// * 
// * @author ji.sun
// */
//public class BatchRunCommand extends AbstractApiCommand implements InitializingBean {
//
//    private static final Log logger = LogFactory.getLog(BatchRunCommand.class);
//
//    private final static String PARAM_METHOD_FEED = "batch_method_feed";
//
//    private final static String PARAM_METHOD = "method";
//
//    private final static ObjectMapper jsonObjectMapper = new ObjectMapper();// can reuse, share globally
//
//    private static final URLCodec urlCodec = new URLCodec();
//
//    private MobileClientAppService mobileClientAppService;
//
//    private CommandLookupService commandLookupService;
//
//    @Override
//    public ApiResult onExecute(ApiCommandContext context) {
//
//        int appId = context.getMcpAppInfo().getAppId();
//
//        int userId = context.getUserId();
//
//        // 分离参数
//        String methodFeed = context.getStringParams().get(PARAM_METHOD_FEED);
//
//        ArrayList<String> methodFeeds = null;
//
//        try {
//            methodFeeds = jsonObjectMapper.readValue(urlCodec.decode(methodFeed),
//                    new TypeReference<ArrayList<String>>() {});
//        } catch (Exception e) {
//            logger.error("BatchRunCommand", e);
//        }
//
//        if (methodFeed == null) {
//            return new ApiResult(ApiResultCode.E_SYS_PARAM);
//        }
//
//        // TODO：如果需要登录，对user用户身份的检查，例如是否封禁等等
//        //        if (userId > 0) {
//        //            
//        //        }
//
//        Map<String, Object> batchResultMap = new HashMap<String, Object>();
//        for (String methodAttr : methodFeeds) {
//            Map<String, String> strParamMap = this.parseCommandParamMap(methodAttr);
//            String methodValue = strParamMap.get(PARAM_METHOD);
//
//            if (context.getStringParams().get(HttpConstants.PARAM_CLIENT_INFO) != null) {
//                // 加上client_info
//                strParamMap.put(HttpConstants.PARAM_CLIENT_INFO,
//                        context.getStringParams().get(HttpConstants.PARAM_CLIENT_INFO));
//            }
//
//            if (context.getStringParams().get(HttpConstants.CLIENT_IP) != null) {
//                // 加上client_ip
//                strParamMap.put(HttpConstants.CLIENT_IP,
//                        context.getStringParams().get(HttpConstants.CLIENT_IP));
//            }
//
//            // check methodValue
//            if (StringUtils.isEmpty(methodValue)) {
//                return new ApiResult(ApiResultCode.E_SYS_PARAM);
//            }
//
//            methodValue = this.methodNameFix(methodValue);
//            // check method is authzed
//            if (!mobileClientAppService.isAllowedApiMethod(appId, methodValue)) {
//                batchResultMap.put(methodValue, new ApiResult(ApiResultCode.E_SYS_PERMISSION_DENY));
//                continue;
//            }
//
//            // TODO: 限制流量
//
//            // 获得具体的command
//            ApiCommand cmd = commandLookupService.lookupApiCommand(methodValue);
//            if (cmd == null) {
//                batchResultMap.put(methodValue, new ApiResult(ApiResultCode.E_SYS_UNKNOWN_METHOD));
//                continue;
//            }
//            // check cycle call
//            if (cmd == this) {
//                batchResultMap.put(methodValue, new ApiResult(
//                        ApiResultCode.E_BIZ_BATCH_RUN_CYCLE_CALL));
//                continue;
//            }
//            // check if need to login
//            if (userId == 0 && commandLookupService.isNeedLogin(methodValue)) {
//                batchResultMap.put(methodValue, new ApiResult(ApiResultCode.E_SYS_INVALID_TICKET));
//                continue;
//            }
//
//            // 构造ApiCommandContext
//            Map<String, String> singleStrParam = new HashMap<String, String>();
//            copyStringParam(strParamMap, singleStrParam);
//            String ticket = strParamMap.get(HttpConstants.PARAM_TICKET);
//            //            if (StringUtils.isEmpty(ticket)) {
//            //                ticket = context.getTicket();
//            //            }
//            ApiCommandContext acc = new ApiCommandContext(context.getMcpAppInfo(), userId,
//                    singleStrParam, context.getBinaryParams(), ticket, context.getSecretKey());
//            acc.setMethodName(methodValue); // 调用execute打印日志时能拿到接口名称
//            // 执行命令
//            ApiResult apiRt = cmd.execute(acc);
//
//            //            if (cmd instanceof UserLoginCommand && apiRt.getCode() == ApiResultCode.SUCCESS) {
//            //                JSONObject jo = (JSONObject) apiRt.getData();
//            //                userId = jo.getInt("uid");
//            //            }
//            //            if (apiRt.getCode() == ApiResultCode.SUCCESS) {
//            //                batchResultMap.put(methodValue, apiRt.getData());
//            //            } else {
//            //                batchResultMap.put(methodValue, apiRt);
//            //            }
//
//            if (cmd instanceof UserLoginCommand && apiRt.getCode() == ApiResultCode.SUCCESS) {
//                @SuppressWarnings("unchecked")
//                Map<String, Object> loginMap = (Map<String, Object>) apiRt.getData();
//                if (loginMap != null) {
//                    UserStatus us = (UserStatus) loginMap.get("userStatus");
//                    if (us != null) {
//                        userId = us.getUserId();
//                    }
//                }
//            }
//            if (apiRt.getCode() == ApiResultCode.SUCCESS) {
//                batchResultMap.put(methodValue, apiRt.getData());
//            } else {
//                batchResultMap.put(methodValue, apiRt);
//            }
//
//        }
//
//        //        JSONObject jo = JSONObject.fromObject(batchResultMap);
//        //        return new ApiResult(ApiResultCode.SUCCESS, jo);
//
//        return new ApiResult(ApiResultCode.SUCCESS, batchResultMap);
//    }
//
//    private Map<String, String> copyStringParam(Map<String, String> from, Map<String, String> to) {
//        if (from == null || from.size() == 0 || to == null) {
//            return to;
//        }
//        for (String fromKey : from.keySet()) {
//            to.put(fromKey, from.get(fromKey));
//        }
//        return to;
//    }
//
//    /**
//     * 请求的方法名兼容.和/
//     * 
//     * @param methodName
//     * @return
//     */
//    private String methodNameFix(String methodName) {
//        return methodName.replace('/', '.');
//    }
//
//    /**
//     * 解析参数
//     * 
//     * @param methodAttr
//     * @return
//     */
//    private Map<String, String> parseCommandParamMap(String methodAttr) {
//        Map<String, String> paramMap = new HashMap<String, String>();
//        String[] params = StringUtils.split(methodAttr, '&');
//        if (params == null) {
//            return paramMap;
//        }
//        for (String param : params) {
//            String[] kv = StringUtils.split(param, '=');
//            if (kv.length == 2) {
//                String key = kv[0];
//                String value = kv[1];
//                try {
//                    value = urlCodec.decode(value);
//                } catch (DecoderException e) {
//                    logger.error("parseCommandParamMap(String)", e);
//                }
//                paramMap.put(key, value);
//            }
//        }
//        return paramMap;
//    }
//
//    public void setMobileClientAppService(MobileClientAppService mobileClientAppService) {
//        this.mobileClientAppService = mobileClientAppService;
//    }
//
//    public void setCommandLookupService(CommandLookupService commandLookupService) {
//        this.commandLookupService = commandLookupService;
//    }
//
//    public static void main(String[] a) {
//        System.out.println(System.currentTimeMillis());
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(mobileClientAppService, "mobileClientAppService is required");
//        Assert.notNull(commandLookupService, "commandLookupService is required");
//    }
//}
