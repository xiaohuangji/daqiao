package com.renren.intl.soundsns.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tg.util.CONFIGUtil;

public class LoadConfig implements ServletContextListener{
    private static final String DB_END_POINTS = "endpoints";
    private static final String ZK_REGISTRY = "zk_registry";
    private static final String DB_STORM_ZK_ROOT = "dbstorm_zkroot";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
        CONFIGUtil configUtil=CONFIGUtil.getInstance();
		Assert.hasLength(configUtil.getConfig("endpoints"),"dbstorm endpoints should not be null or empty, please check zk and config the endpoints properites");
		Assert.hasLength(configUtil.getConfig("zk_registry"),"dbstorm zk_registry should not be null or empty, please check zk and config the zk_registry properites");
		Assert.hasLength(configUtil.getConfig("dbstorm_zkroot"),"dbstorm dbstorm_zkroot should not be null or empty, please check zk and config the dbstorm_zkroot properites");
		
    	System.setProperty(DB_END_POINTS, configUtil.getConfig("endpoints"));      	
    	System.setProperty(ZK_REGISTRY, configUtil.getConfig("zk_registry"));
    	System.setProperty(DB_STORM_ZK_ROOT, configUtil.getConfig("dbstorm_zkroot"));
    	
	}
}
	