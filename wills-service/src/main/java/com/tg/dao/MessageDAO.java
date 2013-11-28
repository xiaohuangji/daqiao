package com.tg.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

import com.tg.model.Message;

@DAO(catalog=DAOConstant.CATALOG)
public interface MessageDAO {

	@SQL("insert into message (id,from_id,to_id,type,content,create_time) values (:1.id,:1.fromId,:1.toId,:1.type,:1.content,:1.createTime)")
	public int insertMessage(Message message);
	
	@SQL("select * from message where to_id=:1 order by id desc limit :2,:3")
	public List<Message> getMessage(int userId,int start,int rows);
}
