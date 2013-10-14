/*package com.mid.t2.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import com.mid.t2.redis.Person;

import com.mid.t2.redis.client.RedisClient;

*//**
 * Unit test for simple App.
 *//*
public class AppTest 
{
	static RedisClient redisClient=new RedisClient("test");

	@Test
	@Ignore
    public void testSet()
    {
    	redisClient.set("1", 1);
    	int actual=redisClient.get("1",Integer.class);
    	assertEquals(1, actual);
    }
    
    @Ignore
    @Test
    public void testSetex() throws InterruptedException
    {
    	redisClient.setex("2", 2, 5);
    	Integer actual=redisClient.get("2",Integer.class);
    	assertNotNull(actual);
    	Thread.sleep(5000);
    	actual=redisClient.get("2",Integer.class);
    	assertNull(actual);
    }
    
    @Ignore
    @Test
    public void testRpush(){
    	List<Long> values=new ArrayList<Long>();
    	values.add(1000L);
    	values.add(2000L);
    	values.add(3000L);
    	values.add(4000L);
    	redisClient.del("3");
    	redisClient.rpush("3", values);
    	List<Long> actualList=redisClient.lrange("3", 0, -1,Long.class);
    	long len=redisClient.llen("3");
    	assertEquals(values.size(),actualList.size());
    	
    	assertEquals(actualList.get(0).longValue(), 1000);
    	assertEquals(actualList.get(new Long(len-1).intValue()).longValue(), 4000);
    }
    
    @Test
    @Ignore
    public void testSadd(){
    	Set<Long> values=new HashSet<Long>();
    	values.add(1001L);
    	values.add(1002L);
    	values.add(1003L);
    	redisClient.del("4");
    	redisClient.sadd("4", values);
    	
    	Set<Long> actualSet=redisClient.smember("4",Long.class);
    	assertEquals(values.size(), actualSet.size());
    	
    	assertTrue(actualSet.contains(1002L));
    }
    
    @Test
    @Ignore
    public void testZadd(){
    	String key="5";
    	Map<Double,Long> map=new HashMap<Double,Long>();
    	map.put(1.0d, 111L);
    	map.put(2.0d, 222L);
    	map.put(3.0d, 333L);
    	map.put(4.0d, 444L);
    	redisClient.del(key);
    	redisClient.zadd(key, map);
    	Long len=redisClient.zcard(key);
    	assertEquals(map.size(), len.intValue());
    	Set<Long> zset=redisClient.zrange(key, 0, 1, Long.class);
    	assertEquals(2, zset.size());
    	assertTrue(zset.contains(111L));
    	
    	zset=redisClient.zrangeByScore(key, 2.0, 3.0, Long.class);
    	assertEquals(2, zset.size());
    	assertFalse(zset.contains(111L));
    }
    
    @Test
    @Ignore
    public void testT(){
    	Person person=new Person();
    	person.setId(10086);
    	person.setGender(true);
    	person.setName("iamtester");
    	
    	List<Integer> list=new ArrayList<Integer>();
    	list.add(1);list.add(2);list.add(3);
    	person.setList(list);
    	
    	Map<String,Integer> map=new HashMap<String, Integer>();
    	map.put("1",1);map.put("2",2);map.put("3",3);
    	person.setMap(map);
    	
    	String key="6";
    	redisClient.del(key);
    	redisClient.set(key,person);
    	Person actual=redisClient.get(key,Person.class);
    	
    	assertEquals(person.getId(), actual.getId());
    	assertEquals(person.getName(), actual.getName());
    	assertEquals(person.isGender(), actual.isGender());
    	assertEquals(person.getList().get(0), actual.getList().get(0));
    	assertEquals(person.getMap().get("1"), actual.getMap().get("1"));
    }
    
    @Test
    @Ignore
    public void testMap(){
    	String key="7";
    	//redisClient.del(key);
    	Map<String ,Object> map=new HashMap<String, Object>();
    	map.put("1", 1);
    	map.put("2", 2);
    	map.put("3", 100);
    	System.out.println(redisClient.hmset(key, map));
    	System.out.println(redisClient.hincrBy(key, "1", -1000));
    	Map<String, String> rmap=redisClient.hgetAll(key);
    	System.out.println(rmap);
    }
    
    @Ignore
    @Test
    public void testList(){
    	String key ="8";
    	List<Long> values=new ArrayList<Long>();
    	values.add(1000L);
    	values.add(2000L);
    	values.add(3000L);
    	values.add(4000L);
    	values.add(4000L);
    	redisClient.del(key);
    	redisClient.rpush(key, values);
    	redisClient.lrem(key, 0, 4000L);
    	List<Long> actual=redisClient.lrange(key, 0, -1, Long.class);
    	assertEquals(actual.size(), values.size()-2);
    	
    }
    
    public static void main(String[] args) {
    	List<String> keys=new ArrayList<String>();
    	for(int i=0;i<=10;i++){
    		Person person=new Person();
        	person.setId(i);
        	person.setGender(true);
        	person.setName("iamtester");
    		redisClient.set("mget"+i, person);
    		keys.add("mget"+i);
    	}
    	
    	List<Person> list=redisClient.mget(keys, Person.class);
    	for(Person p:list){
    		System.out.println(p.getId());
    	}
    	
    	
	}
    
}
*/