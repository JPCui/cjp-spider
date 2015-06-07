package cn.cjp.spider.chinanews.news.spider;

import org.apache.log4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

import org.springframework.beans.factory.annotation.Autowired;

import cn.cjp.spider.common.spider.BaseSpider;

/**
 * 中国新闻网爬虫
 * @author REAL
 *
 */
public class ChinanewsSpider extends BaseSpider{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ChinanewsSpider.class);

	@Autowired ChinanewsThread chinanewsThread;
	
	public void crawling(int threadNum){
		this.threadNum = threadNum;
		this.crawling();
	}
	
	public void crawling(){
		for (int i = 0; i < threadNum; i++) {
			String threadName = this.getClass().getSimpleName()+"-"+i;
			Thread thread = this.getOneThread(threadName);
			threadMap.put(threadName, thread);
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					logger.error("UncaughtExceptionHandler："+t.getName(), e);
				}
			});
			thread.start();
		}
	}

	protected Thread getOneThread(String name){

		Thread thread = new Thread(chinanewsThread);
		thread.setName(name);

		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("UncaughtExceptionHandler："+t.getName(), e);
			}
		});
		return thread;
	}
	
}
