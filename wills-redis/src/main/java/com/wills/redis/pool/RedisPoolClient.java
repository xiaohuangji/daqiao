package com.wills.redis.pool;

import java.sql.Ref;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

/*
 * @xingxing.feng
 */
public class RedisPoolClient implements JedisCommands {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RedisPoolClient.class);

	private RedisPoolFactory factory;

	private JedisPool jedisPool;
	
	private int changeFlag=0;
	
	private int changeCount=5;//redispool.getresource失败达到这个次数后更新redispool。
	
	private static volatile boolean isChanging=false;
	
	private RedisPoolClient() {
		factory=new RedisPoolFactory();
		jedisPool = factory.getRedisPool();
	}

	private static class RedisPoolClientFactory{
		private static RedisPoolClient instance=new RedisPoolClient();
	}
	
	public static RedisPoolClient getInstance(){
		return RedisPoolClientFactory.instance;
	}
	
	//选新的连接池
	//应该是惰性的。不能一次失败就更换jedispool
	private void checkAndChange(){
		if(isChanging==true){
			logger.info("redispool is changing ,return ");
			return ;
		}
		if(changeFlag > changeCount){
			isChanging=true;
	        changeFlag=0;
			logger.info("start checkAndChange jedisPool");
	      /*  FutureTask<Object> future = new FutureTask<Object>(new AsynCheckAndChange(jedisPool,factory));
	        ExecutorService executor = Executors.newCachedThreadPool();
	        executor.execute(future);
	        executor.shutdown();*/
			jedisPool.destroy();
			jedisPool = factory.changeRedisPool();
			logger.info("checkAndChange jedisPool finish");
			isChanging=false;
		}else {
			changeFlag++;
			logger.info("checkAndChange wait  : "+changeFlag);
		}
	}
	
	class AsynCheckAndChange implements Callable{

		private JedisPool jedisPool;
		private RedisPoolFactory factory;
		
		public AsynCheckAndChange(JedisPool jedisPool,RedisPoolFactory factory){
			this.jedisPool=jedisPool;
			this.factory=factory;
		}
		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			jedisPool.destroy();
			jedisPool = factory.changeRedisPool();
			return null;
		}
		
	}
	
	private void resourceErrorHandle(Jedis jedis, Exception e) {
		try {
			if (jedisPool != null) {
				jedisPool.returnBrokenResource(jedis);
			    checkAndChange();
			}
		} catch (Exception e1) {
			logger.error("change redisPool error",e1);
		}
	}

	@Override
	public String set(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.set(s, s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}

	@Override
	public String get(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.get(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}
	
	
	public List<String> mget(String... keys) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r = jedis.mget(keys);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}
	
	public Long del(String... s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.del(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}
	

	@Override
	public Boolean exists(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean r = jedis.exists(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}

	@Override
	public String type(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.type(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long expire(String s, int i) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.expire(s, i);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long expireAt(String s, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.expireAt(s, l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long ttl(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.ttl(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Boolean setbit(String s, long l, boolean flag) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean r = jedis.setbit(s,l,flag);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}
		return null;
	}

	@Override
	public Boolean getbit(String s, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean r = jedis.getbit(s, l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long setrange(String s, long l, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.setrange(s, l, s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String getrange(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.getrange(s, l, l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String getSet(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.getSet(s, s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long setnx(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.setnx(s, s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String setex(String s, int i, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.setex(s,i, s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long decrBy(String s, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.decrBy(s,l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long decr(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.decr(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long incrBy(String s, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.incrBy(s,l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long incr(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.incr(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long append(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.append(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String substr(String s, int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.substr(s,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long hset(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.hset(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String hget(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.hget(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long hsetnx(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.hsetnx(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String hmset(String s, Map<String, String> map) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.hmset(s,map);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public List<String> hmget(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r= jedis.hmget(s, as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long hincrBy(String s, String s1, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.hincrBy(s,s1,l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Boolean hexists(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean r = jedis.hexists(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long hdel(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.hdel(s,as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long hlen(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.hlen(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> hkeys(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.hkeys(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public List<String> hvals(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r = jedis.hvals(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Map<String, String> hgetAll(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Map<String, String> r = jedis.hgetAll(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long rpush(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.rpush(s, as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long lpush(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.lpush(s,as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}	

	@Override
	public Long llen(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.llen(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public List<String> lrange(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r = jedis.lrange(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String ltrim(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.ltrim(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String lindex(String s, long l) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.lindex(s,l);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String lset(String s, long l, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.lset(s,l,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long lrem(String s, long l, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.lrem(s,l,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String lpop(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.lpop(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String rpop(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.rpop(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long sadd(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.sadd(s,as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> smembers(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.smembers(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long srem(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.srem(s,as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String spop(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.spop(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long scard(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.scard(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Boolean sismember(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Boolean r = jedis.sismember(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public String srandmember(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String r = jedis.srandmember(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zadd(String s, double d, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zadd(s,d,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zadd(String s, Map<Double, String> map) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zadd(s,map);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrange(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrange(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zrem(String s, String... as) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zrem(s,as);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Double zincrby(String s, double d, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Double r = jedis.zincrby(s,d,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zrank(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zrank(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zrevrank(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zrevrank(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrevrange(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrevrange(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrangeWithScores(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrangeWithScores(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrevrangeWithScores(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zcard(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zcard(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Double zscore(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Double r = jedis.zscore(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public List<String> sort(String s) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r = jedis.sort(s);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public List<String> sort(String s, SortingParams sortingparams) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> r = jedis.sort(s,sortingparams);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zcount(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zcount(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zcount(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zcount(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrangeByScore(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrangeByScore(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrangeByScore(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrangeByScore(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrevrangeByScore(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrangeByScore(String s, double d, double d1, int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrangeByScore(s,d,d1,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrevrangeByScore(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrangeByScore(String s, String s1, String s2, int i,
			int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrangeByScore(s,s1,s2,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String s, double d, double d1, int i,
			int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrevrangeByScore(s,d,d1,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrangeByScoreWithScores(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrevrangeByScoreWithScores(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String s, double d, double d1,
			int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrangeByScoreWithScores(s,d,d1,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String s, String s1, String s2, int i,
			int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> r = jedis.zrevrangeByScore(s,s1,s2,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrangeByScoreWithScores(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrevrangeByScoreWithScores(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String s, String s1, String s2,
			int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrangeByScoreWithScores(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String s, double d, double d1,
			int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrevrangeByScoreWithScores(s,d,d1,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String s, String s1,
			String s2, int i, int j) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<Tuple> r = jedis.zrevrangeByScoreWithScores(s,s1,s2,i,j);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zremrangeByRank(String s, long l, long l1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zremrangeByRank(s,l,l1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zremrangeByScore(String s, double d, double d1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zremrangeByScore(s,d,d1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long zremrangeByScore(String s, String s1, String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.zremrangeByScore(s,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long linsert(String s, LIST_POSITION list_position, String s1,
			String s2) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.linsert(s,list_position,s1,s2);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long lpushx(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.lpushx(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

	@Override
	public Long rpushx(String s, String s1) {
		// TODO Auto-generated method stub
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long r = jedis.rpushx(s,s1);
			jedisPool.returnResource(jedis);
			return r;
		} catch (Exception e) {
			resourceErrorHandle(jedis, e);
		}

		return null;
	}

}