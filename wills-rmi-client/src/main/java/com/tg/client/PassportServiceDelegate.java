package com.tg.client;

import com.tg.model.UserPassport;
import com.tg.service.PassportService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class PassportServiceDelegate implements PassportService{

	private PassportService passportServiceDelegate;
	
	public PassportServiceDelegate(){
		passportServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), PassportService.class);

	}
	
	@Override
	public void destroyTicket(String ticket) {
		// TODO Auto-generated method stub
		passportServiceDelegate.destroyTicket(ticket);
	}

	@Override
	public int getUserIdByTicket(String ticket) {
		// TODO Auto-generated method stub
		return passportServiceDelegate.getUserIdByTicket(ticket);
	}

	@Override
	public String createTicket(UserPassport userPassport) {
		// TODO Auto-generated method stub
		return passportServiceDelegate.createTicket(userPassport);
	}

	@Override
	public UserPassport getPassportByTicket(String ticket) {
		// TODO Auto-generated method stub
		return passportServiceDelegate.getPassportByTicket(ticket);
	}

	@Override
	public String getTicketByUserId(int userId) {
		// TODO Auto-generated method stub
		return passportServiceDelegate.getTicketByUserId(userId);
	}

}
