package cn.cjp.spider.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 用来监控同步资源，需要手动调用
 * 
 * @author REAL
 * 
 */
public class SynchUtils extends Thread {
	private static final Logger logger = Logger.getLogger(SynchUtils.class);

	private static Map<Long, String> synMarkMap = new HashMap<Long, String>();

	public static void put(Long nowTime, String value, Class<?> clazz) {
		Thread currThread = Thread.currentThread();
		logger.debug("进站--" + currThread.getName() + "-"
				+ clazz.getSimpleName() + " : " + value);
		synMarkMap.put(nowTime,
				currThread.getName() + "-" + clazz.getSimpleName() + " : "
						+ value);
	}

	public static void remove(Long key) {
		if(synMarkMap.containsKey(key)){
			logger.debug("出站--"+synMarkMap.get(key));
			synMarkMap.remove(key);
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
			}
			synchronized (logger) {
				for (Long key : synMarkMap.keySet()) {
					// 输出资源利用情况

					logger.error(DateTimeUtils.toLoggerTime(key) + " : "
							+ synMarkMap.get(key));
				}
			}
		}

	}

}
