package com.renren.intl.soundsns.utils;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangsiping
 * 
 */
public class ZKTemplate implements ZKOperation {
	// 创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
	protected static Logger logger = LoggerFactory.getLogger(ZKTemplate.class
			.getName());

	private final int DETAULT_TIME_OUT = 4000;

	private ZooKeeper zk = null;
	
	private final int MAX_RETRY = 1000;

	public void init(final String address, final String serverName,final Runnable callback)
			throws IOException {
		// 试两次
		if (zk == null) {
			synchronized (ZooKeeper.class) {
				if (zk == null) {
					logger.info("initing new Zookeeper Client....");
					zk = new ZooKeeper(address, DETAULT_TIME_OUT,
							new Watcher() {

								@Override
								public void process(WatchedEvent event) {
									if (event.getState() == KeeperState.SyncConnected) {

									}

									// session expired,连接超时，重连
									if (event.getState() == KeeperState.Expired) {
										try {
											logger.info("Zookeeper session expired,reconecting......");
											destroy();
											init(address, serverName ,callback);
											
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								}

							});// 如果失败，下次还有成功的机会
					long startTime = System.currentTimeMillis();

					
					
					if(zk!=null ){
						
						int tryCount  = 1 ;
						while(zk.getState()!=States.CONNECTED&&tryCount<=MAX_RETRY){ 
							try {
								logger.info("ZKStatus="+zk.getState().name()+" try "+tryCount+" times create session"); 
								Thread.sleep(2000); 
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								tryCount++;
							}
						}
						if(zk.getState()==States.CONNECTED){
							logger.info("zookeeper session id: " + zk.getSessionId());
							logger.info(" init cost: "
									+ (System.currentTimeMillis() - startTime)
									+ "(ms) ");
							callback.run();
						}else{
							throw new IOException("Zookeeper client not initialized!");
						}
					}
					

				}
			}
		}
	}

	@Override
	public void destroy() throws InterruptedException {
		if (zk != null) {
			logger.info("destroying old client...");
			zk.close();
			zk = null;
		} 
	}

	@Override
	public List<String> getChildren(String path) throws KeeperException,
			InterruptedException {
		if (zk != null) {
			return zk.getChildren(path, true);
		}
		return null;
	}

	@Override
	public String getData(String path) throws KeeperException,
			InterruptedException {
		if (zk != null) {
			// 取得/root/childone节点下的数据,返回byte[]
			byte[] b = zk.getData(path, true, null);
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