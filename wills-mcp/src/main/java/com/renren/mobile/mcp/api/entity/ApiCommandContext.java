/**
 * $Id: ApiCommandContext.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.renren.mobile.mcp.constants.HttpConstants;
import com.renren.mobile.mcp.utils.McpUtils;

/**
 * @author ji.sun
 */
public class ApiCommandContext implements Serializable {

    private static final Log logger = LogFactory.getLog(ApiCommandContext.class);

    private static final long serialVersionUID = 1L;

    private long beginTime;

    private String methodName;

    private int userId;

    /** 客户端接入授权信息 */
    private MobileClientAppInfo mcpAppInfo;

    /** 客户端数据采集 */
    private ClientInfo clientInfo;

    /** current secret key */
    private String secretKey;

    private String ticket;

    //the input parameters map
    private Map<String, String> stringParams;

    //multipart post binary data
    private Map<String, MultipartFile> binaryParams;

    //    public ApiCommandContext(MobileClientAppInfo mcpAppInfo, User user,
    //            Map<String, String> stringParams) {
    //        this(mcpAppInfo, user, stringParams, null, null, null);
    //    }

    public ApiCommandContext(MobileClientAppInfo mcpAppInfo, int userId,
            Map<String, String> stringParams, Map<String, MultipartFile> binaryParams,
            String ticket, String secretKey) {
        this.beginTime = System.currentTimeMillis();
        this.mcpAppInfo = mcpAppInfo;
        this.userId = userId;
        this.stringParams = stringParams;
        this.binaryParams = binaryParams;
        this.ticket = ticket;
        this.secretKey = secretKey;
        this.methodName = stringParams.get(HttpConstants.PARAM_CMD_METHOD);
        String strClientInfo = stringParams.get(HttpConstants.PARAM_CLIENT_INFO);
        if (StringUtils.isNotBlank(strClientInfo)) {
            try {
                this.clientInfo = McpUtils.toObjectSafe(strClientInfo, ClientInfo.class);
            } catch (Exception e) {
                logger.error("ApiCommandContext", e);
                String sig = stringParams.get("sig");
                logger.error(String.format("ClientInfo sig[%s] error: %s", sig, strClientInfo));
            }
        }
        if (clientInfo == null) {
            clientInfo = new ClientInfo();
        }
        //        // 统计用,客户端传递的misc参数，里面包含了  htf,在线OR离线,联网状态 这三部分内容，需要解析该参数
        //        String strMISC = stringParams.get(HttpConstants.MISC);
        //        if (StringUtils.isNotBlank(strMISC)) {
        //            try {
        //                String[] miscArray = strMISC.split(",");
        //                if (StringUtils.isNotBlank(miscArray[0])) {
        //                    this.stringParams.put("htf", miscArray[0]);
        //                }
        //            } catch (Exception e) {
        //                logger.error("ApiCommandContext", e);
        //            }
        //        }
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MobileClientAppInfo getMcpAppInfo() {
        return mcpAppInfo;
    }

    public void setMcpAppInfo(MobileClientAppInfo mcpAppInfo) {
        this.mcpAppInfo = mcpAppInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Map<String, String> getStringParams() {
        return stringParams;
    }

    public void setStringParams(Map<String, String> stringParams) {
        this.stringParams = stringParams;
    }

    public Map<String, MultipartFile> getBinaryParams() {
        return binaryParams;
    }

    public void setBinaryParams(Map<String, MultipartFile> binaryParams) {
        this.binaryParams = binaryParams;
    }

    @Override
    public String toString() {
        return "ApiCommandContext [beginTime=" + beginTime + ", methodName=" + methodName
                + ", userId=" + userId + ", mcpAppInfo=" + mcpAppInfo + ", clientInfo="
                + clientInfo + ", secretKey=" + secretKey + ", ticket=" + ticket
                + ", stringParams=" + stringParams + ", binaryParams=" + binaryParams + "]";
    }

}
