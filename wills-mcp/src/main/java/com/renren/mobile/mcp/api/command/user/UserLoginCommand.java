package com.renren.mobile.mcp.api.command.user;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.model.UserInfo;
import com.tg.model.UserPassport;
import com.tg.service.PassportService;
import com.tg.service.UserService;

public class UserLoginCommand extends AbstractApiCommand {

	private static final Log logger = LogFactory.getLog(UserLoginCommand.class);

    private UserService userService;
    
    private PassportService passportService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
       // int userId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();

        String mobile=stringParams.get("mobile");
        String password=stringParams.get("password");
        int appId = context.getMcpAppInfo().getAppId();
        //Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            UserInfo result = userService.loginExt(mobile, password);
            McpUtils.rpcTimeCost(t, "user.login");
            if (result!=null) {
                    // 登陆成功返回userId
                   // UserView userView = loginStatusView.getUserInfo();
                    context.setUserId(result.getUserId());

                    UserPassport userPassport = new UserPassport();
                    userPassport.setUserId(result.getUserId());
                    userPassport.setAppId(appId);
                    userPassport.setCreateTime(System.currentTimeMillis());
                    String userSecretKey = McpUtils.generateSecretKey();
                    userPassport.setUserSecretKey(userSecretKey);
                    String ticket = passportService.createTicket(userPassport);
                    logger.info("ticket: " + ticket);
                    if (StringUtils.isEmpty(ticket)) {
                            return new ApiResult(ApiResultCode.E_BIZ_LOGIN_FAILED);
                    }   
                    userPassport.setTicket(ticket);
                    result.setUserPassport(userPassport);
                    return new ApiResult(ApiResultCode.SUCCESS, result);
                }else {
                    return new ApiResult(ApiResultCode.E_BIZ_LOGIN_FAILED);
                }  

        } catch (Exception e) {
            // 异常记录日志， 返回错误信息
            logger.error("RPC error ", e);
            apiResult = new ApiResult(ApiResultCode.E_SYS_RPC_ERROR);
            return apiResult;
        }
    }

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setPassportService(PassportService passportService) {
		this.passportService = passportService;
	}
	
	
}
