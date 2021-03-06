package com.wills.clientproxy.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangsiping
 * 
 */
public class ZKTemplate implements ZKOperation {
	protected static Logger logger = LoggerFactory.getLogger(ZKTemplate.class
			.getName());

	private final int DETAULT_TIME_OUT = 20000;

	private static ZooKeeper zk = null;

	private final int MAX_RETRY = 1000;

	/*  
	 *  初始化ZK，加入重连机制
	 */
	@Override
	public void init(String address, String serverName, Watcher watcher)
			throws IOException {
		if (zk == null) {
			zk = new ZooKeeper(address, DETAULT_TIME_OUT, watcher);
			long startTime = System.currentTimeMillis();
			if (zk != null) {

				int tryCount = 1;
				while ((zk.getState() != States.CONNECTED || zk.getSessionId() == 0)
						&& tryCount <= MAX_RETRY) {
					try {
						logger.info("ZKStatus=" + zk.getState().name()
								+ " try " + tryCount + " times create session");
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						tryCount++;
					}
				}
				if (zk.getState() == States.CONNECTED && zk.getSessionId() != 0) {
					logger.info("zookeeper session id: " + zk.getSessionId());
					logger.info(" init cost: "
							+ (System.currentTimeMillis() - startTime)
							+ "(ms) ");
				} else {
					throw new IOException("Zookeeper client not initialized!");
				}

			}
		} else if (zk != null && zk.getState() == States.CONNECTED
				&& zk.getSessionId() != 0) {
			logger.info("Zk instance already exist");
		} else {
			logger.info("Zk instance exists,buy not connected.............");

		}
	}

	@Override
	public void destroy() throws InterruptedException {
		if (zk != null) {
			zk.close();
		}
		zk = null;
	}

	@Override
	public List<String> getChildren(String path, Watcher watcher)
			throws KeeperException, InterruptedException {
		if (zk != null) {
			return zk.getChildren(path, watcher);
		}
		return null;
	}

	@Override
	public String getData(String path, Watcher watcher) throws KeeperException,
			InterruptedException {
		if (zk != null && zk.getState() == States.CONNECTED
				&& zk.getSessionId() != 0) {
			// 取得/root/childone节点下的数据,返回byte[]
			byte[] b = zk.getData(path, watcher, null);
			return new String(b);
		}
		return null;
	}

	@Override
	public void changeData(String path, String data) throws KeeperException,
			InterruptedException {
		if (zk != null) {
			// 修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
			zk.setData(path, data.getBytes(), -1);
		}
	}

	@Override
	public void delData(String path) throws InterruptedException,
			KeeperException {
		if (zk != null) {
			// 删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
			zk.delete(path, -1);
		}
	}

	@Override
	public void delNode(String path) throws InterruptedException,
			KeeperException {
		if (zk != null) {
			zk.delete(path, -1);
		}
	}

	@Override
	public boolean exist(String path) throws KeeperException,
			InterruptedException {
		if (zk != null) {
			return zk.exists(path, true) != null;
		}
		return false;
	}

	@Override
	public void apendTempNode(String path, String data) throws KeeperException,
			InterruptedException {
		if (zk != null) {
			zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL);
		}
	}

	@Override
	public void apendPresistentNode(String path, String data)
			throws KeeperException, InterruptedException {
		if (zk != null) {
			zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}
	}

	@Override
	public void bindHandler(String path, Watcher watcher)
			throws KeeperException, InterruptedException {
		if (zk != null) {
			zk.exists(path, watcher);
		}
	}
}