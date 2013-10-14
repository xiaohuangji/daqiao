package com.wills.redis.benchmark;

import com.wills.redis.client.RedisClient;

public class RedisClientMThread implements Runnable {

	RedisClient client=new RedisClient("test");
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int loopnum=10000;
		int count=loopnum;
		
		long d=System.currentTimeMillis();
		while(loopnum-- > 0 ){
			
			client.del(loopnum+"");
			//int re=client.get(loopnum+"", Integer.class);
		}
		
		System.out.print((System.currentTimeMillis()-d)/count+" ");
		
	}

}
