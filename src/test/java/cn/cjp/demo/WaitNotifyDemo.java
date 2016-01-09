package cn.cjp.demo;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyDemo{

	static List<String> strList = new ArrayList<String>();
	
	public void waiting() throws InterruptedException{
		synchronized (strList) {
			System.out.println("休眠");
			strList.wait();
			System.out.println("休眠结束");
		}
	}
	
	public void notifing(){
		synchronized (strList) {
			System.out.println("被唤醒");
			strList.notify();
			System.out.println("被唤醒结束");
		}
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		final WaitNotifyDemo demo = new WaitNotifyDemo();
		new Thread(new Runnable() {
			public void run() {
				try {
					demo.waiting();
				} catch (InterruptedException e) {
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				demo.notifing();
			}
		}).start();
		
	}
	
}
