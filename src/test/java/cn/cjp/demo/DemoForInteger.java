package cn.cjp.demo;

import org.apache.log4j.Logger;

/**
 * 以Integer作同步线程的实例<br>
 * 
 * @author REAL
 * 
 */
public class DemoForInteger extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DemoForInteger.class);

	static Integer count = null;

	public DemoForInteger(Integer count) {
		DemoForInteger.count = count;
	}

	public void run() {
		logger.info("start");
		synchronized (count) {
			// count>5的时候，线程开始等待
			while (count <= 0) {
				try {
					count.wait();
					// this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			count--;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Integer count = 0;
		System.out.println(count);
		Integer THREAD_NUM = 10;
		DemoForInteger[] demos = new DemoForInteger[THREAD_NUM];
		for (Integer i = 0; i < THREAD_NUM; i++) {
			demos[i] = new DemoForInteger(count);
			demos[i].start();
		}

		for (Integer i = 1; i <= THREAD_NUM; i++) {
			synchronized (count) {
				logger.info("sub-count=" + count + "唤醒");
				count++; //////////////这里count++执行完毕，本身已经之前的对象了，等于 new Integer(count)+1
				count.notify();
				// this.notify();
				Thread.sleep(1000);
			}
		}
	}

}
