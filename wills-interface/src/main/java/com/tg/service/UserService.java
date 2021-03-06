package com.tg.service;

import java.util.List;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;

public interface UserService {

	/**
	 * 获取验证码，系统将下发验证码到手机
	 * @param mobile
	 * @return
	 */
	public int getVerifyCode(String mobile);
	
	/**
	 * 注册
	 * @param mobile
	 * @param password
	 * @param name
	 * @return
	 */
	public int register(String mobile,String password,String verifyCode,String name,int gender);
	
	public UserInfo registerExt(String mobile,String password,String verifyCode,String name,int gender);
	/**
	 * 登陆
	 * @param mobile
	 * @param password
	 * @return
	 */
	public int login(String mobile,String password);
	
	public UserInfo loginExt(String mobile,String password);
	
	/**
	 * 修改密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public int changePassword(int userId,String oldPassword,String newPassword);
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfo(int userId);
	
	/**
	 * 指量获取用户信息
	 * @param ids
	 * @return
	 */
	public List<UserInfo> getUserInfos(List<Integer> ids);
	
	/**
	 * 指获取导游数据。
	 * @param ids
	 * @return
	 */
	public List<GuideInfo> getGuideInfos(List<Integer> ids);
	
	/**
	 * 申请成为导游
	 * @param userId
	 * @param goodAtScenic
	 * @param birthday
	 * @param beGuideYear
	 * @param guideCardUrl
	 * @param location
	 * @return
	 */
	public int applyForGuide(int userId,String goodAtScenic,long birthday,int beGuideYear,
			String guideCardUrl,String guideCardId,String location,int city,String travelAgency);
	

	/**
	 * 获取附近的导游
	 * @param location 经纬度
	 * @param dist 距离范围
	 * @return
	 */
	public List<Integer> getNearByGuide(String location,double dist,int start,int rows);
	
	/**
	 * 带ext的表示组合了用户数据返回的接口    
	 * @param location
	 * @param dist
	 * @param start
	 * @param row
	 * @return
	 */
	public List<UserInfo> getNearByGuideExt(String location,double dist,int start,int rows);
	
	/**
	 * 根据条件搜索导游
	 * @param city
	 * @param gender
	 * @param senic
	 * @return
	 */
	public List<Integer> searchGuide(int city,int gender,String scenic,int start,int rows);
	
	public List<UserInfo> searchGuideExt(int city,int gender,String scenic,int start,int rows);
	
	/**
	 * 获取附近的导游,并根据性别和景点过滤
	 * @param location 经纬度
	 * @param dist 距离范围
	 * @return
	 */
	public List<Integer> getNearByGuideWithFilter(int gender,String scenic,String location,double dist,int start,int row);

	public List<UserInfo> getNearByGuideWithFilterExt(int gender,String scenic,String location,double dist,int start,int row);

	/**
	 * 修改头像
	 * @param userId
	 * @param headUrl
	 * @return
	 */
	@Deprecated
	public int changeHeadUrl(int userId,String headUrl);
	
	/**
	 * 更改用户信息接口
	 * @param userId
	 * @param userName
	 * @param gender
	 * @param headUrl
	 * @return
	 */
	public int changeUserInfo(int userId, String userName,int gender,String headUrl);
	
	/**
	 * 更改地理位置信息
	 * @param userId
	 * @param location
	 * @return
	 */
	public int changeLocation(int userId,String location);
	
	/**
	 * 评价引起的对导游评分
	 * @param userId
	 * @param satisfaction
	 * @return
	 */
	public int updateEvaluate(int userId,int satisfaction);

}
