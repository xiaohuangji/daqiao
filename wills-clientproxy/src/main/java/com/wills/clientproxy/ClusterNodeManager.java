package com.wills.clientproxy;

import java.util.List;
import java.util.Set;

/**
 * @author huangsiping
 *
 */
public interface ClusterNodeManager {
	
	public ClusterServiceRegistry getClusterServiceRegistry();

	void refreshClusterNodes(List<String> nodes);
	
	void addNode(String node);
	
	void removeNode(String nodeName);
	
	public NodeChangeHandler getGroupChildrenChangeHandler();
	
	public NodeChangeHandler getNodeDataChangeHandler();

	HessianClusterNode getAvailableNodeByStraitegy();
	
	public HessianClusterStateHandler getClusterStateHandler();

	void init() throws Exception;

	Set<String> getManagedNodeNames();

	void reconnect() throws Exception;

}
