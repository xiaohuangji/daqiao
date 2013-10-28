/**
 * 
 */
package com.tg.service.impl;

import org.apache.commons.lang.StringUtils;

import com.tg.constant.RedisKeyConstant;
import com.tg.model.UserPassport;
import com.tg.service.PassportService;
import com.tg.service.utils.TicketUtils;
import com.wills.redis.client.RedisClient;


/**
 * 
 * 
 * 
 * @author sunji
 * 
 */
public class PassportServiceImpl implements PassportService {
    
//    private static final Log logger = LogFactory.getLog(PassportServiceImpl.class);
            
//    private Map<String, Object> passportCache = new HashMap<String, Object>();
//    
//    private Map<Integer, String> onlineUserCache = new HashMap<Integer, String>();
	
	private static RedisClient redisClient=new RedisClient(RedisKeyConstant.USER_PASSPORT);
   
    @Override
    public void destroyTicket(String ticket) {
        if (StringUtils.isEmpty(ticket)) {
            return;
        }
        UserPassport userPassport = getPassportByTicket(ticket);
        if (userPassport != null) {
            int uid = TicketUtils.decryptTicket(ticket);
            if (uid <= 0) {
                return;
            }
//            PassportRedisCache.del(uid + "");
            redisClient.del(uid + "");
        }
        return;
    }
    
    @Override
    public UserPassport getPassportByTicket(String ticket) {
        UserPassport userPassport = null;
        if (StringUtils.isEmpty(ticket)) {
            return userPassport;
        }
        int uid = TicketUtils.decryptTicket(ticket);
        if (uid <= 0) {
            return userPassport;
        }
//        userPassport = (UserPassport) PassportRedisCache.get(uid + "");
        userPassport = redisClient.get(uid + "", UserPassport.class);
        return userPassport;
    }
    
    @Override
    public int getUserIdByTicket(String ticket) {
        int userId = 0;
        UserPassport userPassport = getPassportByTicket(ticket);
        if (userPassport == null) {
            return userId;
        }
        userId = userPassport.getUserId();
        return userId;
    }

    @Override
    public String createTicket(UserPassport userPassport) {
        String ticket = null;
        if (userPassport == null || userPassport.getUserId() == 0) {
            return ticket;
        }
        ticket = TicketUtils.generateTicket(userPassport.getUserId());
        userPassport.setTicket(ticket);
        if (!StringUtils.isEmpty(ticket)) {
//            PassportRedisCache.set(userPassport.getUserId() + "", userPassport);
            redisClient.set(userPassport.getUserId() + "", userPassport);
        }
        return ticket;
    }
    
    @Override
    public String getTicketByUserId(int userId) {
        String ticket = null;
        
        if (userId != 0) {
        	
        	
//            UserPassport userPassport = (UserPassport) PassportRedisCache.get(userId + "");
            UserPassport userPassport = redisClient.get(userId+"", UserPassport.class);
            if (userPassport != null) {
                ticket = userPassport.getTicket();
                int uid = TicketUtils.decryptTicket(ticket);
                if (uid <= 0) {
                    return null;
                }
                if (uid != userId) {
                    return null;
                }
            }
        }
        return ticket;
    }
    
//    @Override
//    public boolean destroyTicket(String ticket) {
//        boolean rt = false;
//        if (StringUtils.isEmpty(ticket)) {
//            return rt;
//        }
//        UserPassport userPassport = getPassportByTicket(ticket);
//        if (userPassport != null) {
//            Long delCnt = PassportRedisCache.del(ticket);
//            if (delCnt != null && delCnt > 0L) {
//                rt = true;
//            }
//            if (rt) {
//                delCnt = PassportRedisCache.del(userPassport.getUserId() + "");
//            }
//        }
//        return rt;
//    }
//    
//    @Override
//    public UserPassport getPassportByTicket(String ticket) {
//        UserPassport userPassport = null;
//        if (StringUtils.isEmpty(ticket)) {
//            return userPassport;
//        }
//        userPassport = (UserPassport) PassportRedisCache.get(ticket);
//        return userPassport;
//    }
//    
//    @Override
//    public int getUserIdByTicket(String ticket) {
//        int userId = 0;
//        UserPassport userPassport = getPassportByTicket(ticket);
//        if (userPassport == null) {
//            return userId;
//        }
//        userId = userPassport.getUserId();
//        return userId;
//    }
//
//    @Override
//    public String createTicket(UserPassport userPassport) {
//        String ticket = null;
//        if (userPassport == null || userPassport.getUserId() == 0) {
//            return ticket;
//        }
//        ticket = TicketUtils.generateTicket();
//        if (!StringUtils.isEmpty(ticket)) {
//            PassportRedisCache.set(ticket, userPassport);
//            PassportRedisCache.set(userPassport.getUserId() + "", ticket);
//        }
//        return ticket;
//    }
//    
//    @Override
//    public String getTicketByUserId(int userId) {
//        String ticket = null;
//        if (userId != 0) {
//            ticket = (String) PassportRedisCache.get(userId + "");
//        }
//        return ticket;
//    }

}
