package cn.cjp.spider.demo;

import org.apache.log4j.Logger;

public class SynObjectDemo {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SynObjectDemo.class);

	public int num = 0;

	public static void main(String[] args) {
		final SynObjectDemo demo = new SynObjectDemo();

		new Thread(new Runnable() {
			public void run() {
				synchronized (demo) {
					try {
						logger.info(demo.num);
						Thread.sleep(5000);
						logger.info(demo.num);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

		logger.info("---------");
		synchronized (demo) {
			demo.num = 99;
		}
		logger.info(demo.num);

	}

}
