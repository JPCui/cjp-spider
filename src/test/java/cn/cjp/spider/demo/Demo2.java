package cn.cjp.spider.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.domain.LoginDomain;

public class Demo2 {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Const.LOGIN_URL);
		for(String key : Const.header.keySet()){
			httpPost.setHeader(key, Const.header.get(key));
		}
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("username","1367471019@qq.com"));
		pairs.add(new BasicNameValuePair("password","15838228248"));
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs);
		httpPost.setEntity(formEntity);
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		CookieStore cookieStore = httpClient.getCookieStore();
		List<Cookie> cookieList = cookieStore.getCookies();
		String cookieStr = "";
		for(Cookie cookie : cookieList){
			cookieStr += cookie.getName()+"="+cookie.getValue()+";";
		}
		cookieStr = cookieStr.substring(0, cookieStr.length());
		System.out.println(cookieStr);
		
		String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		
		System.out.println(LoginDomain.fromJson(json));
		
		//////////////////////////////////////////////////////////////
		
		httpPost = new HttpPost(Const.B_PUB_WEIBO);
//		httpPost.setURI(URI.create(Const.B_PUB_WEIBO));
		for(String key : Const.header.keySet()){
			httpPost.setHeader(key, Const.header.get(key));
		}
		httpPost.setHeader("Host", "m.weibo.cn");
		httpPost.addHeader("Cookie", cookieStr);
		pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("content","1367471019@qq.com"));
		formEntity = new UrlEncodedFormEntity(pairs);
		httpPost.setEntity(formEntity);
		
		httpResponse = httpClient.execute(httpPost);
		json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		
		System.out.println(LoginDomain.fromJson(json));
		
		
		
		
	}
	
	
}
