package com.tg.dao;

import java.util.List;

import com.tg.model.InviteEvent;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog = DAOConstant.CATALOG)
public interface InviteEventDAO {

	@SQL("insert into invite_event (event_id,user_id,guide_id,event_type,event_status,create_time,start_time,end_time,scenic,satisfaction,sa_content) values (:1.eventId,:1.userId,:1.guideId,:1.eventType,:1.eventStatus,:1.createTime,:1.startTime,:1.endTime,:1.scenic,:1.satisfaction,:1.saContent)")
	public int insertInvite(InviteEvent  inviteEvent);
	
	@SQL("update invite_event set event_status=:3 where event_id=:2")
	public int changeStatus(int userId,long eventId,int eventStatus);
	
	@SQL("update invite_event set event_status=2 , guide_id=:3 where event_id=:2")
	public int accepted(int userId,long eventId,int guideId);
	
	@SQL("update invite_event set satisfaction=:3 , sa_content=:4 ,event_status=4 where event_id=:2 and event_status=2")
	public int setSatisfaction(int userId,long eventId,int satisfaction,String saContent);
	
	@SQL("select * from invite_event where user_id=:1 order by create_time desc limit :2,:3")
	public List<InviteEvent> getHistoricalInviteEvents(int userId,int start,int count);
	
	@SQL("select * from invite_event where event_id=:2")
	public InviteEvent getOneInviteEvent(int userId,long eventId);
}
