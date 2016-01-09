package cn.cjp.common.spider;

import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫基类
 * @author REAL
 *
 */
public abstract class BaseSpider {

	/**
	 * 开启线程数
	 */
	protected int threadNum = 3;
	
	/**
	 * 用于线程管理
	 */
	protected static Map<String, Thread> threadMap = new HashMap<String, Thread>();

	/**
	 * 开始爬取
	 */
	abstract public void crawling();

	/**
	 * 开始爬取
	 * @param threadNum 线程数
	 */
	abstract public void crawling(int threadNum);

	/**
	 * 修改线程优先级
	 * @param threadName 线程名
	 * @param newPriority 优先级
	 */
	public void set(String threadName, int newPriority){
		if(threadMap.containsKey(threadName)){
			threadMap.get(threadName).setPriority(newPriority);
		}
	}

	/**
	 * 获取一个抓取线程线程
	 * @param name 设置线程名
	 * @return
	 */
	protected abstract Thread getOneThread(String name);
	
	/**
	 * 设置线程数
	 * @param threadNum
	 */
	public void setThreadNum(int threadNum){
		this.threadNum = threadNum;
	}
	
	/**
	 * 获取线程数
	 * @return
	 */
	public int getThreadNum() {
		return threadNum;
	}

	/**
	 * 用于线程管理
	 * @return the threadMap
	 */
	public static Map<String, Thread> getThreadMap() {
		return threadMap;
	}

	/**
	 * 用于线程管理
	 * @param threadMap the threadMap to set
	 */
	public static void setThreadMap(Map<String, Thread> threadMap) {
		BaseSpider.threadMap = threadMap;
	}

}
