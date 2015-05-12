package cn.cjp.spider.sina.weibo.spider;

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

	public GetFansThread(Map<String, String> accounts, String saveDir, List<String> waitingUidList){
		spider = new GetFansSpider(accounts);
		spider.setSavedFileDir(saveDir);
		spider.initWaitingUidList(waitingUidList);
	}
	
	public GetFansThread(Map<String, String> accounts, String saveDir, String waitingUid){
		spider = new GetFansSpider(accounts);
		spider.setSavedFileDir(saveDir);
		spider.initWaitingUidList(waitingUid);
	}
	
	public Thread getOneThread(String name){

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
		for (int i = 0; i < 3; i++) {
			Thread thread = this.getOneThread("thread-"+i);
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
}
