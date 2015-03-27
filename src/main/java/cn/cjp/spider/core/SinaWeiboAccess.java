package cn.cjp.spider.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import cn.cjp.spider.domain.weibo.sina.Const;
import cn.cjp.spider.domain.weibo.sina.StatusPubWeibo;

/**
 * 
 * @function <br>
 *           {@link SinaWeiboAccess#getAttitudesOfWeibo(Map, String, int)}<br>
 *           {@link SinaWeiboAccess#getHomeMsg(Map, int)}<br>
 *           {@link SinaWeiboAccess#getHomeWeibo(Map, String, String)}<br>
 *           {@link SinaWeiboAccess#getPLsOfWeibo(Map, String, String, int)}<br>
 *           {@link SinaWeiboAccess#getWeibosOfUser(Map, String, int)}<br>
 *           {@link SinaWeiboAccess#getZFsOfWeibo(Map, String, String, int)}<br>
 *           {@link SinaWeiboAccess#login(Map)}<br>
 *           {@link SinaWeiboAccess#publishWeibo(Map, Map)}<br>
 *           {@link SinaWeiboAccess#pubWeibo(Map, Map)}<br>
 *           {@link SinaWeiboAccess#pubMWeibo(Map, String, Map)}<br>
 *           {@link SinaWeiboAccess#}<br>
 * @author REAL
 * 
 */
