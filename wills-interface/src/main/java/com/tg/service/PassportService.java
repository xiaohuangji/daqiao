/**
 * 
 */
package com.tg.service;

import com.tg.model.UserPassport;



/**
 * @author sunji
 *
 */
public interface PassportService {
    
    /**
     * 销毁票
     * @param ticket
     * @return
     */
    public void destroyTicket(String ticket);
    
    /**
     * 通过票获得userId
     * @return
     */
    public int getUserIdByTicket(String ticket);
    
    /**
     * 生成票
     * @param userId
     * @param accountOrigin
     * @param expirePeriod
     * @return
     */
    public String createTicket(UserPassport userPassport);
    
    /**
     * 通过票获得passport
     * @param ticket
     * @return
     */
    public UserPassport getPassportByTicket(String ticket);
    
    /**
     * 通过userId换票，同时可以检查此用户在不在线
     * @param userId
     * @return
     */
    public String getTicketByUserId(int userId);
    
}
