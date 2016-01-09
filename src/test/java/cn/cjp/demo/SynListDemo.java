package cn.cjp.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * 同步List中某一个对象，是可行的
 * @author REAL
 *
 */
public class SynListDemo{

	private static final Logger logger = Logger.getLogger(SynListDemo.class);
	
	public int num=0;
	
	public static void main(String[] args) {
		final List<SynListDemo> strList = new ArrayList<SynListDemo>();
		
		strList.add(new SynListDemo());
		strList.add(new SynListDemo());
		strList.add(new SynListDemo());

		new Thread(new Runnable() {
			public void run() {
				synchronized (strList.get(1)) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					logger.info(strList.get(1));
					for(int i=0; i<strList.size(); i++){
						logger.info(i+" : "+strList.get(i).num);
					}
				}
			}
		}).start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		
		new Thread(new Runnable() {
			public void run() {
				for(int i=0; i<strList.size(); i++){
					synchronized (strList.get(i)) {
						logger.info(i+" : "+strList.get(i).num);
						strList.get(i).num = 3;
					}
				}
			}
		}).start();
		
		
	}
	
	
}