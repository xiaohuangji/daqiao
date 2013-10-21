package com.tg.dao;

import java.util.List;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog = "tg")
public interface UserDAO {

	@SQL("replace into user_pwd (user_id,md5_pwd) values (:1,:2)")
	public int insertPwd(int userId,String md5Pwd);
	
	@SQL("update user_pwd set md5_pwd=:2 where user_id=:1")
	public int changePwd(int userId,String md5Pwd);
	
	@SQL("select md5_pwd from user_pwd where user_id=:1")
	public String getPwd(int userId);
	
	@SQL("select user_id from user_info where mobile=:1 ")
	public int getUserIdByMobile(String mobile);
	
	@SQL("replace into user_info (user_id,user_name,mobile,gender,user_type) values (:1.userId,:1.userName,:1.mobile,:1.gender,:1.userType)")
	public int insertUserInfo(UserInfo userInfo);
	
	@SQL("select user_id,user_name,mobile,gender,user_type,head_url from user_info where user_id=:1")
	public UserInfo getUserById(int userId);
	
	@SQL("select * from guide_info where user_id=:1")
	public GuideInfo getGuideById(int userId);
	
	@SQL("replace into guide_info (user_id,good_at_scenic,birthday,be_guide_year,guide_card_url,guide_card_id,location,city,evaluate_score,evaluate_count,status) value" +
			" (:1.userId,:1.goodAtScenic,:1.birthday,:1.beGuideYear,:1.guideCardUrl,:1.guideCardId,:1.location,:1.city,:1.evaluateScore,:1.evaluateCount,:1.status)")
	public int insertGuideInfo(GuideInfo guideInfo);
	
	@SQL("update user_info set user_type=:2 where user_id=:1")
	public int changeUserInfoType(int userId,int userType);
	
	@SQL("update guide_info set status=:2 where user_id=:1")
	public int changeGuideInfoStatus(int userId,int status);
	
	@SQL("select user_id from guide_info where status=0")
	public List<Integer> getAllApplyForGuideUsers();
	
	@SQL("update user_info set head_url=:2 where user_id=:1")
	public int changeHeadUrl(int userId,String headUrl);
	
	@SQL("udpate user_info set user_name=:2 ,gender=:3 ,head_url=:4 where user_id:1")
	public int changeUserInfo(int userId,String userName,int gender,String headUrl);
	
	@SQL("update guide_info set location=:2 where user_id=:1")
	public int changeLocation(int userId,String location);
	
	@SQL("select user_id from guide_info where status=1 order by user_id limit :1,:2 ")
	public List<Integer> getAllGuides(int start,int rows);
	
	@SQL("update guide_info set evaluate_score=evalute_score+:2 ,evaluate_count=evalute_count+1 where user_id=:1")
	public int updateEvaluate(int userId,int evaluateScore);
}
