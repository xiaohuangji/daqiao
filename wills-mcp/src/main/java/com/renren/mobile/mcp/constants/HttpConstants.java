/**
 * $Id: HttpConstants.java 6685 2013-03-20 12:31:36Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.constants;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 和http请求相关的常量，注意添加参数的时候，要改变不需要参加sig加密的参数
 * 
 * @author sunji
 * 
 */
public final class HttpConstants {

    public final static String USER_AGENT = "HTTP_REQUEST_USER_AGENT";

    public final static String CLIENT_IP = "HTTP_REQUEST_CLIENT_IP";

    public final static String LANGUAGE = "HTTP_REQUEST_LANGUAGE";

    public final static String PARAM_APP_ID = "app_id";

    public final static String PARAM_VER = "v";

    public final static String PARAM_CMD_METHOD = "cmd_method";

    public final static String PARAM_TICKET = "t";

    public final static String PARAM_ACCESS_TOKEN = "access_token";

    public final static String PARAM_SIG = "sig";

    public final static String PARAM_FORMAT = "format";

    public final static String PARAM_DATA_TYPE = "gz";

    // 不需要参加sig加密的参数
    public final static String[] NOT_ENCRYPTED_PARAMS = { USER_AGENT, CLIENT_IP, LANGUAGE,
            PARAM_SIG, PARAM_CMD_METHOD };

    public static final String FORMAT_JSON = "json";

    public final static String DEFAULT_FORMAT = FORMAT_JSON;

    public final static String DATA_TYPE_COMPRESSION = "compression";

    public final static String PARAM_CLIENT_INFO = "client_info";

    public final static String MISC = "misc";

    public final static Locale DEFAULT_LANGUAGE = Locale.CHINA;

    public final static Set<String> platformParams = new HashSet<String>(); // 平台参数的key

    static {
        platformParams.add(USER_AGENT);
        platformParams.add(CLIENT_IP);
        platformParams.add(LANGUAGE);
        platformParams.add(PARAM_APP_ID);
        platformParams.add(PARAM_VER);
        platformParams.add(PARAM_CMD_METHOD);
        platformParams.add(PARAM_TICKET);
        platformParams.add(PARAM_ACCESS_TOKEN);
        platformParams.add(PARAM_SIG);
        platformParams.add(PARAM_FORMAT);
        platformParams.add(PARAM_DATA_TYPE);
        platformParams.add(PARAM_CLIENT_INFO);
    }
}
