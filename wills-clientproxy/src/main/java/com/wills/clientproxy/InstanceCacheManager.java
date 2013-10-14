package com.wills.clientproxy;
 

/**
 * @author huangsiping
 *
 */
public interface InstanceCacheManager {
	
	public Object getServiceInstance(InstanceKey key) ;
 
	void putServiceInstance(InstanceKey key, Object instance);
 

}
