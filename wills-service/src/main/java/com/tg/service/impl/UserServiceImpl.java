package com.tg.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.tg.constant.EventConstant;
import com.tg.constant.RedisKeyConstant;
import com.tg.constant.ResultConstant;
import com.tg.constant.UserConstant;
import com.tg.dao.IdSequenceDAO;
import com.tg.dao.UserDAO;
import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.service.AdminService;
import com.tg.service.PassportService;
import com.tg.service.UserService;
import com.tg.service.utils.GuideForSolrUtil;
import com.tg.solr.SolrClient;
import com.tg.solr.User4Solr;
import com.tg.util.MD5Util;
import com.tg.util.SMSUtil;
import com.tg.util.StrFilterUtil;
import com.wills.redis.client.RedisClient;

public class UserServiceImpl implements UserService{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	private UserDAO userDAO;
	
	private IdSequenceDAO idSequenceDAO;
	
	private AdminService adminService;
	
	private PassportService passportService;
	
	private static  RedisClient redisVerify=new RedisClient(RedisKeyConstant.USER_VERIFYCODE);
	
	private static  RedisClient redisGuideInfo=new RedisClient(RedisKeyConstant.USER_GUIDE_INFO);
	
	private static  RedisClient redisUserInfo=new RedisClient(RedisKeyConstant.USER_INFO);
	
	private static String verifyCodeStr="TG注册验证码：";
	
	private static String adminCheckStr="有新的导游申请";
	
	//评价打分时对应的打分值，满意对应5分，不满意对应2分
	private static int EVALUATE_SCORE_YES=5;
	
	private static int EVALUATE_SCORE_NO=2;
	
	@Override
	public int getVerifyCode(String mobile) {
		// TODO Auto-generated method stub
		//判断如果已经是注册用户，返回错误
		Integer userId=userDAO.getUserIdByMobile(mobile);
		if(userId!=null){
			logger.warn("this user has registered :"+userId);
			return ResultConstant.OP_FAIL;
		}
		Random r = new Random();
		int x = r.nextInt(9999); 
		redisVerify.setex(mobile, x, 120);
		if(SMSUtil.sendSM(mobile, verifyCodeStr+x)){
			logger.debug("gen verify code for mobile:"+x);
			return ResultConstant.OP_OK;
		}else{
			logger.warn("send verify code error:"+mobile);
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
			logger.debug("verify code not match"+mobile);
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
			logger.info("register succ ,userId:"+userId);
			return userId;
		}
		else{
			return ResultConstant.OP_FAIL;
		}
	}

	@Override
	public int login(String mobile, String password) {
		// TODO Auto-generated method stub
		Integer userId=userDAO.getUserIdByMobile(mobile);
		if(userId==null)
			return ResultConstant.OP_FAIL;
		String realPwd=userDAO.getPwd(userId);
		if(realPwd==null)
			return ResultConstant.OP_FAIL;
		if(realPwd.equals(MD5Util.md5(password+userId))){
			return userId;
		}
		logger.debug("login fail,password error:"+mobile);
		return ResultConstant.OP_FAIL;
	}
	
	@Override
	public int changePassword(int userId,String oldPassword,String newPassword){
		String oldPwd=userDAO.getPwd(userId);
		if(oldPwd.equals(MD5Util.md5(oldPassword+userId))){//旧密码验证通过
			logger.debug("oldpwd succ,start to update newpwd:"+userId);
			int result=userDAO.insertPwd(userId, MD5Util.md5(newPassword+userId));
			passportService.destroyTicket(passportService.getTicketByUserId(userId));
			return (result!=0?ResultConstant.OP_OK:ResultConstant.OP_FAIL);
		}else{
			logger.warn("oldpwd error,changepwd fail:"+userId);
			return ResultConstant.OP_FAIL;
		}
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
	

	@Override
	public List<GuideInfo> getGuideInfos(List<Integer> ids) {
		// TODO Auto-generated method stub
		if(ids==null || ids.size()==0)
			return null;
		List<GuideInfo> users=new ArrayList<GuideInfo>();
		for(Integer id:ids){
			users.add(getGuideInfoById(id));
		}
		return users;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("copy userinfo to guideinfo error:",e);
		} 
		redisGuideInfo.set(String.valueOf(userId), guideInfo);
		return guideInfo;
	}
	
