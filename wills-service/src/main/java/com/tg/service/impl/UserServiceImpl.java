package com.tg.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.wills.redis.client.RedisClient;
import com.tg.constant.RedisKeyConstant;
import com.tg.constant.ResultConstant;
import com.tg.constant.UserConstant;
import com.tg.dao.IdSequenceDAO;
import com.tg.dao.UserDAO;
import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.service.UserService;
import com.tg.solr.SolrClient;
import com.tg.solr.User4Solr;
import com.tg.util.CONFIGUtil;
import com.tg.util.MD5Util;
import com.tg.util.SMSUtil;

public class UserServiceImpl implements UserService{

	private UserDAO userDAO;
	
	private IdSequenceDAO idSequenceDAO;
	
	private static  RedisClient redisVerify=new RedisClient(RedisKeyConstant.USER_VERIFYCODE);
	
	private static  RedisClient redisGuideInfo=new RedisClient(RedisKeyConstant.USER_GUIDE_INFO);
	
	private static  RedisClient redisUserInfo=new RedisClient(RedisKeyConstant.USER_INFO);

	private static String adminMobile=CONFIGUtil.getInstance().getConfig("admin_mobile");
	
	private static String verifyCodeStr="TG注册验证码：";
	
	private static String adminCheckStr="有新的导游申请";
	
	@Override
	public int getVerifyCode(String mobile) {
		// TODO Auto-generated method stub
		Random r = new Random();
		int x = r.nextInt(9999); 
		redisVerify.setex(mobile, x, 120);
		if(SMSUtil.sendSM(mobile, verifyCodeStr+x)){
			return ResultConstant.OP_OK;
		}else{
			return ResultConstant.OP_FAIL;
		}
		
	}

	@Override
	public int register(String mobile, String password,String verifyCode ,String name,
			int gender) {
		// TODO Auto-generated method stub
		//检查验证码
		String realVerifyCode=redisVerify.get(mobile, String.class);
		if(!realVerifyCode.equals(verifyCode)){
			return ResultConstant.OP_FAIL;
		}
		// 入库
		//获取userId
		int userId=idSequenceDAO.getUserId();
		//密码信息
		int result=userDAO.insertPwd(userId, MD5Util.md5(password+userId));
		if(result==0)
			return ResultConstant.OP_FAIL;
		//基本信息
		UserInfo userInfo=new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setGender(gender);
		userInfo.setMobile(mobile);
		userInfo.setUserName(name);
		userInfo.setUserType(UserConstant.TYPE_TOURIST);
		result=userDAO.insertUserInfo(userInfo);
		if(result==1){
			return userId;
		}
		else{
			return ResultConstant.OP_FAIL;
		}
	}

	@Override
	public int login(String mobile, String password) {
		// TODO Auto-generated method stub
		int userId=userDAO.getUserIdByMobile(mobile);
		String realPwd=userDAO.getPwd(userId);
		if(realPwd.equals(MD5Util.md5(password+userId))){
			return userId;
		}
		return ResultConstant.OP_FAIL;
	}

	@Override
	public UserInfo getUserInfo(int userId) {
		// TODO Auto-generated method stub
		UserInfo userInfo=getUserInfoById(userId);
		if(userInfo!=null){
			if(userInfo.getUserType()==UserConstant.TYPE_TOURIST){
				return userInfo;
			}else{
				return getGuideInfoById(userId);
			}
		}
		return null;
	}

	private UserInfo getUserInfoById(int userId){
		UserInfo userInfo=redisUserInfo.get(String.valueOf(userId), UserInfo.class);
		if(userInfo==null){
			userInfo=userDAO.getUserById(userId);
			redisUserInfo.set(String.valueOf(userId), userInfo);
		}
		return userInfo;
	}
		
