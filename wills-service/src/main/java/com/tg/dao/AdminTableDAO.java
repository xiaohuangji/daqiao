package com.tg.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog="tg")
public interface AdminTableDAO {
	
	@SQL("update admin_table set value=:1 where type=1")
	public int setAdminMobile(String mobile);

	@SQL("select value from admin_table where type=1")
	public String getAdminMobile();
}
