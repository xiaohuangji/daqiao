package com.wills.clientproxy;

/**
 * @author huangsiping
 *
 */
public class HessianClusterNode {

	private String nodeName = ""; 
	
	private String url = null;
	
	
	public HessianClusterNode(String node) {
		this.nodeName = node;
		url = new StringBuffer("http://").append(nodeName.replace("_", ":")).append("/hessian/").toString();
	 
	}
	
	public String getNode(){
		return this.nodeName;
	}

	public String getURL() { 
		 
		 return url;
	}
	

}
