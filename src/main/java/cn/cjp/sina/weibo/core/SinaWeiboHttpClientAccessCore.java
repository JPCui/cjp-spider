package cn.cjp.sina.weibo.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.analyzer.ResponseJsonAnalyzer;
import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.domain.LoginDomain;
import cn.cjp.sina.weibo.domain.StatusPubWeibo;
import cn.cjp.sina.weibo.domain.UserDomain;

/**
 * 1. 纳入多线程的新浪微博爬虫<br>
 * 2. 将要加入代理服务<br>
 * 
 * @author REAL
 * 
 */
public class SinaWeiboHttpClientAccessCore extends HttpClientCore{
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
	public static SinaWeiboHttpClientAccessCore getInstance(String username,
			String password){
		SinaWeiboHttpClientAccessCore sinaWeiboAccessCore = new SinaWeiboHttpClientAccessCore();
		boolean isLogined = sinaWeiboAccessCore.login(username, password);

		if (!isLogined) {
			return null;
		}
		return sinaWeiboAccessCore;
	}

	/**
	 * 登录
	 * 
	 * @param data
	 *            参数(username, password)
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean login(String username, String password){
		// set request params
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("username", username);
		datas.put("password", password);
		datas.put("savestate", "1");
		datas.put("ec", "0");
		datas.put("entry", "mweibo");
		
		String json = null;
		
		synchronized (httpClient) {
			HttpResponse httpResponse = this.executePost(Const.LOGIN_URL, Const.HEADER_FOR_LOGIN, datas);
			json = this.getText(httpResponse);
		}

		LoginDomain loginDomain = LoginDomain.fromJson(json);
		if (null != loginDomain
				&& loginDomain.retcode == LoginDomain.LOGIN_SUCCESS) {
			logger.info("登录成功 -- " + loginDomain);
			return true;
		} else {
			logger.info("登录失败 -- " + json);
		}

		return false;
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
	public synchronized List<UserDomain> requestFansByUid(String uid, int page) {

		String url = Const.FANS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		HttpResponse response = this.executeGet(url, null);
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
	public synchronized List<UserDomain> requestFollowsByUid(String uid, int page)
			throws ClientProtocolException, IOException {

		String url = Const.FOLLOWERS_URL;
		url = url.replace("{uid}", uid);
		url = url.replace("{page}", page + "");

		HttpResponse response = this.executeGet(url, null);
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
	public synchronized StatusPubWeibo pubWeibo(Map<String, String> datas) {
		String url = Const.B_PUB_WEIBO;
		// set http-header
		HttpResponse httpResponse = this.executePost(url, null, datas);
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
