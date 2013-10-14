package com.wills.clientproxy;

/**
 * @author huangsiping
 *
 */
public class InstanceKey {
	
	private String nodekey;
	
	private String servicekey;

	public InstanceKey(String nodekey, String servicekey) {
		super();
		if (nodekey == null || "".equals(nodekey.trim()) || servicekey == null
				|| "".equals(servicekey)) {
			throw new RuntimeException("keys can not be null");
		}
		this.nodekey = nodekey;
		this.servicekey = servicekey;
	}

	public String getNodekey() {
		return nodekey;
	}

	public String getServicekey() {
		return servicekey;
	}

}
