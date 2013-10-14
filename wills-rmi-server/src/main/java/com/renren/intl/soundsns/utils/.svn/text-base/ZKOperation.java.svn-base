package com.renren.intl.soundsns.utils;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;

/**
 * @author huangsiping
 * Zookeeper的操作封装
 *
 */
public interface ZKOperation {
	
	

	void destroy() throws InterruptedException;
 

	String getData(String path) throws KeeperException, InterruptedException;

	void changeData(String path, String data) throws KeeperException,
			InterruptedException;

	void delData(String path) throws InterruptedException, KeeperException;

	void delNode(String path) throws InterruptedException, KeeperException;

	boolean exist(String path) throws KeeperException, InterruptedException;

	void apendTempNode(String path, String data) throws KeeperException,
			InterruptedException;

	void apendPresistentNode(String path, String data) throws KeeperException,
			InterruptedException;

	List<String> getChildren(String path) throws KeeperException,
			InterruptedException;
	
	


	void init(String address, String serverName ,Runnable callback) throws IOException;


	void bindHandler(String string, Watcher watcher) throws KeeperException, InterruptedException;

}
