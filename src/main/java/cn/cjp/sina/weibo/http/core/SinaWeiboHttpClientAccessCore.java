package cn.cjp.sina.weibo.http.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.analyzer.ResponseJsonAnalyzer;
import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.domain.LoginDomain;
import cn.cjp.sina.weibo.domain.StatusPubWeibo;
import cn.cjp.sina.weibo.domain.UserDomain;
import cn.cjp.sina.weibo.login.service.LoginCookieService;
import cn.cjp.utils.SynchUtils;

/**
 * 1. 纳入多线程的新浪微博爬虫<br>
 * 2. 将要加入代理服务<br>
 * 
 * @author REAL
 * 
 */
public class SinaWeiboHttpClientAccessCore extends HttpClientCore {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SinaWeiboHttpClientAccessCore.class);

	private SinaWeiboHttpClientAccessCore() {
	}

	/**
	 * 获取SinaWeiboHttpClientAccessCore实例
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SinaWeiboHttpClientAccessCore getInstance(
			Map<String, String> accounts) {
		SinaWeiboHttpClientAccessCore sinaWeiboAccessCore = new SinaWeiboHttpClientAccessCore();
		boolean loginFlag = false;
		for (String username : accounts.keySet()) {
			loginFlag = sinaWeiboAccessCore.login(username,
					accounts.get(username));
			if (!loginFlag) {
				logger.error("账号（" + username + "）登录失败");
			}
		}
		return sinaWeiboAccessCore;
	}

	/**
	 * 登录
	 * 
	 * @param data
	 *            参数(username, password)
	 * 
	 * @return true if login success，or false
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean login(String username, String password) {
		// set request params
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("username", username);
		datas.put("password", password);
		datas.put("savestate", "1");
		datas.put("ec", "0");
		datas.put("entry", "mweibo");

		String json = null;
		HttpResponse httpResponse = null;

		HttpPost httpPost = new HttpPost(Const.LOGIN_URL);
		// 设置Header
		if (Const.HEADER_FOR_LOGIN != null
				&& Const.HEADER_FOR_LOGIN.size() != 0) {
			for (String key : Const.HEADER_FOR_LOGIN.keySet()) {
				httpPost.setHeader(key, Const.HEADER_FOR_LOGIN.get(key));
			}
		}
		// 设置请求参数
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
		synchronized (httpClient) {
			long now = System.currentTimeMillis();
			SynchUtils.put(now, "登录HttpClient占用", this.getClass());
			// 执行请求
			httpResponse = this.execute(httpPost);
			// 保存cookie
			LoginCookieService.saveLoginCookies(username,
					this.httpClient.getCookieStore());
			SynchUtils.remove(now);
		}

		json = this.getText(httpResponse);

		LoginDomain loginDomain = LoginDomain.fromJson(json);
		if (null != loginDomain
				&& loginDomain.retcode == LoginDomain.LOGIN_SUCCESS) {
			logger.error("登录成功 -- " + loginDomain);
			return true;
		} else {
			logger.error("登录失败 -- " + json);
		}

		return false;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param cookies
	 * @param uid
	 * @param page
	 * @return get FansList, size=0 if response is null while SocketException
	 *         happening
	 */
	public List<UserDomain> requestFansByUid(String uid, int page) {

		String url = Const.FANS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		HttpResponse response = this.executeGet(url,
				LoginCookieService.getRandomLoginCookieMap());
		if (response == null) {
			return new ArrayList<UserDomain>();
		}
		HttpEntity entity = response.getEntity();
		String json = null;
		try {
			json = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			logger.error("请求数据转换失败", e);
			return new ArrayList<UserDomain>();
		} catch (IOException e) {
			logger.error("请求数据转换失败", e);
			return new ArrayList<UserDomain>();
		}

		List<UserDomain> fans = ResponseJsonAnalyzer.analyzerGetFans(json);

		return fans;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param cookies
	 * @param uid
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public List<UserDomain> requestFollowsByUid(String uid,
			int page) throws ClientProtocolException, IOException {

		String url = Const.FOLLOWERS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		HttpResponse response = this.executeGet(url,
				LoginCookieService.getRandomLoginCookieMap());
		HttpEntity entity = response.getEntity();
		String json = EntityUtils.toString(entity, "UTF-8");

		List<UserDomain> follows = ResponseJsonAnalyzer.analyzerGetFans(json);

		return follows;
	}

	/**
	 * 
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
	 */
	public StatusPubWeibo pubWeibo(Map<String, String> datas) {
		String url = Const.B_PUB_WEIBO;
		// set http-header
		HttpResponse httpResponse = this.executePost(url,
				LoginCookieService.getRandomLoginCookieMap(), datas);
		HttpEntity entity = httpResponse.getEntity();
		String json = "";
		try {
			json = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		StatusPubWeibo status = ResponseJsonAnalyzer.analyzePubWeibo(json);
		return status;
	}
}
