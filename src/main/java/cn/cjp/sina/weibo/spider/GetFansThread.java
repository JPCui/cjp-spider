package cn.cjp.sina.weibo.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.cjp.utils.SynchUtils;

public class GetFansThread extends Thread{
	private static final Logger logger = Logger
			.getLogger(GetFansThread.class);

	private static Map<Integer, Thread> threadMap = new HashMap<Integer, Thread>();

	GetFansSpider spider = null;
	private int threadNum = 3;

	/**
	 * 
	 * @param accounts 微博登录账号
	 * @param saveDir 物理保存路径
	 * @param waitingUidList 等待抓取的用户ID队列
	 */
	public GetFansThread(Map<String, String> accounts, String saveDir, List<String> waitingUidList){
		spider = new GetFansSpider(accounts);
		spider.setSavedFileDir(saveDir);
		spider.initWaitingUidList(waitingUidList);
	}
	
	/**
	 * 
	 * @param accounts 微博登录账号
	 * @param saveDir 物理保存路径
	 * @param waitingUidList 等待抓取的用户ID
	 */
	public GetFansThread(Map<String, String> accounts, String saveDir, String waitingUid){
		spider = new GetFansSpider(accounts);
		spider.setSavedFileDir(saveDir);
		spider.initWaitingUidList(waitingUid);
	}
	
	/**
	 * 线程数，默认为3
	 * @param threadNum 线程数
	 */
	public void setThreadNum(int threadNum){
		this.threadNum = threadNum;
	}
	
	private Thread getOneThread(String name){

		Thread thread = new Thread(spider);
		thread.setName(name);
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("UncaughtExceptionHandler："+t.getName(), e);
			}
		});
		return thread;
	}
	
	public void run(){
		for (int i = 0; i < threadNum; i++) {
			Thread thread = this.getOneThread("get-fans-thread-"+i);
			threadMap.put(i, thread);
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					logger.error("UncaughtExceptionHandler："+t.getName(), e);
				}
			});
		}
		
		// start
		for(Integer key : threadMap.keySet()){
			Thread thread = threadMap.get(key);
			thread.start();
		}
		
		// 监测资源利用情况
		SynchUtils synchUtils = new SynchUtils();
		synchUtils.start();

		// 输出线程状态, restart if blocked...
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(Integer key : threadMap.keySet()){
						Thread thread = threadMap.get(key);
						logger.error(thread.getName()
								+ " : " + thread.getState().name());
					}
				}
			}
		}).start();
	}

	public static Map<Integer, Thread> getThreadMap() {
		return threadMap;
	}
	public static void setThreadMap(Map<Integer, Thread> threadMap) {
		GetFansThread.threadMap = threadMap;
	}
	public GetFansSpider getSpider() {
		return spider;
	}
	public void setSpider(GetFansSpider spider) {
		this.spider = spider;
	}

	public static Logger getLogger() {
		return logger;
	}

	public int getThreadNum() {
		return threadNum;
	}
}
