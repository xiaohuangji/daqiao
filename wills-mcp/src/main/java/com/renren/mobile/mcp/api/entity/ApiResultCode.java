/**
 * $Id: ApiResultCode.java 4782 2013-03-06 10:04:35Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

/**
 * "E"表示“error”(错误)；"SYS"表示"system"（系统平台级的）;"biz"表示“business”（业务级的）
 * 
 * @author sunji
 * 
 */
public final class ApiResultCode {

    // 成功
    public final static int SUCCESS = 0;

    // <1000的为系统错误
    public final static int E_SYS_UNKNOWN = 1;//unknown

    public final static int E_SYS_PARAM = 2;//invalid or unkown parameter

    public final static int E_SYS_PERMISSION_DENY = 3;//permission deny

    public final static int E_SYS_REQUEST_TOO_FAST = 4;//user request is too fast

    public final static int E_SYS_ANTISPAM = 5;//antispam相关

    public final static int E_SYS_INVALID_APP_ID = 6;//invalid appid

    public final static int E_SYS_INVALID_TICKET = 7;//invalid tikect

    public final static int E_SYS_INVALID_SIG = 8;//invalid sig

    public final static int E_SYS_INVALID_VERSION = 9;//invalid version

    public final static int E_SYS_UNKNOWN_METHOD = 10;//unknown method

    public final static int E_SYS_UNKNOWN_RESULT_FORMAT = 11;//unknown results format

    public final static int E_SYS_RPC_ERROR = 12;// RPC error

    // >1000且<10000为业务级错误

    public final static int E_BIZ_LOGIN_FAILED = 1001;//login failed:no reason
    
  

    public final static int E_BIZ_LOGIN_NO_ACCOUNT = 1002;//login failed:NO_ACCOUNT

    public final static int E_BIZ_LOGIN_WRONG_PASSWORD = 1003;//login failed:WRONG_PASSWORD

    public final static int E_BIZ_SAFE_CAPTCHA = 1004; //需要图形验证码

    public final static int E_BIZ_PHOHE_NUMBER_USED = 1005; //该手机号已使用，请更换手机号绑定

    public final static int E_BIZ_USER_BIND_UNVERIFIED = 1006; //用户尚未进行绑定验证操作

    public final static int E_BIZ_SAFE_VERIFY_CODE_INVALID = 1007; //验证码错误

    public final static int E_BIZ_BATCH_RUN_CYCLE_CALL = 1008; //批处理时，循环调用了

    public final static int E_BIZ_REGISTER_FAILED = 1009;//注册失败
    

    public final static int E_BIZ_ACCESS_SERVER_HOST_NULL = 1010;//获取push server时得到空

    public final static int E_BIZ_NICKNAME_UN_FORMAT = 1011;//nickname less than 6 or nickname more than 20
    
    public final static int E_BIZ_LOGIN_USER_OR_PASSWORD_WRONG = 1012;//用户名或密码错误

	public static final int E_BIZ_REGISTER_VERIFI_CODE_EXPIRE = 1013;//register时验证码过期
}
