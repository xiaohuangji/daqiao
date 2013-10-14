package com.renren.mobile.mcp.api.command.resource;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.renren.mobile.mcp.api.command.AbstractApiCommand;
import com.renren.mobile.mcp.api.entity.ApiCommandContext;
import com.renren.mobile.mcp.api.entity.ApiResult;
import com.renren.mobile.mcp.api.entity.ApiResultCode;
import com.renren.mobile.mcp.utils.McpUtils;
import com.tg.service.ResourceService;
import com.tg.service.UserService;

public class ResourceUploadResourceCommand extends AbstractApiCommand{

	private static final Log logger = LogFactory.getLog(ResourceUploadResourceCommand.class);

    private ResourceService resourceService;

    @Override
    public ApiResult onExecute(ApiCommandContext context) {

        // 取参数
        int userId = context.getUserId();
        Map<String, String> stringParams = context.getStringParams();
        Map<String, MultipartFile> binaryParams = context.getBinaryParams();
        
        String suffix=stringParams.get("suffix");

        InputStream is = null;

        try {
            is = binaryParams.get("file").getInputStream();
        } catch (Exception e) {
            logger.error("getInputStream failed", e);
            return new ApiResult(ApiResultCode.E_SYS_PARAM);
        }
        
        Object result = null;
        ApiResult apiResult = null;

        // 执行RPC调用       
        try {
            long t = System.currentTimeMillis();
            result =resourceService.uploadResource(InputStreamToByte(is), suffix);
            
            McpUtils.rpcTimeCost(t, "resource.uploadResource");
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

	private static  byte[] InputStreamToByte(InputStream is) throws IOException {   
	    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();   
	   int ch;   
	   while ((ch = is.read()) != -1) {   
	     bytestream.write(ch);   
	    }   
	   byte imgdata[] = bytestream.toByteArray();   
	    bytestream.close();   
	   return imgdata;   
	   }

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	} 
}