	private GuideInfo getGuideInfoById(int userId){
		GuideInfo guideInfo=redisGuideInfo.get(String.valueOf(userId), GuideInfo.class);
		if(guideInfo!=null){
			return guideInfo;
		}
		guideInfo=userDAO.getGuideById(userId);
		UserInfo userInfo=getUserInfoById(userId);
		try {
			BeanUtils.copyProperties(guideInfo, userInfo);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		redisGuideInfo.set(String.valueOf(userId), guideInfo);
		return guideInfo;
	}
	
	@Override
	public int applyForGuite(int userId, String goodAtScenic, long birthday,
			int beGuideYear, String guideCardUrl, String guideCardId,String location,int city) {
		// TODO Auto-generated method stub
		GuideInfo guideInfo=new GuideInfo();
		guideInfo.setUserId(userId);
		guideInfo.setGoodAtScenic(goodAtScenic);
		guideInfo.setBirthday(birthday);
		guideInfo.setBeGuideYear(beGuideYear);
		guideInfo.setGuideCardUrl(guideCardUrl);
		guideInfo.setGuideCardId(guideCardId);
		guideInfo.setLocation(location);
		guideInfo.setCity(city);
		guideInfo.setStatus(UserConstant.GSTATUS_PENDING);
		if(userDAO.insertGuideInfo(guideInfo)==1){
			//发送提醒给管理员进行审核
			SMSUtil.sendSM(adminMobile,adminCheckStr);
			return ResultConstant.OP_OK;
		}else{
			return ResultConstant.OP_FAIL;
		}
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
		if(addGuideToSolr((GuideInfo)getUserInfo(userId)))
			return ResultConstant.OP_OK;
		else
			return ResultConstant.OP_FAIL;
			
	}

	private boolean addGuideToSolr(GuideInfo guideInfo){
		User4Solr user4Solr=new User4Solr();
		user4Solr.setCity(guideInfo.getCity());
		user4Solr.setGender(guideInfo.getGender());
		user4Solr.setId(guideInfo.getUserId());
		user4Solr.setMobile(guideInfo.getMobile());
		user4Solr.setPosition(guideInfo.getLocation());
		user4Solr.setUserName(guideInfo.getUserName());
		user4Solr.setUserType(guideInfo.getUserType());
		user4Solr.setBeGuideYear(guideInfo.getBeGuideYear());
		user4Solr.setBirthday(guideInfo.getBirthday());
		user4Solr.setGoodAtScenic(guideInfo.getGoodAtScenic());
		SolrClient.getInstance().addUser(user4Solr);
		return SolrClient.getInstance().addUserCommit();
	}

	
	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Autowired
	public void setIdSequenceDAO(IdSequenceDAO idSequenceDAO) {
		this.idSequenceDAO = idSequenceDAO;
	}

	@Override
	public List<Integer> getNearByGuide(String location, double dist,
			int start, int row) {
		// TODO Auto-generated method stub
		SolrQuery queryArgs=new SolrQuery();
	    queryArgs.setSortField("geodist()", ORDER.asc);
        queryArgs.setFilterQueries("{!geofilt}");
        queryArgs.setFields("*,dist:geodist()");
        queryArgs.set("sfield", "position");
        queryArgs.set("pt", location);
        queryArgs.set("d", String.valueOf(dist));
        queryArgs.set("spatial",true);
        queryArgs.setStart(start);
        queryArgs.setRows(row);
        queryArgs.setQuery("*:*");
		QueryResponse qrs=SolrClient.getInstance().queryUser(queryArgs);
		return solrUserToIds(qrs);
	}

	@Override
	public List<Integer> searchGuide(int city, int gender, String goodAtScenic,
			int start, int row) {
		// TODO Auto-generated method stub
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.setStart(start);
		solrQuery.setRows(row);
		StringBuffer sb=new StringBuffer();
		sb.append("city:"+city);
		if(gender!=UserConstant.GENDER_UNKNOWN){
			sb.append(" AND gender:"+gender);
		}
		if(goodAtScenic!=null&&!goodAtScenic.isEmpty()){
			sb.append(" AND goodAtScenic:"+goodAtScenic);
		}
		solrQuery.setQuery(sb.toString());
		QueryResponse qrs=SolrClient.getInstance().queryUser(solrQuery);
		return solrUserToIds(qrs);
	}

	private List<Integer> solrUserToIds(QueryResponse qrs){
		List<User4Solr> user4Solrs=qrs.getBeans(User4Solr.class);
		if(user4Solrs==null)
			return null;
		List<Integer> ids=new ArrayList<Integer>();
		for(User4Solr  user4Solr:user4Solrs){
			ids.add(user4Solr.getId());
		}
		return ids;
	}

	@Override
	public List<Integer> getAllApplyForGuideUsers() {
		// TODO Auto-generated method stub
		return userDAO.getAllApplyForGuideUsers();
	}

	@Override
	public List<UserInfo> getUserInfos(List<Integer> ids) {
		// TODO Auto-generated method stub
		if(ids==null || ids.size()==0)
			return null;
		List<UserInfo> users=new ArrayList<UserInfo>();
		for(Integer id:ids){
			users.add(getUserInfo(id));
		}
		return users;
	}

	@Override
	public List<Integer> getNearByGuideWithFilter(int gender, String scenic,
			String location, double dist, int start, int row) {
		// TODO Auto-generated method stub
		SolrQuery queryArgs=new SolrQuery();
	    queryArgs.setSortField("geodist()", ORDER.asc);
        queryArgs.setFilterQueries("{!geofilt}");
        queryArgs.setFields("*,dist:geodist()");
        queryArgs.set("sfield", "position");
        queryArgs.set("pt", location);
        queryArgs.set("d", String.valueOf(dist));
        queryArgs.set("spatial",true);
        queryArgs.setStart(start);
        queryArgs.setRows(row);
        
        StringBuffer sb=new StringBuffer();
        sb.append("*:*");
		if(gender!=UserConstant.GENDER_UNKNOWN){
			sb.append(" AND gender:"+gender);
		}
		if(scenic!=null&&!scenic.isEmpty()){
			sb.append(" AND goodAtScenic:"+scenic);
		}
		queryArgs.setQuery(sb.toString());
		QueryResponse qrs=SolrClient.getInstance().queryUser(queryArgs);
		return solrUserToIds(qrs);
	}

	@Override
	public List<UserInfo> getNearByGuideExt(String location, double dist,
			int start, int row) {
		// TODO Auto-generated method stub
		return getUserInfos(getNearByGuide(location, dist, start, row));
	}

	@Override
	public List<UserInfo> searchGuideExt(int city, int gender, String goodAtScenic,
			int start, int row) {
		// TODO Auto-generated method stub
		return getUserInfos(searchGuide(city, gender, goodAtScenic, start, row));
	}

	@Override
	public List<UserInfo> getAllApplyForGuideUsersExt() {
		// TODO Auto-generated method stub
		return getUserInfos(getAllApplyForGuideUsers());
	}

	@Override
	public List<UserInfo> getNearByGuideWithFilterExt(int gender,
			String scenic, String location, double dist, int start, int row) {
		// TODO Auto-generated method stub
		return getUserInfos(getNearByGuideWithFilter(gender, scenic, location, dist, start, row));
	}
	
}
