package cn.cjp.utils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
		synMarkMap.put(nowTime, clazz + " : " + value);
	}

	public static void remove(Long key) {
		synMarkMap.remove(key);
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
			}
			for (Long key : synMarkMap.keySet()) {
				// 输出资源利用情况

				logger.error(DateTimeUtils.toLoggerTime(key) + " : "
						+ synMarkMap.get(key));
			}
		}

	}

}
