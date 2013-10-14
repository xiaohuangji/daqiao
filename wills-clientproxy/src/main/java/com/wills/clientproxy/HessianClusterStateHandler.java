package com.wills.clientproxy;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wills.clientproxy.zookeeper.HessianClusterZookeeperFacade;
 

/**
 * @author huangsiping
 *
 */
public class HessianClusterStateHandler implements NodeChangeHandler {
   
	private static final Logger logger = LoggerFactory
			.getLogger(HessianClusterStateHandler.class);
	
	private ClusterNodeManager clusterNodeManager;
	
	public HessianClusterStateHandler(
			ClusterNodeManager clusterNodeManager) {
		super(); 
		this.clusterNodeManager = clusterNodeManager;
	}

	@Override
	public void process(WatchedEvent event) {
		 
		if (event.getState() == KeeperState.Expired) {  
			logger.info("Session expired ,reconnecting......"); 
			try {
				HessianClusterZookeeperFacade.getInstance().reconnect(clusterNodeManager.getClusterStateHandler());
				clusterNodeManager.reconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		}
		
		
		
	}

}
