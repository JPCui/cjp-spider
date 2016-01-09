package cn.cjp.demo;

import java.net.URISyntaxException;
import java.util.Calendar;

import org.apache.log4j.Logger;


public class Test{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) throws URISyntaxException {
		Calendar cal = Calendar.getInstance();
		cal.get(Calendar.MILLISECOND);

		logger.info(cal.getTime().getTime()+"--"+cal.get(Calendar.MILLISECOND));
		
	}
	
}