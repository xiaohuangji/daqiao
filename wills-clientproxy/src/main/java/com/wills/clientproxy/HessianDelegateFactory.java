package com.wills.clientproxy;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianDefaultProxyFactory;
import com.wills.proxy.config.SimpleRegistry;


/**
 * @author huangsiping
 *
 */
public class HessianDelegateFactory {

	protected static Logger logger = LoggerFactory
			.getLogger(HessianDelegateFactory.class.getName());

	/**
	 * single instance
	 */
	private final static HessianDelegateFactory m_instance = new HessianDelegateFactory();

	private HessianDelegateFactory() {
	}

	@SuppressWarnings("unchecked")
	public <T> T create(SimpleRegistry registry, Class<T> serviceInterface)
			throws MalformedURLException {

		HessianDefaultProxyFactory factory = HessianDefaultProxyFactory.getInstance();
		String url = "http://"
				+ registry.getHessianUrlContext().replace("_", ":") + "/"
				+ "hessian/" + serviceInterface.getSimpleName();

		
		T instance = (T) factory.create(serviceInterface, url);
		return instance;

	}
	
	@SuppressWarnings("unchecked")
	public <T> T create( Class<T> serviceInterface,ClusterNodeManager cm)
			throws MalformedURLException {

		HessianLBProxyFactory factory = HessianLBProxyFactory.getInstance();
		
		T instance = (T) factory.create(serviceInterface, cm);
		return instance;

	}
	

	/**
	 * 该方法创建的代理能在实例方法调用过程中实现LB和Failover,即只生成一个对象，调用方法即可
	 * @param registry
	 * @param serviceInterface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T retrieveService(ClusterServiceRegistry registry,
			Class<T> serviceInterface){
		Object instance = null;
		try {
			ClusterNodeManager cm = ClusterNodeBuilder
					.buildClusterNodeManager(registry);
			String availableNode = null;
			try {
				availableNode = cm.getAvailableNodeByStraitegy().getNode();
			} catch (Exception e) {
				logger.error("retrieve failed",e);
			}
			if (availableNode == null) {
				//throw new Exception("no server available");
				this.logger.warn("currently no  available server found on cluster!");
				return this.create(serviceInterface, cm);
				
			}
			 
			InstanceKey key = new InstanceKey(availableNode, serviceInterface.getSimpleName());

			InstanceCacheManager im = (InstanceCacheManager) cm;
			instance = im.getServiceInstance(key);

			if (instance == null) {
				instance = this.create(serviceInterface, cm);
			}
			im.putServiceInstance(key, instance);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return instance == null ? null : (T) instance;

	}

	/**
	 * 改方法获得的代理无法在不重新实例化的情况下LB，要实现LB,必须每次重新获得实例
	 * @param registry
	 * @param serviceInterface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(ClusterServiceRegistry registry,
			Class<T> serviceInterface){
		Object instance = null;
		try {
			ClusterNodeManager cm = ClusterNodeBuilder
					.buildClusterNodeManager(registry);
			String availableNode = cm.getAvailableNodeByStraitegy().getNode() ;
			if (availableNode == null) {
				throw new Exception("no server available");
			}
			 
			InstanceKey key = new InstanceKey(availableNode, serviceInterface.getSimpleName());

			InstanceCacheManager im = (InstanceCacheManager) cm;
			instance = im.getServiceInstance(key);

			if (instance == null) {
				SimpleRegistry simpleRegistry = new SimpleRegistry(
						key.getNodekey());
				
				instance = this.create(simpleRegistry, serviceInterface);
			} 
			im.putServiceInstance(key, instance);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return instance == null ? null : (T) instance;

	}	
	
	
	public static HessianDelegateFactory getInstance() {
		return m_instance;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T create(SimpleRegistry registry, Class<T> serviceInterface,long timeout,long readtimeout)
			throws MalformedURLException {

		HessianDefaultProxyFactory factory = HessianDefaultProxyFactory.getInstance();
		factory.setConnectTimeout(timeout);
		factory.setReadTimeout(timeout);
		String url = "http://"
				+ registry.getHessianUrlContext().replace("_", ":") + "/"
				+ "hessian/" + serviceInterface.getSimpleName();	
		T instance = (T) factory.create(serviceInterface, url);
		return instance;

	}
	

}
