package cn.cjp.spider.demo;

import org.apache.log4j.Logger;

import java.util.Vector;
/**
 * 同步Vector
 * @author REAL
 *
 */
public class DemoForVector2 {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DemoForVector2.class);

	Vector<Long> pool = null;

	public DemoForVector2(Vector<Long> vector) {
		this.pool = vector;
	}

	public void remove() {
		logger.info("start");
		synchronized (pool) {
			while (pool.isEmpty()) {
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

	public void add() throws InterruptedException {
		synchronized (pool) {
			Long num = System.currentTimeMillis();
			logger.info("add num=" + num);
			pool.add(num);
			pool.notify();
			Thread.sleep(1000);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Vector<Long> pool = new Vector<Long>();
		int THREAD_NUM = 10;

		final DemoForVector2 forVector = new DemoForVector2(pool);

		for (int i = 0; i < THREAD_NUM; i++) {
			new Thread(new Runnable() {
				public void run() {
					forVector.remove();
				}
			}).start();
		}

		for (int i = 0; i < THREAD_NUM; i++) {
			forVector.add();
		}

	}

}
