package com.wills.redis.pool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.tg.util.CONFIGUtil;

/**
 * Factory传入一组host:ip。如果其中一个挂了，可自动切换到其他正常的。
 * @author wills
 *
 */
public class RedisPoolFactory {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RedisPoolFactory.class);
	
	private String redisServer;
	
	private List<RedisServer> redisServerList=new ArrayList<RedisServer>();
	
	private JedisPool curjedisPool;
	
	private long rrsed=System.currentTimeMillis();//随机种子，roundrobin初始轮训种子。避免多个客户端
	
	private static final int redisTimeout=3000;
	
	public RedisPoolFactory(){
		//通过配置中心获取配置OO
		redisServer=CONFIGUtil.getInstance().getConfig("redis_server");
		initRedisServerList();	
	}
	
	static private JedisPoolConfig getJedisPoolConfig(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(1000);
		config.setMaxIdle(100);
		config.setMaxWait(10000);
		return config;
	}
	
	
	private void initRedisServerList(){
		String[] redisServersStr=redisServer.split(",");
		for(String redisServerStr:redisServersStr){
			String[] redis=redisServerStr.split(":");
			RedisServer redisServer=new RedisServer();
			redisServer.host=redis[0];
			redisServer.port=Integer.valueOf(redis[1]);
			redisServerList.add(redisServer);
		}
	}
	
	 private RedisServer getRandomServer(){
		int serverLen=redisServerList.size();
		return redisServerList.get(new Long(++rrsed%serverLen).intValue());
	}
	
	 private boolean isAvaliable(JedisPool jedisPool){
		boolean flag=false;
		try{
			jedisPool.returnResource(jedisPool.getResource());
			flag=true;
		}catch(Exception e){
			flag=false;
		}
		return flag;
	}
	
	 private JedisPool getOneValiableRedisPool(){
		JedisPool jedisPool=null;
		do{
			RedisServer redisServer=getRandomServer();
			jedisPool=new JedisPool(getJedisPoolConfig(),redisServer.host,redisServer.port,redisTimeout);
			logger.info("check redispool is avaliable,"+redisServer.host+":"+redisServer.port);
			if(isAvaliable(jedisPool)){
				logger.info("i have got a avaliable redispool "+redisServer.host+":"+redisServer.port);	
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}while(true);
		
		return jedisPool;
	}
	
	 public JedisPool getRedisPool(){
		if(curjedisPool!=null)
			return curjedisPool;
		else 
			return curjedisPool= getOneValiableRedisPool();
	}
	
	 public JedisPool changeRedisPool(){
		curjedisPool.destroy();
		curjedisPool=getOneValiableRedisPool();
		return curjedisPool;
		
	}
	
	private class RedisServer{
		public String host;
		public int port;
	}
}
