package cn.cjp.spider.demo;

public class Test extends Parent{

	public Test(){
		System.out.println(Test.class);
	}
	
	public static void main(String[] args) {
		new Test();
	}
	
	
	
}
class Parent
{
	public Parent() {
		System.out.println(Parent.class);
	}
}