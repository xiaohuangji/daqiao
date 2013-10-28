package com.tg.dao;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog = "idseq")
public interface IdSequenceDAO {

    @SQL("select nextval('tg_user_id')")
    public int getUserId();

    @SQL("select nextval('tg_event_id')")
    public long getEventId();
    
    @SQL("select nextval('tg_msg_id')")
    public long getMsgId();

}
