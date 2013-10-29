package com.tg.util;

public class A {

	static String format="这是汉字汉字 %s 是吗";
	
	public static void main(String[] args) {

		String ssString="4259406869675991584@#$1106758028279122370";
		String[] strssStrings= ssString.split("\\@\\#\\$");
		System.out.println(strssStrings[0]);
		System.out.println(strssStrings[1]);
		
	}
	
	 static class Test{
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
}
