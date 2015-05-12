package cn.cjp.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class Tools {

	/*
	 * 获取当前时间
	 * @param Y_M_D 是否包含：年月日
	 * @param H_M_S 是否包含：时分秒
	 */
	public static String getCurrentTime(boolean Y_M_D, boolean H_M_S)
	{
		Calendar cal = Calendar.getInstance();
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH )+1;//月份是从0开始的
		int day = cal.get( Calendar.DAY_OF_MONTH );
		int hour = cal.get( Calendar.HOUR_OF_DAY );
		int min = cal.get( Calendar.MINUTE );
		int second = cal.get( Calendar.SECOND );
//		System.out.println(year +"-"+ month +"-"+ day +" "+ hour +":"+ min +":"+ second);
		
		String currentTime = "";
		if(Y_M_D){
			currentTime += year +"-"+ ( month<10?("0"+month):month ) +"-"+ ( day<10?("0"+day):day );
		}
		if(H_M_S){
			currentTime += " "+ ( hour<10?("0"+hour):hour ) +":"+ ( min<10?("0"+min):min ) +":"+ ( second<10?("0"+second):second );
		}
//		System.out.println( currentTime );
		return currentTime;
	}
	
	public static String handingNewsStr(String newsStr)
	{
		newsStr = handingStrCh(newsStr);
		newsStr = handingStrCode(newsStr);
		
		return newsStr;
	}
	public static String handingStrCode(String str)
	{
		try {
			str = new String( str.getBytes("UTF-8") );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static String handingStrCh(String str)
	{
		//掐头去尾（空格）
		while(str.charAt(0)==' ')
		{
			str = str.substring(1);
		}
		while(str.charAt( str.length()-1 )==' ')
		{
			str = str.substring(0, str.length()-1);
		}
		
		//处理中间空格（多个空格缩减为一个空格）
		for(int i=1; i<str.length()-1; i++)
		{
			if(str.charAt(i)==' ' && str.charAt(i+1)==' ')
			{
				str = str.substring(0, i) + str.substring(i+1, str.length());
				i--;
			}
		}
		
		return str;
	}
	
	public static void main(String []args)
	{
		System.out.println(handingNewsStr("濛濛濛") );
	}
}
