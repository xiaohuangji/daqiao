package com.tg.solr;

import org.apache.solr.client.solrj.beans.Field;

/** 
 * @author xingxing.feng@renren-inc.com
 * @version 2013-3-11上午9:47:36
 * readme
 */
public class User4Solr {

    /**
     * user_id solr中需要id表示唯一的字段，所以起名id
     */
    @Field
    private int id;

    /**
     * nickname
     */
    @Field
    private String userName;

    /**
     * 性别
     */
    @Field
    private int gender;
    
    /**
     * 手机号
     */
    @Field
    private String mobile;
    
    /**
     * 城市代号
     */
    @Field
    private int city;
    
    /**
     * 经纬度信息 "lat,lon"的组合,默认表示没传，转到南极点
     */
    @Field
    private String position="0,-90";
    
    /**
     * 距离信息。只在位置搜索时返回的结果中被填充
     */
    @Field
    private double dist=0.0;
    
    /**
     * 擅长景点
     */
    @Field
    private String goodAtScenic;
    
    /**
     * 用户类型
     */
    @Field
    private int userType;
    
    /**
     * 成为导游时间
     */
    @Field
    private int beGuideYear;
    
    /**
     * 生日
     */
    @Field
    private long birthday;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public String getGoodAtScenic() {
		return goodAtScenic;
	}

	public void setGoodAtScenic(String goodAtScenic) {
		this.goodAtScenic = goodAtScenic;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getBeGuideYear() {
		return beGuideYear;
	}

	public void setBeGuideYear(int beGuideYear) {
		this.beGuideYear = beGuideYear;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}
	
}
