package cn.cjp.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils{

	/**
	 * 获取带query参数的URL，取出reference（即 #及#后面的内容）<br>
	 * eg:<br>
	 * https://www.baidu.com/index/s?key=hello&sort=hello#my_id<br>
	 * return : <br>
	 * https://www.baidu.com/index/s?key=hello&sort=hello
	 * @param urlStr
	 * @return
	 */
	public static String getQueryURL(String urlStr){
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url.getProtocol() + "://" + url.getHost() + url.getPath() + "?" + url.getQuery();
	}
	
}
