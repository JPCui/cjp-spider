package cn.cjp.sina.weibo.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.domain.Const;

public class HttpClientCore {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HttpClientCore.class);

	public static String loginCookie = null;

	public DefaultHttpClient httpClient = null;

	public HttpClientCore() {
		// 初始化连接池
		PoolingClientConnectionManager connManager = new PoolingClientConnectionManager();
		// 连接池最大连接数
		connManager.setMaxTotal(200);
		connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());

		httpClient = new DefaultHttpClient();
		// 重新请求次数
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
						3, true));
	}

	public synchronized HttpResponse executePost(String url, Map<String, String> headers,
			Map<String, String> datas) {
		HttpPost httpPost = new HttpPost(url);
		// set cookie
		if (!StringUtils.isBlank(loginCookie)) {
			httpPost.addHeader("Cookie", loginCookie);
		}
		// set header
		for(String key : Const.HEADER_DEFAULT.keySet()){
			httpPost.setHeader(key, Const.HEADER_DEFAULT.get(key));
		}
		// replace header
		if(headers != null && headers.size() != 0){
			for(String key : headers.keySet()){
				httpPost.setHeader(key, headers.get(key));
			}
		}
		// set request params
		if (datas != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (String key : datas.keySet()) {
				pairs.add(new BasicNameValuePair(key, datas.get(key)));
			}
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						pairs);
				httpPost.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			// 验证是否需要设置cookie，一般在第一步登录操作的时候执行
			if(StringUtils.isBlank(loginCookie)){
				CookieStore cookieStore = httpClient.getCookieStore();
				List<Cookie> cookieList = cookieStore.getCookies();
				loginCookie = "";
				for(Cookie cookie :cookieList){
					loginCookie += cookie.getName()+"="+cookie.getValue()+";";
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("query :"+url);
		return httpResponse;
	}

	public synchronized HttpResponse executeGet(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);
		// set cookie
		if (!StringUtils.isBlank(loginCookie)) {
			httpGet.addHeader("Cookie", loginCookie);
		}
		// set header
		for(String key : Const.HEADER_DEFAULT.keySet()){
			httpGet.setHeader(key, Const.HEADER_DEFAULT.get(key));
		}
		// replace header
		if(headers != null && headers.size() != 0){
			for(String key : headers.keySet()){
				httpGet.setHeader(key, headers.get(key));
			}
		}

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("query :"+url);
		return httpResponse;
	}
	
	/**
	 * 获取文本信息
	 * @param httpResponse
	 * @return
	 */
	public String getText(HttpResponse httpResponse){
		HttpEntity httpEntity = httpResponse.getEntity();
		try {
			return EntityUtils.toString(httpEntity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
