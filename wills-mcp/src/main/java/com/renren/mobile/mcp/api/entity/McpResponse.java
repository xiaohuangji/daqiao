/**
 * $Id: McpResponse.java 4782 2013-03-06 10:04:35Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.renren.mobile.mcp.constants.HttpConstants;
import com.renren.mobile.mcp.constants.McpConstants;
import com.renren.mobile.mcp.utils.McpUtils;

/**
 * 
 * @author sunji
 * 
 */
public class McpResponse {

    private static final Log logger = LogFactory.getLog(McpResponse.class);

    private HttpServletResponse response;

    private String format;

    private boolean requireCompression;

    private DelegatingMessageSource messageSource;

    private String language;

    public McpResponse(HttpServletResponse response, String format, boolean requireCompression,
            ResourceBundleMessageSource messageSource, String language) {
        super();
        this.response = response;
        this.format = format;
        this.requireCompression = requireCompression;
    }

    public McpResponse(HttpServletResponse response) {
        this(response, HttpConstants.DEFAULT_FORMAT, false, null, HttpConstants.DEFAULT_LANGUAGE
                .toString());
    }

    /**
     * 输出
     * 
     * @param apiResult
     * @throws IOException
     */
    public void write(ApiResult apiResult) {
        if (apiResult == null) {
            return;
        }

        OutputStream os = null;
        try {
            // 填充提示或错误消息
            if (apiResult.getCode() != 0) {
                this.fillMessage(apiResult);
            }

            this.writeHttpHeader();

            os = response.getOutputStream();

            if (requireCompression) {
                os = new GZIPOutputStream(os);
            }
            if (HttpConstants.FORMAT_JSON.equalsIgnoreCase(format)) {
                // json格式
                this.writeJSON(apiResult, os);
            } else {
                // 默认json格式
                this.writeJSON(apiResult, os);
            }

            os.flush();
        } catch (IOException e) {
            logger.error("McpResponse.write", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("McpResponse.write", e);
            }
        }
    }

    private void writeJSON(ApiResult apiResult, OutputStream os)
            throws UnsupportedEncodingException, IOException {

        if (apiResult.getCode() == 0) {
            Object obj = apiResult.getData();
            String rt = McpUtils.buildJSONResult(obj);
            if (logger.isDebugEnabled()) {
                logger.debug(String
                        .format("[%s]:[cmd result ok:%s]", this.getClass().getName(), rt));
            }
            byte[] ob = rt == null ? null : rt.getBytes(CharEncoding.UTF_8);
            os.write(ob);
        } else {
            String rt = McpUtils.gson.toJson(apiResult);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("[%s]:[cmd result err:%s]", this.getClass().getName(),
                        rt));
            }
            byte[] ob = rt == null ? null : rt.getBytes(CharEncoding.UTF_8);
            os.write(ob);
        }
        
    }

    /**
     * write http header
     * 
     * @param response
     * @param format
     * @param requireCompression
     */
    private void writeHttpHeader() {

        try {
            response.setStatus(HttpServletResponse.SC_OK);
            if (StringUtils.equalsIgnoreCase(format, HttpConstants.FORMAT_JSON)) {// json
                if (requireCompression) {
                    response.setContentType("application/json-gz");
                    response.setHeader("content-encoding", "gzip");
                } else {
                    response.setContentType("text/plain;charset=UTF-8");
                }
            }
        } catch (Exception e) {
            logger.error("writeHttpHeader", e);
        }

    }

    private void fillMessage(ApiResult apiResult) {

        try {
            if (messageSource == null || StringUtils.isEmpty(language) || language.length() < 3
                    || !language.contains("_")) {
                return;
            }
            String[] tmp = language.split("_");

            String msg = messageSource.getMessage(McpConstants.API_RESULT_MESSAGE_PREFIX
                    + apiResult.getCode(), null, new Locale(tmp[0], tmp[1]));
            apiResult.setData(msg);
        } catch (Exception e) {
            logger.error("getMessage", e);
        }

        return;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isRequireCompression() {
        return requireCompression;
    }

    public void setRequireCompression(boolean requireCompression) {
        this.requireCompression = requireCompression;
    }

    public DelegatingMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(DelegatingMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
