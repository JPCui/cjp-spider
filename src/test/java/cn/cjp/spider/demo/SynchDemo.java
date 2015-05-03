package cn.cjp.spider.demo;

public class SynchDemo {
	
	public void run(){
		synchronized (this) {
			System.out.println(111);
			return;
		}
	}

	public static void main(String[] args) {
		
		SynchDemo demo = new SynchDemo();
		demo.run();
		demo.run();
		demo.run();
	}
	
}
