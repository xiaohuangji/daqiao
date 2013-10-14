package com.mid.t2.redis;

import java.util.List;
import java.util.Map;


public class Person{
	private int id;
	
	private String name;
	
	private boolean gender;
	
	private List<Integer> list;
	
	private Map<String,Integer> map;

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "person [id=" + id + ", name=" + name + ", gender=" + gender
				+ "]";
	}
	
	
}
