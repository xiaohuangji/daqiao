package com.tg.util;

public class A {

	static void fun(testO o){
		o.setB(999);
	}
	
	public static void main(String[] args) {
		testO o=new testO();
		o.setA(10);
		fun(o);
		System.out.println(o.getB());
		
	}
	
	static class testO{
		int a;
		int b=0;
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
		
	}
}