	@Override
	public int applyForGuide(int userId, String goodAtScenic, long birthday,
			int beGuideYear, String guideCardUrl, String guideCardId,String location,int city) {
		UserInfo userInfo=getUserInfo(userId);
		//如果已经是guide，不允许再次提交申请
		if(userInfo.getUserType()==UserConstant.TYPE_GUIDE)
			return ResultConstant.OP_FAIL;
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
		//清除缓存
		redisGuideInfo.del(String.valueOf(userId));
		//清除solr。只要是重新申请，之前的状态废除
		SolrClient.getInstance().deleteUser(userId);
		
		if(userDAO.insertGuideInfo(guideInfo)==1){
			//发送提醒给管理员进行审核
			logger.info("apply for guide succ:"+userId);
			SMSUtil.sendSM(adminService.getAdminMobile(),adminCheckStr);
			return ResultConstant.OP_OK;
		}else{
			return ResultConstant.OP_FAIL;
		}
	}
	
	@Autowired
	public void setPassportService(PassportService passportService) {
		this.passportService = passportService;
	}

	@Autowired
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
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
		logger.debug("getnearbyguide succ ,location:"+location);
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
		goodAtScenic=StrFilterUtil.queryFilter(goodAtScenic, false);
		if(goodAtScenic!=null&&!goodAtScenic.isEmpty()&&!StrFilterUtil.isBlank(goodAtScenic)){
			sb.append(" AND goodAtScenic:"+goodAtScenic);
		}
		solrQuery.setQuery(sb.toString());
		QueryResponse qrs=SolrClient.getInstance().queryUser(solrQuery);
		logger.debug("searchguide succ:"+sb.toString());
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
	public List<UserInfo> getNearByGuideWithFilterExt(int gender,
			String scenic, String location, double dist, int start, int row) {
		// TODO Auto-generated method stub
		return getUserInfos(getNearByGuideWithFilter(gender, scenic, location, dist, start, row));
	}

	@Override
	public UserInfo registerExt(String mobile, String password, String verifyCode,
			String name, int gender) {
		// TODO Auto-generated method stub
		int result=register(mobile, password, verifyCode, name, gender);
		if(result!=ResultConstant.OP_FAIL){
			return getUserInfo(result);
		}
		else {
			return null;
		}
	}

	@Override
	public UserInfo loginExt(String mobile, String password) {
		// TODO Auto-generated method stub
		int result=login(mobile, password);
		if(result!=ResultConstant.OP_FAIL){
			return getUserInfo(result);
		}else{
			return null;
		}
	}

	@Override
	public int changeHeadUrl(int userId, String headUrl) {
		// TODO Auto-generated method stub
		int result= userDAO.changeHeadUrl(userId, headUrl);
		
		redisUserInfo.del(String.valueOf(userId));
		redisGuideInfo.del(String.valueOf(userId));
		if(result==1)
			return ResultConstant.OP_OK;
		return ResultConstant.OP_FAIL;
	}
	
	@Override
	public int changeUserInfo(int userId, String userName,int gender,String headUrl) {
		// TODO Auto-generated method stub
		//如果是导游，不允许更改用户姓名和性别,但是头像可以改
		UserInfo userInfo=getUserInfo(userId);
		if(userInfo!=null&&userInfo.getUserType()==UserConstant.TYPE_GUIDE&&(!userInfo.getUserName().equals(userName)||userInfo.getGender()!=gender))
			//以上条件表示导游要更改姓名或者性别
			return ResultConstant.OP_FAIL;
		int result= userDAO.changeUserInfo(userId, userName, gender, headUrl);
		redisUserInfo.del(String.valueOf(userId));
		redisGuideInfo.del(String.valueOf(userId));
		logger.debug("changeuserinfo:"+userId);
		return (result==1?ResultConstant.OP_OK:ResultConstant.OP_FAIL);
	}
	

	@Override
	public int changeLocation(int userId, String location) {
		// TODO Auto-generated method stub
		//更新数据库
		int daoResult=userDAO.changeLocation(userId, location);
		if(daoResult!=1){
			logger.debug("this user is not a guide,it can not changeLocation");
			return ResultConstant.OP_FAIL;
		}
				
		//更新缓存
		redisGuideInfo.del(String.valueOf(userId));
		//更新solr
		GuideInfo g=(GuideInfo)getUserInfo(userId);
		boolean solrResult=GuideForSolrUtil.addGuideToSolr(g);
		if(solrResult==true){
			logger.debug("change location succ:"+userId);
			return ResultConstant.OP_OK;
		}
		else {
			return ResultConstant.OP_FAIL;
		}
	}
	
	@Override
	public int updateEvaluate(int userId, int satisfaction) {
		// TODO Auto-generated method stub
		int result=userDAO.updateEvaluate(userId, satisfaction==EventConstant.SATIS_YES ? EVALUATE_SCORE_YES : EVALUATE_SCORE_NO);
		redisGuideInfo.del(String.valueOf(userId));
		logger.debug("update guide's evaluate:"+userId);
		return (result==1?ResultConstant.OP_OK:ResultConstant.OP_FAIL);
	}
	
}
