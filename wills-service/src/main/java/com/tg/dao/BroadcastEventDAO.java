package com.tg.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog = DAOConstant.CATALOG)
public interface BroadcastEventDAO {
	
	@SQL("insert into broadcast_event (event_id,broadcast_ids) values (:1,:2)")
	public int insertBroadCastEvent(long eventId,String ids);
	
	@SQL("select broadcast_ids from broadcast_event where event_id=:1")
	public String getBroadcastIds(long eventId);
}
