package cn.cjp.sina.weibo.http.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.domain.LoginDomain;

/**
 * @deprecated
 * 考虑纳入多线程的新浪微博爬虫核心代码
 * <br> use {@link SinaWeiboHttpClientAccessCore} instead
 * @author REAL
 *
 */
public class SinaWeiboAccessCore {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SinaWeiboAccessCore.class);

	private Connection conn = null;

	private SinaWeiboAccessCore() {
	}

	/**
	 * 获取SinaWeiboAccessCore实例
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static SinaWeiboAccessCore getInstance(String username, String password) {
		SinaWeiboAccessCore sinaWeiboAccessCore = new SinaWeiboAccessCore();
		boolean isLogined = sinaWeiboAccessCore.login(username, password);

		if(isLogined){
			return sinaWeiboAccessCore;
		}else{
			return null;
		}
	}

	/**
	 * 登录
	 * 
	 * @param data
	 *            参数(username, password)
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean login(String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		map.put("savestate", "1");
		map.put("ec", "0");
		map.put("entry", "mweibo");

		Response response = request(Const.LOGIN_URL, map, Method.POST);
		
		String json = response.body();
		
		LoginDomain loginDomain = LoginDomain.fromJson(json);
		if(null != loginDomain && loginDomain.retcode == LoginDomain.LOGIN_SUCCESS){
			logger.info("登录成功 -- " + loginDomain);
			return true;
		}else{
			logger.info("登录失败 -- " + json);
		}
		
		return false;
	}

	/**
	 * 获取带有请求头的{@code Connection}
	 * 
	 * @param url
	 * @return
	 */
	private Connection getConnection(String url) {
		Connection conn = Jsoup.connect(url);
		conn.ignoreContentType(true);
		for (String key : Const.header.keySet()) {
			conn.header(key, Const.header.get(key));
		}
		return conn;
	}

	/**
	 * 发送请求
	 * 
	 * @param conn
	 *            带有参数（cookies）的{@code Connection}
	 * @param post
	 * @param url
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private synchronized Response request(String url, Map<String, String> dataMap,
			Method method) {
		// 获取连接
		if (null == conn) {
			conn = this.getConnection(url);
		} else {
			conn = conn.url(url);
		}
		// 设置请求参数
		if (null != dataMap) {
			conn.data(dataMap);
		}
		// 设置请求类型
		conn.method(null == method ? Method.GET : method);

		Response response = null;

		int times = 3;
		while (times-- != 0) {
			try {
				response = conn.referrer("http://m.weibo.cn/")
						.ignoreHttpErrors(true).ignoreContentType(true)
						.followRedirects(true).timeout(5000).execute();
			} catch (SocketTimeoutException e) {
				logger.warn("Connect timed out");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
				}
				continue;
			} catch (SocketException e) {
				logger.error("连接拒绝", e);
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}

		return response;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param cookies
	 * @param uid
	 * @param page
	 * @return
	 */
	public synchronized String getFansByUid(String uid, int page) {

		String url = Const.FANS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		conn.url(url);
		conn.method(Method.POST);

		Response response = request(url, null, Method.GET);
		String json =response.body();
		
		return json;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param cookies
	 * @param uid
	 * @param page
	 * @return
	 */
	public String getFollowsByUid(String uid, int page) {

		String url = Const.FOLLOWERS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		conn.method(Method.POST);

		Response response = request(url, null, Method.GET);
		return response.body();
	}
}
