/**
 * 
 */
package com.tg.model;

import java.io.Serializable;

/**
 * @author sunji
 * 
 */
public class UserPassport implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;

    private String ticket;

    // 用户帐号
    private String account;

    // 接入方唯一标识
    private int appId;

    // 账户来源
    private int accountOrigin;

    // 第三方token
    private String thirdPartyToken;

    // 创建时间
    private long createTime;

    // 用户的密钥
    private String userSecretKey;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountOrigin() {
        return accountOrigin;
    }

    public void setAccountOrigin(int accountOrigin) {
        this.accountOrigin = accountOrigin;
    }

    public String getThirdPartyToken() {
        return thirdPartyToken;
    }

    public void setThirdPartyToken(String thirdPartyToken) {
        this.thirdPartyToken = thirdPartyToken;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    @Override
    public String toString() {
        return "UserPassport [userId=" + userId + ", ticket=" + ticket + ", account=" + account
                + ", appId=" + appId + ", accountOrigin=" + accountOrigin + ", thirdPartyToken="
                + thirdPartyToken + ", createTime=" + createTime + ", userSecretKey="
                + userSecretKey + "]";
    }

}
