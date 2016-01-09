package cn.cjp.demo;


public class Demo2 extends Thread{
	
	Integer n;
	public Demo2(Integer n){
		this.n = n;
	}
	
	public void run(){
		synchronized (n) {
			try {
				n.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Integer n = 1;		
		
		Demo2 demo2 = new Demo2(n);
		demo2.start();
		
		Thread.sleep(200);
		
		synchronized (n) {
			n ++;
			n.notify();
		}
		
	}
	
	
}
