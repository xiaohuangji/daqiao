package com.tg.admin.server.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class LoadConfig implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub     
    	System.out.println("============================");
	}
}
	