package com.wills.redis.benchmark;

public class MTtest {

	public static void main(String[] args) {
		
		int loop=100;
		for(int i=0;i<loop;i++){
			Thread t=new Thread(new RedisClientMThread());
			t.start();
		}
	}
}
