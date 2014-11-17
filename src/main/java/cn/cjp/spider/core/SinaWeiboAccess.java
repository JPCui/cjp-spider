package cn.cjp.spider.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.cjp.spider.domain.weibo.sina.Const;

/**
 * 
 * @function <br>
 * {@link SinaWeiboAccess#getAttitudesOfWeibo(Map, String, int)}<br>
 *           {@link SinaWeiboAccess#getHomeWeibo(Map, String, String)}<br>
 *           {@link SinaWeiboAccess#getPLsOfWeibo(Map, String, String, int)}<br>
 *           {@link SinaWeiboAccess#getWeibosOfUser(Map, String, int)}<br>
 *           {@link SinaWeiboAccess#getZFsOfWeibo(Map, String, String, int)}<br>
 * @author REAL
 * 
 */
public class SinaWeiboAccess {

	public SinaWeiboAccess() {
	}

	/**
	 * 获取带有请求头的{@code Connection}
	 * 
	 * @param url
	 * @return
	 */
	private static Connection getConnection(String url) {
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
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static Response request(Connection conn) throws IOException,
			InterruptedException {
		Response response = null;

		while (true) {
			try {
				response = conn.execute();
			} catch (SocketTimeoutException e) {
				Thread.sleep(2000);
				continue;
			}
			break;
		}

		return response;
	}

	/**
	 * 登录
	 * 
	 * @param data
	 *            参数
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Response login(Map<String, String> data) throws IOException,
			InterruptedException {
		Connection conn = getConnection(Const.LOGIN_URL);
		for (String key : data.keySet()) {
			conn.data(key, data.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response;
	}

	/**
	 * 获取主页微博
	 * 
	 * @param cookies
	 * @param next_cursor
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getHomeWeibo(Map<String, String> cookies,
			String next_cursor, String page) throws IOException,
			InterruptedException {
		String url = Const.HOME_WEIBO_URL;
		if (!StringUtils.isBlank(next_cursor)) {
			url = url.replace("{next_cursor}", next_cursor);
		}
		url = url.replace("{page}", page + "");
		Connection conn = getConnection(url);
		for (String key : cookies.keySet()) {
			conn.header(key, cookies.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取微博评论
	 * 
	 * @param cookies
	 * @param uid
	 * @param mid
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getPLsOfWeibo(Map<String, String> cookies, String uid,
			String mid, int page) throws IOException, InterruptedException {
		String url = Const.PLs_OF_WEIBO_URL;
		url = url.replace("{page}", page + "");
		url = url.replace("{uid}", uid);
		url = url.replace("{mid}", mid);
		Connection conn = getConnection(url);
		for (String key : cookies.keySet()) {
			conn.header(key, cookies.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取微博转发
	 * 
	 * @param cookies
	 * @param uid
	 * @param mid
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getZFsOfWeibo(Map<String, String> cookies, String uid,
			String mid, int page) throws IOException, InterruptedException {
		String url = Const.ZFs_OF_WEIBO_URL;
		url = url.replace("{page}", page + "");
		url = url.replace("{uid}", uid);
		url = url.replace("{mid}", mid);
		Connection conn = getConnection(url);
		for (String key : cookies.keySet()) {
			conn.header(key, cookies.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取指定用户的微博列表
	 * 
	 * @param cookies
	 * @param uid
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getWeibosOfUser(Map<String, String> cookies,
			String uid, int page) throws IOException, InterruptedException {
		String url = Const.WEIBOs_OF_USER_URL;
		url = url.replace("{page}", page + "");
		url = url.replace("{uid}", uid);
		Connection conn = getConnection(url);
		for (String key : cookies.keySet()) {
			conn.header(key, cookies.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取微博的赞列表
	 * 
	 * @param cookies
	 * @param mid
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getAttitudesOfWeibo(Map<String, String> cookies,
			String mid, int page) throws IOException, InterruptedException {
		String url = Const.ATTITUDEs_OF_WEIBO_URL;
		url = url.replace("{page}", page + "");
		url = url.replace("{mid}", mid);
		Connection conn = getConnection(url);
		for (String key : cookies.keySet()) {
			conn.header(key, cookies.get(key));
		}
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

}
