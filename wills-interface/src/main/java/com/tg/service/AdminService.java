package com.tg.service;

import java.util.List;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;

public interface AdminService {

	
	/**
	 * 设置管理员手机号
	 * @param mobile
	 * @return
	 */
	public int setAdminMobile(String mobile);
	
	/**
	 * 获取当前管理员手机号
	 * @param mobile
	 * @return
	 */
	public String getAdminMobile();
	
	
	/**
	 * 获取所有申请导游的人，为管理员提供接口
	 * @return
	 */
	public List<Integer> getAllApplyForGuideUsers();
	
	public List<GuideInfo> getAllApplyForGuideUsersExt();
	
	/**
	 * 审核通过，将userId升级为guide
	 * @param userId
	 * @return
	 */
	public int toBeGuide(int userId);
	
	/**
	 * 获取所有guide
	 * @return
	 */
	public List<UserInfo> getAllGuide(int start,int rows);
}
