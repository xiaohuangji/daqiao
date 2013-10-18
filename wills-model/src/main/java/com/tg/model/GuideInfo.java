package com.tg.model;

import java.io.Serializable;

public class GuideInfo  extends UserInfo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7854326296582808995L;

	/**
	 * 评价得分
	 */
	private int evaluateScore=0;
	
	/**
	 * 评价次数
	 */
	private int evaluateCount=0;
	
	/**
	 * 擅长景区
	 */
	private String goodAtScenic;
	
	/**
	 * 生日
	 */
	private long birthday;
	
	/**
	 * 领证年份
	 */
	private int beGuideYear;
	
	/**
	 * 导游证图片地址
	 */
	private String guideCardUrl;
	
	/**
	 * 导游证id
	 */
	private String guideCardId;
	
	/**
	 * 所在经纬度
	 */
	private String location;
	
	/**
	 * 城市代号
	 */
	private int city;
	
	/**
	 * 导游状态，标识正常，审核中，冻结等
	 */
	private int status;

	public int getEvaluateScore() {
		return evaluateScore;
	}

	public void setEvaluateScore(int evaluateScore) {
		this.evaluateScore = evaluateScore;
	}

	public int getEvaluateCount() {
		return evaluateCount;
	}

	public void setEvaluateCount(int evaluateCount) {
		this.evaluateCount = evaluateCount;
	}

	public String getGoodAtScenic() {
		return goodAtScenic;
	}

	public void setGoodAtScenic(String goodAtScenic) {
		this.goodAtScenic = goodAtScenic;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public int getBeGuideYear() {
		return beGuideYear;
	}

	public void setBeGuideYear(int beGuideYear) {
		this.beGuideYear = beGuideYear;
	}

	public String getGuideCardUrl() {
		return guideCardUrl;
	}

	public void setGuideCardUrl(String guideCardUrl) {
		this.guideCardUrl = guideCardUrl;
	}

	public String getGuideCardId() {
		return guideCardId;
	}

	public void setGuideCardId(String guideCardId) {
		this.guideCardId = guideCardId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GuideInfo [evaluateScore=" + evaluateScore + ", evaluateCount="
				+ evaluateCount + ", goodAtScenic=" + goodAtScenic
				+ ", birthday=" + birthday + ", beGuideYear=" + beGuideYear
				+ ", guideCardUrl=" + guideCardUrl + ", guideCardId="
				+ guideCardId + ", location=" + location + ", city=" + city
				+ ", status=" + status + "]";
	}
	
	
}
