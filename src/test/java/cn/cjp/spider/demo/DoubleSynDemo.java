package cn.cjp.spider.demo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DoubleSynDemo {
	private static final Logger logger = Logger.getLogger(DoubleSynDemo.class);

	public static void main(String[] args) {

		List<String> list = new ArrayList<String>();
		synchronized (list) {
			logger.info(1);
			synchronized (list) {
				logger.info(2);
			}
		}

	}

}
