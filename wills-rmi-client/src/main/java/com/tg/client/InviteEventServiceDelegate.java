package com.tg.client;

import java.util.List;

import com.tg.model.InviteEvent;
import com.tg.service.InviteEventService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class InviteEventServiceDelegate implements InviteEventService {

	private InviteEventService inviteEventServiceDelegate;
	
	public InviteEventServiceDelegate(){
		inviteEventServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), InviteEventService.class);

	}
	
	@Override
	public int invite(int userId, int guideId, String scenic, long startTime,
			long endTime) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.invite(userId, guideId, scenic, startTime, endTime);
	}

	@Override
	public int inviteAll(int userId, String scenic, long startTime, long endTime,String location,int gender) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.inviteAll(userId, scenic, startTime, endTime,location,gender);
	}

	@Override
	public int cancle(long eventId, int userId, int guideId) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.cancle(eventId, userId, guideId);
	}

	@Override
	public List<InviteEvent> getHistoricalInviteEvents(int userId, int start,
			int count) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.getHistoricalInviteEvents(userId, start, count);
	}

	@Override
	public InviteEvent getOneInviteEvent(int userId, long eventId) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.getOneInviteEvent(userId, eventId);
	}

	@Override
	public int setSatisfaction(long eventId, int satisfaction,String saContent ,int userId,
			int guideId) {
		// TODO Auto-generated method stub
		return inviteEventServiceDelegate.setSatisfaction(eventId, satisfaction,saContent, userId, guideId);
	}

	
	public static void main(String[] args) {
		InviteEventServiceDelegate d=new InviteEventServiceDelegate();
		//System.out.println(d.invite(10000006, 10000005, "故宫", System.currentTimeMillis(), System.currentTimeMillis()+10000));
		
		//System.out.println(d.inviteAll(10000008, "", System.currentTimeMillis(), System.currentTimeMillis()+1000, "38.6518141832995,104.07643139362494",0));
		System.out.println(d.getHistoricalInviteEvents(10000014, 0, 100).size());
		//System.out.println(d.cancle(1, 100000006, 100000007));
		//System.out.println(d.cancle(2, 100000006, 0));
		//System.out.println(d.setSatisfaction(7, 1, 10000008, 10000007));
		
	}
}
