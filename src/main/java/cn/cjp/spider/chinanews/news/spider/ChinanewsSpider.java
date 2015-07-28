package cn.cjp.spider.chinanews.news.spider;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cjp.common.spider.BaseSpider;

/**
 * 中国新闻网爬虫
<<<<<<< HEAD
 * 
=======
>>>>>>> branch 'master' of https://github.com/JPCui/cjp-spider.git
 * @author REAL
 *
 */
public class ChinanewsSpider extends BaseSpider{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ChinanewsSpider.class);

	@Autowired
	private Thread chinanewsThread;

	ExecutorService threadPool;

	public void crawling(int threadNum) {
		this.threadNum = threadNum;
		this.crawling();
	}

	public void crawling() {
		threadPool = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			String threadName = this.getClass().getSimpleName() + "-" + i;
			Thread thread = this.getOneThread(threadName);
			threadMap.put(threadName, thread);
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					logger.error("UncaughtExceptionHandler：" + t.getName(), e);
				}
			});
			threadPool.execute(chinanewsThread);
		}
	}

	protected Thread getOneThread(String name) {
		Thread thread = new Thread(chinanewsThread);
		thread.setName(name);
		// 设置未能捕获的异常
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("UncaughtExceptionHandler：" + t.getName(), e);
			}
		});
		return thread;
	}

	public Thread getThread() {
		return chinanewsThread;
	}

	public void setThread(Thread thread) {
		this.chinanewsThread = thread;
	}

}