public class SinaWeiboAccess {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SinaWeiboAccess.class);

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

		int times = 3;
		while (times-- != 0) {
			try {
				response = conn.referrer("http://m.weibo.cn/")
						.ignoreHttpErrors(true).ignoreContentType(true)
						.followRedirects(true).execute();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				Thread.sleep(2000);
				continue;
			}
			break;
		}
		
		logger.info(response.body());
		return response;
	}

	/**
	 * 获取位置列表
	 * 
	 * @param cookies
	 * @param lon
	 * @param lat
	 * @see Const#GET_NEAR_BY_POIS
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getPosList(Map<String, String> cookies, String q,
			double lon, double lat) throws IOException, InterruptedException {
		String url = Const.GET_NEAR_BY_POIS;
		url = url.replace("{lat}", lat + "");
		url = url.replace("{lon}", lon + "");
		if (null != q) {
			url += "&q=" + q;
		}
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取'@'列表
	 * 
	 * @param cookies
	 * @see Const#AT_LIST_URL
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getAtList(Map<String, String> cookies)
			throws IOException, InterruptedException {
		String url = Const.AT_LIST_URL;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 根据关键词获取'@'列表
	 * 
	 * @param cookies
	 * @param keyword
	 *            关键词
	 * @param page
	 *            页数
	 * @see Const#AT_LIST_BY_KEY_URL
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getAtListByKey(Map<String, String> cookies,
			String keyword, int page) throws IOException, InterruptedException {
		String url = Const.AT_LIST_BY_KEY_URL;
		url = url.replace("{keyword}", keyword);
		url = url.replace("{page}", page + "");
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取话题列表
	 * 
	 * @param cookies
	 * @see Const#TOPIC_LIST_URL
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getTopicList(Map<String, String> cookies)
			throws IOException, InterruptedException {
		String url = Const.TOPIC_LIST_URL;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}
	
	public static String getLoginedUser(Map<String, String> cookies) throws IOException, InterruptedException{
		String url = Const.GET_ME_URL;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		
		return response.body();
	}

	/**
	 * 根据关键词获取话题列表
	 * 
	 * @param cookies
	 * @param keyword
	 *            关键词
	 * @see Const#TOPIC_LIST_BY_KEY_URL
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getTopicListByKey(Map<String, String> cookies,
			String keyword) throws IOException, InterruptedException {
		String url = Const.TOPIC_LIST_BY_KEY_URL;
		url = url.replace("{keyword}", keyword);
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
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
			String next_cursor, int page) throws IOException,
			InterruptedException {
		String url = Const.HOME_WEIBO_URL;
		if (!StringUtils.isBlank(next_cursor)) {
			url += "&next_cursor=" + next_cursor;
		}
		url += "&page=" + page;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 获取个人信息<br>
	 * 获取第一页时，会有总页数
	 * 
	 * @param cookies
	 * @param page
	 *            >=1
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getHomeMsg(Map<String, String> cookies, int page)
			throws IOException, InterruptedException {
		String url = Const.HOME_MSG_URL;
		url += "&page=" + page;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
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
	 * 
	 * @param cookies
	 * @param datas
	 *            <pre>
	 * **** 发布内容 ****
	 * content  我是一条微博
	 * 
	 * **** 发布图片（逗号分隔每一个picId） ****
	 * picId  picId1,picId2
	 * 
	 * **** 发布位置信息 ****
	 * poiid	B2094656D465AAFB4099 // 位置信息只需要poiid
	 * </pre>
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String pubWeibo(Map<String, String> cookies,
			Map<String, String> datas) throws IOException, InterruptedException {
		String url = Const.B_PUB_WEIBO;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.data(datas);
		conn.method(Method.POST);

		Response response = request(conn);
		return response.body();
	}

	/**
	 * 
	 * @param cookies
	 * @param datas
	 *            <pre>
	 * **** 发布内容 ****
	 * content  我是一条微博
	 * 
	 * **** 发布图片（逗号分隔每一个picId） ****
	 * picId  picId1,picId2
	 * 
	 * **** 发布位置信息 ****
	 * poiid	B2094656D465AAFB4099 // 位置信息只需要poiid
	 * </pre>
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	public static StatusPubWeibo publishWeibo(Map<String, String> cookies,
			Map<String, String> datas) throws IOException,
			InterruptedException, JSONException {
		String url = Const.B_PUB_WEIBO;
		Connection conn = getConnection(url);
		conn.cookies(cookies);
		conn.data(datas);
		conn.method(Method.POST);

		Response response = request(conn);

		StatusPubWeibo statusPubWeibo = StatusPubWeibo
				.fromJson(response.body());

		return statusPubWeibo;
	}

	/**
	 * 发送移动App微博，在微博上显示自己设置的终端类型
	 * 
	 * @param cookies
	 * @param datas
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String pubMWeibo(Map<String, String> cookies, String term,
			Map<String, String> datas) throws IOException, InterruptedException {
		String url = Const.M_PUB_WEIBO + "gsid=" + cookies.get("gsid_CTandWM");
		Connection conn = getConnection(url);
		conn.data(datas);
		conn.method(Method.POST);

		// Response response = request(conn);
		// return response.body();

		throw new NotImplementedException();
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

	/**
	 * 
	 * @param f
	 *            图片文件
	 * @return 返回 picId
	 * @throws Exception
	 */
	public static String addPic(Map<String, String> cookies, File f) {
		try {
			String bound = "---------------------------20226268138071";
			List<byte[]> bts = new ArrayList<byte[]>();

			String ts = "--"
					+ bound
					+ "\r\n"
					+ "Content-Disposition: form-data; name=\"type\""
					+ "\r\n\r\n"
					+ "json"
					+ "\r\n"
					+ "--"
					+ bound
					+ "\r\n"
					+ "Content-Disposition: form-data; name=\"pic\"; filename=\"2.png\""
					+ "\r\n" + "Content-Type: image/png" + "\r\n\r\n";
			bts.add(ts.getBytes());
			ts = "";

			// 写入图片流
			int size = (int) f.length();
			byte[] data = new byte[size];
			try {
				FileInputStream fis = new FileInputStream(f);
				fis.read(data, 0, size);
				fis.close();
				bts.add(data);
			} catch (Exception e) {
				return e.getMessage();
			}

			ts += "\r\n--" + bound + "--\r\n";
			bts.add(ts.getBytes());
			ts = "";

			String str = "";
			try {
				str = upload(cookies, bound, bts);
			} catch (Exception e) {
				return e.getMessage();
			}

			JSONObject jsonObj = null;
			try {
				jsonObj = new JSONObject(str);
				ts = jsonObj.getString("pic_id");
			} catch (JSONException e) {
				return e.getMessage();
			}
			return ts;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 上传方法 返回上传完毕的文件名
	 * 
	 * @return 如果正常，返回：{"ok":1,"msg":null,"pic_url":
	 *         "http:\/\/ww4.sinaimg.cn\/thumbnail\/da66c124jw1el0lhc07x7j200w0owmxg.jpg","pic_id":"da66c124jw1el0lhc07x7j200w0owm
	 *         x g " } <br>
	 *         如果通信异常，返回：{\"ok\":0,\"msg\":"000"}
	 */
	private static String upload(Map<String, String> cookieMap,
			String boundary, List<byte[]> data) {
		try {
			// 服务器IP(这里是从属性文件中读取出来的)
			URL url = new URL(Const.ADD_PIC_URL);

			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(30000);
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
			uc.setRequestMethod("POST");
			// 上传图片的一些参数设置
			uc.setRequestProperty("Accept",
					"application/json, text/javascript, */*; q=0.01");
			uc.setRequestProperty("Connection", "keep-alive");
			uc.setRequestProperty("Content-type",
					"multipart/form-data; boundary=" + boundary);

			String cookieStr = "";
			for (String key : cookieMap.keySet()) {
				cookieStr += key + "=" + cookieMap.get(key) + ";";
			}
			uc.setRequestProperty("Cookie", cookieStr);

			uc.setRequestProperty("Referer", "http://m.weibo.cn/mblog");
			uc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
			uc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			uc.setDoOutput(true);
			uc.setUseCaches(true);

			OutputStream out = uc.getOutputStream();
			for (byte[] bs : data) {
				out.write(bs);
			}
			out.flush();
			out.close();

			// 读取响应数据
			int code = uc.getResponseCode();

			String sCurrentLine = "";
			// 存放响应结果
			String sTotalString = "";
			if (code == 200) {
				java.io.InputStream is = uc.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((sCurrentLine = reader.readLine()) != null)
					if (sCurrentLine.length() > 0)
						sTotalString += sCurrentLine.trim();
			} else {
				sTotalString = "{\"ok\":0,\"msg\":" + code + "}";
			}
			return sTotalString;
		} catch (Exception e) {
			return e.getMessage();
		}

	}
}
