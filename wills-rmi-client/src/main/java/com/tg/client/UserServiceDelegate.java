package com.tg.client;

import java.util.List;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.service.UserService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class UserServiceDelegate implements UserService {

	private UserService userServiceDelegate;
	
	public UserServiceDelegate(){
		userServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), UserService.class);

	}

	@Override
	public int getVerifyCode(String mobile) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getVerifyCode(mobile);
	}

	@Override
	public int register(String mobile, String password, String verifyCode,
			String name, int gender) {
		// TODO Auto-generated method stub
		return userServiceDelegate.register(mobile, password, verifyCode, name, gender);
	}

	@Override
	public int login(String mobile, String password) {
		// TODO Auto-generated method stub
		return userServiceDelegate.login(mobile, password);
	}

	@Override
	public UserInfo getUserInfo(int userId) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getUserInfo(userId);
	}

	@Override
	public int applyForGuide(int userId, String goodAtScenic, long birthday,
			int beGuideYear, String guideCardUrl, String guideCardId,
			String location, int city,String travelAgency) {
		// TODO Auto-generated method stub
		return userServiceDelegate.applyForGuide(userId, goodAtScenic, birthday, beGuideYear, guideCardUrl, guideCardId, location, city,travelAgency);
	}


	@Override
	public List<Integer> getNearByGuide(String location, double dist,
			int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getNearByGuide(location, dist, start, row);
	}

	@Override
	public List<Integer> searchGuide(int city, int gender, String senic,
			int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.searchGuide(city, gender, senic, start, row);
	}

	@Override
	public List<UserInfo> getUserInfos(List<Integer> ids) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getUserInfos(ids);
	}

	
	@Override
	public List<Integer> getNearByGuideWithFilter(int gender, String scenic,
			String location, double dist, int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getNearByGuideWithFilter(gender, scenic, location, dist, start, row);
	}

	@Override
	public List<UserInfo> getNearByGuideExt(String location, double dist,
			int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getNearByGuideExt(location, dist, start, row);
	}

	@Override
	public List<UserInfo> searchGuideExt(int city, int gender, String scenic,
			int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.searchGuideExt(city, gender, scenic, start, row);
	}

	@Override
	public List<UserInfo> getNearByGuideWithFilterExt(int gender,
			String scenic, String location, double dist, int start, int row) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getNearByGuideWithFilterExt(gender, scenic, location, dist, start, row);
	}

	@Override
	public UserInfo registerExt(String mobile, String password,
			String verifyCode, String name, int gender) {
		// TODO Auto-generated method stub
		return userServiceDelegate.registerExt(mobile, password, verifyCode, name, gender);
	}

	@Override
	public UserInfo loginExt(String mobile, String password) {
		// TODO Auto-generated method stub
		return userServiceDelegate.loginExt(mobile, password);
	}
	
	@Override
	public int changeHeadUrl(int userId, String headUrl) {
		// TODO Auto-generated method stub
		return userServiceDelegate.changeHeadUrl(userId, headUrl);
	}
	
	@Override
	public List<GuideInfo> getGuideInfos(List<Integer> ids) {
		// TODO Auto-generated method stub
		return userServiceDelegate.getGuideInfos(ids);
	}

	
	@Override
	public int changeLocation(int userId, String location) {
		// TODO Auto-generated method stub
		return userServiceDelegate.changeLocation(userId, location);
	}
	

	@Override
	public int updateEvaluate(int userId, int satisfaction) {
		// TODO Auto-generated method stub
		return userServiceDelegate.updateEvaluate(userId, satisfaction);
	}
	
	@Override
	public int changeUserInfo(int userId, String userName, int gender,
			String headUrl) {
		// TODO Auto-generated method stub
		return userServiceDelegate.changeUserInfo(userId, userName, gender, headUrl);
	}
	@Override
	public int changePassword(int userId, String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return userServiceDelegate.changePassword(userId, oldPassword, newPassword);
	}
	public static void main(String[] args) {
		UserServiceDelegate userServiceDelegate=new UserServiceDelegate();
		//userServiceDelegate.getVerifyCode("18510408654");
		
		//userServiceDelegate.register("18510408654", "123456", "7204", "judy", 2);
		//GuideInfo guideInfo=(GuideInfo)(userServiceDelegate.getUserInfo(10000005));
		//System.out.println(guideInfo.toString());
		
		//userServiceDelegate.applyForGuite(10000005, "故宫 天坛", System.currentTimeMillis(), 2000, "http://bcs.duapp.com/tgimage/138089992694012.jpg", "guideId123456", "38.6518141832995,104.07643139362494", 99);
		userServiceDelegate.applyForGuide(10000034 , "晋祠 太原", System.currentTimeMillis(), 19980, "http://bcs.duapp.com/tgimage/138089992694012.jpg", "guideId999999", "38.6518141832995,104.07643139362494", 88,"just test");
		
		//System.out.println(userServiceDelegate.toBeGuide(10000010));
		//System.out.println(userServiceDelegate.toBeGuide(10000009));
		
		//System.out.println(userServiceDelegate.getNearByGuideExt("38.65,104.07643139362494", 2000, 0, 100));
		//System.out.println(userServiceDelegate.searchGuideExt(99, 0, "宫", 0, 100));
		//List<Integer> ids=userServiceDelegate.getAllApplyForGuideUsers();
		//System.out.println(userServiceDelegate.changePassword(10000033, "123456", "123789"));
	}

}
