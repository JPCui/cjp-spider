package cn.cjp.spider.demo;

import org.apache.log4j.Logger;

import java.util.Vector;

public class DemoForVector extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DemoForVector.class);

	Vector<Long> pool = null;

	public DemoForVector(Vector<Long> vector) {
		this.pool = vector;
	}

	public void run() {
		logger.info("start");
		synchronized (pool) {
			if (pool.isEmpty()) {
				try {
					pool.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Long num = pool.remove(pool.size() - 1);
			logger.info("remove num=" + num);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Vector<Long> pool = new Vector<Long>();
		int THREAD_NUM = 10;

		DemoForVector[] forVectors = new DemoForVector[THREAD_NUM];
		for (int i = 0; i < THREAD_NUM; i++) {
			forVectors[i] = new DemoForVector(pool);
			forVectors[i].start();
		}

		for (int i = 0; i < THREAD_NUM; i++) {
			synchronized (pool) {
				Long num = System.currentTimeMillis();
				logger.info("add num=" + num);
				pool.add(num);
				pool.notify();
				Thread.sleep(1000);
			}
		}

	}

}
