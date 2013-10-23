package com.tg.client;

import java.util.List;

import com.tg.model.GuideInfo;
import com.tg.model.UserInfo;
import com.tg.service.AdminService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class AdminServiceDelegate implements AdminService{

	private AdminService adminServiceDelegate;
	
	public AdminServiceDelegate(){
		adminServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), AdminService.class);

	}
	@Override
	public int setAdminMobile(String mobile) {
		// TODO Auto-generated method stub
		return adminServiceDelegate.setAdminMobile(mobile);
	}

	@Override
	public String getAdminMobile() {
		// TODO Auto-generated method stub
		return adminServiceDelegate.getAdminMobile();
	}

	@Override
	public List<Integer> getAllApplyForGuideUsers() {
		// TODO Auto-generated method stub
		return adminServiceDelegate.getAllApplyForGuideUsers();
	}

	@Override
	public List<GuideInfo> getAllApplyForGuideUsersExt() {
		// TODO Auto-generated method stub
		return adminServiceDelegate.getAllApplyForGuideUsersExt();
	}

	@Override
	public int toBeGuide(int userId) {
		// TODO Auto-generated method stub
		return adminServiceDelegate.toBeGuide(userId);
	}

	@Override
	public List<UserInfo> getAllGuide(int start, int rows) {
		// TODO Auto-generated method stub
		return adminServiceDelegate.getAllGuide(start, rows);
	}

	public static void main(String[] args) {
		AdminServiceDelegate dd=new AdminServiceDelegate();
		System.out.println(dd.toBeGuide(10000014));
	}
}
