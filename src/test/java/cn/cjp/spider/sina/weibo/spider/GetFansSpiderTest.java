package cn.cjp.spider.sina.weibo.spider;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.cjp.spider.sina.weibo.spider.GetFansSpider;
import cn.cjp.spider.utils.SynchUtils;

public class GetFansSpiderTest {
	private static final Logger logger = Logger
			.getLogger(GetFansSpiderTest.class);

	public static void main(String[] args) {
		Map<String, String> accounts = new HashMap<String, String>();
		accounts.put("1367471019@qq.com", "15838228248");
		accounts.put("15838228248", "CJP15838228248");

		GetFansSpider spider = new GetFansSpider(accounts);
		spider.setSavedFileDir("D:/_weibo_data_/");

		spider.initWaitingUidList("1092617025");

		final List<Thread> threadList = new ArrayList<Thread>();

		for (int i = 0; i < 3; i++) {
			Thread thread = new Thread(spider);
			threadList.add(thread);
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					logger.error("UncaughtExceptionHandler："+t.getName(), e);
				}
			});
			thread.start();
		}
		
		// 监测资源利用情况
		SynchUtils synchUtils = new SynchUtils();
		synchUtils.start();

		// 输出线程状态
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < threadList.size(); i++) {
						Thread thread = threadList.get(i);
						logger.error(thread.getName()
								+ " : " + thread.getState().name());
					}
				}
			}
		}).start();
	}

}
