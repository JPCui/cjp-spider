package cn.cjp.demo;

import org.apache.log4j.Logger;

/**
 * 测试Thread与Runnable区别
 * @author REAL
 *
 */
public class ThreadStartTest{

	
	public static void main(String[] args) {
		RunnableDemo runnableDemo = new RunnableDemo();
		for(int i=0; i<3; i++){
			new Thread(runnableDemo).start();
		}
		for(int i=0; i<3; i++){
			new Thread(new RunnableDemo()).start();
		}
		for(int i=0; i<3; i++){
			new ThreadDemo().start();
		}
	}
	
	
}

class ThreadDemo extends Thread{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ThreadDemo.class);

	public void run(){
		logger.info(this.getClass());
	}
}

class RunnableDemo implements Runnable{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RunnableDemo.class);

	public void run() {
		logger.info(this.getClass());
	}
	
}