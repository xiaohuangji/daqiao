package com.tg.dao;

import com.tg.model.UserDevice;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;

@DAO(catalog="tg")
public interface UserDeviceDAO {

	@SQL("replace into user_device_token (user_id,device_type,device_token) values (:1,:2,:3)")
	public int addDevice(int userId,int deviceType,String deviceToken);
	
	@SQL("delete from user_device_token where user_id=:1")
	public int removeDevice(int userId);
	
	@SQL("select * from user_device_token where user_id=:1")
	public UserDevice getDevice(int userId);
}
