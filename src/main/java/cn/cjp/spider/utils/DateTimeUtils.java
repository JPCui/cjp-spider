package cn.cjp.spider.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

	public static String toLoggerTime(Long time) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		int y = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);

		return y + "-" + month + "-" + d + " " + h + ":" + min + ":" + s + ","
				+ ms;

	}

}
