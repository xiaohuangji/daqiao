/**
 * $Id: SigCacheImpl.java 11003 2013-04-23 03:56:55Z ji.sun $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.mobile.mcp.api.service.SigCache;
import com.wills.redis.client.RedisClient;

/**
 * 缓存sig，用于防重发机制
 * 
 * @author sunji
 * 
 */
public class SigCacheImpl implements SigCache {

    private static final Log logger = LogFactory.getLog(SigCacheImpl.class);
    
    private static RedisClient redisClient=new RedisClient("mcp_t_sig_");

    private String prefix = "mcp_t_sig_";

//    public SigCacheImpl() throws ClusterConnException {
//        ConfigurationService cs = ConfigurationService.getInstance();
//        String zkIp = cs.getzkConfigProperty("zk_host");
//        //        System.out.println(zkIp);
//        this.redisClient = new RedisClusterPoolClient("cache_cluster", zkIp);
//    }

    //    @Override
    //    public Object get(String key) {
    //        try {
    //            if (redisClient == null || StringUtils.isEmpty(key)) {
    //                return null;
    //            }
    //            byte[] btArr = redisClient.get((prefix + key).getBytes("UTF-8"));
    //            if (btArr != null) {
    //                return TicketUtils.unserialize(btArr);
    //            }
    //        } catch (Exception e) {
    //            logger.error("SigCacheImpl get", e);
    //        }
    //        return null;
    //    }

    @Override
    public String get(String key) {
        try {
            if (redisClient == null || StringUtils.isEmpty(key)) {
                return null;
            }
            return redisClient.get(key, String.class);
        } catch (Exception e) {
            logger.error("SigCacheImpl get", e);
        }
        return null;
    }

    //    @Override
    //    public String set(String key, Object value) {
    //        try {
    //            if (redisClient == null || StringUtils.isEmpty(key) || value == null) {
    //                return null;
    //            }
    //
    //            return redisClient.set((prefix + key).getBytes("UTF-8"), TicketUtils.serialize(value));
    //        } catch (Exception e) {
    //            logger.error("SigCacheImpl set", e);
    //        }
    //        return null;
    //    }

    @Override
    public void set(String key, String value, int seconds) {
        try {
            if (redisClient == null || StringUtils.isEmpty(key) || value == null) {
                return;
            }

            redisClient.setex(key, value, seconds);
        } catch (Exception e) {
            logger.error("SigCacheImpl set", e);
        }
        return;
    }

    //    @Override
    //    public Long del(String key) {
    //        try {
    //            if (redisClient == null || StringUtils.isEmpty(key)) {
    //                return null;
    //            }
    //            return redisClient.del((prefix + key).getBytes("UTF-8"));
    //        } catch (Exception e) {
    //            logger.error("SigCacheImpl del", e);
    //        }
    //        return null;
    //    }

    @Override
    public void del(String key) {
        try {
            if (redisClient == null || StringUtils.isEmpty(key)) {
                return;
            }
            redisClient.del(key);
        } catch (Exception e) {
            logger.error("SigCacheImpl del", e);
        }
        return;
    }
}
