package cn.cjp.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class URLTest {

	@Test
	public void testGetUrl() throws MalformedURLException{
		URL url = new URL("https://www.baidu.com/index/s?key=hello&sort=hello#myid");
		
		System.out.println(URLUtils.getQueryURL(url.toString()));
	}
	
}
