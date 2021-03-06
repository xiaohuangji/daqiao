package com.wills.clientproxy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wills.clientproxy.zookeeper.HessianClusterZookeeperFacade;
 

/**
 * @author huangsiping
 *
 */
public class HessianClusterNodeDataChangeHandler implements NodeChangeHandler {
   
	private final int DEFAULT_READ_TIMEOUT = 3000;
	private final String MARK = "Server Activation Page";
	
	private static final Logger logger = LoggerFactory
			.getLogger(HessianClusterNodeDataChangeHandler.class);
	
	private ClusterNodeManager clusterNodeManager;
	
	public HessianClusterNodeDataChangeHandler(
			ClusterNodeManager clusterNodeManager) {
		super(); 
		this.clusterNodeManager = clusterNodeManager;
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("data---"+event.toString());
		//同时观察 data change 和 delete
		String path = event.getPath();
		if(path==null){
			logger.info("no path found in event");
			return;
		}
		String nd = path.substring(path.lastIndexOf("/")+1, path.length());
 		if(event.getType().equals(Watcher.Event.EventType.NodeDataChanged)){
			//getData会解除监听器绑定，需要重绑
			String dt = HessianClusterZookeeperFacade.getInstance().getNodeData(path);
			try {
				HessianClusterZookeeperFacade.getInstance().bindClusterNodeChangeHandler(path, clusterNodeManager.getNodeDataChangeHandler());
			} catch (KeeperException e) {
				logger.error(e.getMessage());
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
			if(Constants.ENABLE_STATUS.equals(dt)){
				clusterNodeManager.addNode(nd); 
			}else if(Constants.DISABLE_STATUS.equals(dt)){
				clusterNodeManager.removeNode(nd);
			} 
		}
		if (event.getType().equals(Watcher.Event.EventType.NodeDeleted)) {
			// 首先访问一下主机，如果还有响应，则暂缓从本地缓存中移除
			if (this.isNodeStillOk(nd) == false) {
				clusterNodeManager.removeNode(nd);
			}
		}
		
	}

	public boolean isNodeStillOk(String nodeName) {
		String urlString = new StringBuffer("http://").append(nodeName.replace("_", ":")).append("/list").toString();
		BufferedReader in = null;
		StringBuffer content = new StringBuffer();
		boolean flag = true;
		try {
			String line ;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			// 读取URL连接的网络资源
			connection.setReadTimeout(DEFAULT_READ_TIMEOUT); 
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream())); 
			while ((line = in.readLine()) != null) {
				content.append(line);
			} 
		} catch (Exception e) {
			flag = false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		if(content.indexOf(MARK)>-1){
			flag = true;
		}
		return flag;
	} 
 
}