package com.wills.proxy.config;

/**
 * @author huangsiping
 *
 */
public class SimpleRegistry {
	private String urlContext = null;

	public String getHessianUrlContext() {
		return urlContext;
		
	}

	public SimpleRegistry(String urlContext) {
		super();
		this.urlContext = urlContext;
	}
	

}
