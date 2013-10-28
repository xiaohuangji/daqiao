package com.tg.service.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tg.constant.RedisKeyConstant;
import com.tg.constant.ResultConstant;
import com.tg.constant.UserConstant;
import com.tg.dao.AdminTableDAO;
import com.tg.dao.UserDAO;
import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.passport.utils.GuideForSolrUtil;
import com.tg.service.AdminService;
import com.tg.service.UserService;
import com.tg.util.SMSUtil;
import com.wills.redis.client.RedisClient;

public class AdminServiceImpl implements AdminService{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);

	private UserDAO userDAO;
	
	private UserService userService;
	
	private AdminTableDAO adminTableDAO;
	
	private static  RedisClient redisUserInfo=new RedisClient(RedisKeyConstant.USER_INFO);
	
	private static RedisClient redisAdmin=new RedisClient(RedisKeyConstant.USER_ADMIN_MOBLE);
	
	private static final String SMS_TOBEGUIDE="恭喜您，导游审核通过";
	
	private static final String SMS_REJECT_TOBEGUIDE="您的导游身份未通过审核:";
	
	@Override
	public int setAdminMobile(String mobile) {
		// TODO Auto-generated method stub
		int result=adminTableDAO.setAdminMobile(mobile);
		redisAdmin.del("mobile");
		return ( result==1 ? ResultConstant.OP_OK : ResultConstant.OP_FAIL);
	}

	@Override
	public String getAdminMobile() {
		// TODO Auto-generated method stub
		String mobile=redisAdmin.get("mobile", String.class);
		if(mobile==null){
			mobile=adminTableDAO.getAdminMobile();
			if(mobile!=null){
				redisAdmin.set("mobile", mobile);
			}else{//数据库不存在，插入一个哨兵。避免每次再穿透redis
				mobile="000";
				redisAdmin.set("mobile", "000");
			}
		}
		if(mobile.equals("000")){//可能是哨兵
			return null;
		}
		return mobile;
	}


	@Override
	public List<Integer> getAllApplyForGuideUsers() {
		// TODO Auto-generated method stub
		return userDAO.getAllApplyForGuideUsers();
	}

	@Override
	public List<GuideInfo> getAllApplyForGuideUsersExt() {
		// TODO Auto-generated method stub
		return userService.getGuideInfos(getAllApplyForGuideUsers());
	}


	@Override
	public int toBeGuide(int userId) {
		// TODO Auto-generated method stub
		//更改userInfo中的usertype
		userDAO.changeUserInfoType(userId, UserConstant.TYPE_GUIDE);
		redisUserInfo.del(String.valueOf(userId));
		//更改guideInfo中状态
		userDAO.changeGuideInfoStatus(userId, UserConstant.GSTAUS_NORMAL);
		UserInfo userInfo=userService.getUserInfo(userId);
		//发短信通知用户
		SMSUtil.sendSM(userInfo.getMobile(), SMS_TOBEGUIDE);
		//将数据更新入solr
		if(GuideForSolrUtil.addGuideToSolr((GuideInfo)userService.getUserInfo(userId))){
			logger.info("add new guide to solr:"+userId);
			return ResultConstant.OP_OK;
		}
		else{
			return ResultConstant.OP_FAIL;
		}
	}
	

	@Override
	public int rejectToBeGuide(int userId, String reason) {
		// TODO Auto-generated method stub
		//清空guide_info中的宴请信息
		userDAO.removeGuideInfo(userId);
		UserInfo userInfo=userService.getUserInfo(userId);
		SMSUtil.sendSM(userInfo.getMobile(), SMS_REJECT_TOBEGUIDE+reason);
		logger.info("reject a user guide apply:"+userId);
		return ResultConstant.OP_OK;
	}
	
	@Override
	public List<UserInfo> getAllGuide(int start, int rows) {
		// TODO Auto-generated method stub
		return userService.getUserInfos(userDAO.getAllGuides(start, rows));
	}
	

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setAdminTableDAO(AdminTableDAO adminTableDAO) {
		this.adminTableDAO = adminTableDAO;
	}
	
}
