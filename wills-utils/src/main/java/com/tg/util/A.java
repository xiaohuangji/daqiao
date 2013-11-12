package com.tg.util;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;


public class A {

	static String format="这是汉字汉字 %s 是吗";
	
	public static void main(String[] args) throws UnknownHostException {

		Test test=new Test();
		Class<? extends Test> class1=test.getClass();
		//System.out.println(class1);
		
		System.out.println(class1.getModifiers());
		 TypeVariable<?>[] tv=class1.getTypeParameters();
		 System.out.println(tv.length);
		for (TypeVariable t : tv)
		    System.out.println(t.getName());
		
		Class ss=Testtest.class.getSuperclass();
		System.out.println(ss.getName());
		
		Class[] sss=class1.getInterfaces();
		System.out.println(Arrays.toString(sss));
		
		System.out.println(((Inet4Address)Inet4Address.getByName("localhost")).getHostAddress());
	}
	
	
	 static  class Test<T>implements Serializable{
		 T t;
		Double double1;
		Testtest testtest;
		int a;
		int b=0;
		String name;
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}	
	}
	 
	 static class Testtest extends Test{
		 private int c;
	 }
}
