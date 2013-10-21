package com.tg.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog="tg")
public interface AdminTableDAO {
	
	@SQL("replace into admin_table (type,value) values (1,:1)")
	public int setAdminMobile(String mobile);

	@SQL("select value from admin_table where type=1")
	public String getAdminMobile();
}
