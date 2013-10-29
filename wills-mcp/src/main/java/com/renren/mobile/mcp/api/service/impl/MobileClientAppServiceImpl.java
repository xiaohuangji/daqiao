/**
 * $Id: MobileClientAppServiceImpl.java 7279 2013-03-25 07:56:04Z ji.sun $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.mobile.mcp.api.entity.MobileClientAppInfo;
import com.renren.mobile.mcp.api.service.MobileClientAppService;
import com.renren.mobile.mcp.utils.McpUtils;

/**
 * @author sunji
 * 
 */
public class MobileClientAppServiceImpl implements MobileClientAppService, Runnable {

    private static final Log logger = LogFactory.getLog(MobileClientAppServiceImpl.class);

    //    private Map<String, MobileClientAppInfo> appKeyAppInfoMap = null;

    private Map<Integer, MobileClientAppInfo> appIdAppInfoMap = null;

    private Map<Integer, List<String>> appAuthMap = null;

//    private AppInfoDAO appInfoDAO;
//
//    private AppMethodsWhiteListDAO appMethodsWhiteListDAO;

    public MobileClientAppServiceImpl() {
    	this.loadApp();
    }
    
    private synchronized void loadApp() {
        logger.info("loadApp start");
        long startTime = System.currentTimeMillis();

        // 加载app
        appIdAppInfoMap = new HashMap<Integer, MobileClientAppInfo>();
        appAuthMap = new HashMap<Integer, List<String>>();

//        List<MobileClientAppInfo> appInfoList = appInfoDAO.getAll();
//        if (CollectionUtils.isEmpty(appInfoList)) {
//            logger.error("MobileClientAppServiceImpl loadApp appInfoList is null");
//            return;
//        }
//
//        for (MobileClientAppInfo mcai : appInfoList) {
//            appIdAppInfoMap.put(mcai.getAppId(), mcai);
//            List<String> methods = appMethodsWhiteListDAO.getByAppId(mcai.getAppId());
//            if (CollectionUtils.isEmpty(methods)) {
//                logger.error("MobileClientAppServiceImpl loadApp methods is null");
//                continue;
//            }
//            appAuthMap.put(mcai.getAppId(), methods);
//        }

        List<String> methods = new ArrayList<String>();
        methods.add("test.cmd");
        methods.add("user.*");
        methods.add("inviteEvent.*");
        methods.add("guideEvent.*");
        methods.add("resource.*");
        methods.add("push.*");

        // web app
        MobileClientAppInfo mcaiWeb = new MobileClientAppInfo();
        mcaiWeb.setAppId(1003);
        mcaiWeb.setAppName("pm_web_1.0");
        //        mcaiIOS.setAppUrl("web");
        mcaiWeb.setSecretKey("test_web");
        appIdAppInfoMap.put(mcaiWeb.getAppId(), mcaiWeb);
        appAuthMap.put(mcaiWeb.getAppId(), methods);
        
        // android app
        MobileClientAppInfo mcaiAndroid = new MobileClientAppInfo();
        mcaiAndroid.setAppId(1001);
        mcaiAndroid.setAppName("pm_android_1.0");
        //        mcaiAndroid.setAppUrl("android");
        mcaiAndroid.setSecretKey("9f738a3934abf88b1dca8e8092043fbd");
        appIdAppInfoMap.put(mcaiAndroid.getAppId(), mcaiAndroid);
        appAuthMap.put(mcaiAndroid.getAppId(), methods);

        // IOS app
        MobileClientAppInfo mcaiIOS = new MobileClientAppInfo();
        mcaiIOS.setAppId(1002);
        mcaiIOS.setAppName("pm_ios_1.0");
        //        mcaiIOS.setAppUrl("IOS");
        mcaiIOS.setSecretKey("bf646f6f09c07e911a6239780ea1b7df");
        appIdAppInfoMap.put(mcaiIOS.getAppId(), mcaiIOS);
        appAuthMap.put(mcaiIOS.getAppId(), methods);
        
        logger.info("loadApp end timecost:" + (System.currentTimeMillis() - startTime));
    }

    @Override
    public void run() {
        this.loadApp();
    }

    @Override
    public MobileClientAppInfo getAppInfo(int appId) {
        //        if (StringUtils.isEmpty(appKey) || this.appKeyAppInfoMap == null) {
        //            return null;
        //        }
        //        return this.appKeyAppInfoMap.get(appKey);
        if (appId == 0 || this.appIdAppInfoMap == null) {
            return null;
        }
        return this.appIdAppInfoMap.get(appId);
    }

    @Override
    public boolean isAllowedApiMethod(int appId, String methodName) {
        if (this.appAuthMap == null) {
            return false;
        }
        Collection<String> apiMethods = this.appAuthMap.get(appId);
        if (apiMethods == null) {
            return false;
        }
        for (String method : apiMethods) {
            if (McpUtils.leftMatch(methodName, method)) {
                return true;
            }
        }
        return false;
    }

//    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
//        this.appInfoDAO = appInfoDAO;
//    }
//
//    public void setAppMethodsWhiteListDAO(AppMethodsWhiteListDAO appMethodsWhiteListDAO) {
//        this.appMethodsWhiteListDAO = appMethodsWhiteListDAO;
//    }

}
