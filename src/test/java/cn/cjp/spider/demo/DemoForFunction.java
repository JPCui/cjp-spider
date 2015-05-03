package cn.cjp.spider.demo;

import org.apache.log4j.Logger;

/**
 * 同步函数
 * @author REAL
 *
 */
public class DemoForFunction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DemoForFunction.class);

	Integer count = 0;

	public synchronized void add() {
		// count>5的时候，线程开始等待
		if (count > 5) {
			try {
				long time = System.currentTimeMillis() % 1000;

				logger.info(time + " add-count=" + count + " 等待");
				 count.wait();
//				this.wait();
				logger.info(time + " add-count=" + count + " 苏醒");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		count++;

	}

	public synchronized void sub() {
		logger.info("sub-count=" + count + "唤醒");
		count--;

		if (count <= 5) {
			 count.notify();
//			this.notify();
		}
	}

	public static void main(String[] args) {
		final DemoForFunction demo = new DemoForFunction();

		/**
		 * count>5开始等待，所以最多有5个等待
		 */
		for (int i = 1; i <= 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					logger.info("I am coming");
					demo.add();
					logger.info("I am back");
				}
			}).start();
		}

		for (int i = 1; i <= 6; i++) {
			demo.sub();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
