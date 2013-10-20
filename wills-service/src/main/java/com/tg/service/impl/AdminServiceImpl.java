package com.tg.service.impl;

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
import com.wills.redis.client.RedisClient;

public class AdminServiceImpl implements AdminService{

	private UserDAO userDAO;
	
	private UserService userService;
	
	private AdminTableDAO adminTableDAO;
	
	private static  RedisClient redisUserInfo=new RedisClient(RedisKeyConstant.USER_INFO);
	
	private static RedisClient redisAdmin=new RedisClient(RedisKeyConstant.USER_ADMIN_MOBLE);
	
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
				redisAdmin.set("", mobile);
			}else{//数据库不存在，插入一个哨兵。避免每次再穿透redis
				redisAdmin.set("", "000");
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
	public List<UserInfo> getAllApplyForGuideUsersExt() {
		// TODO Auto-generated method stub
		return userService.getUserInfos(getAllApplyForGuideUsers());
	}


	@Override
	public int toBeGuide(int userId) {
		// TODO Auto-generated method stub
		//更改userInfo中的usertype
		int result=userDAO.changeUserInfoType(userId, UserConstant.TYPE_GUIDE);
		redisUserInfo.del(String.valueOf(userId));
		if(result!=1)
			return ResultConstant.OP_FAIL;
		//更改guideInfo中状态
		result=userDAO.changeGuideInfoStatus(userId, UserConstant.GSTAUS_NORMAL);
		if(result!=1)
			return ResultConstant.OP_FAIL;
		//将数据更新入solr
		if(GuideForSolrUtil.addGuideToSolr((GuideInfo)userService.getUserInfo(userId)))
			return ResultConstant.OP_OK;
		else
			return ResultConstant.OP_FAIL;
			
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
