package com.wills.proxy.config;

/**
 * @author huangsiping
 * 
 */
public class SimpleConfig {
	private String host;

	private int port;

	public SimpleConfig(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
