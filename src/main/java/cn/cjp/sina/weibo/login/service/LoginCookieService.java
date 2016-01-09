package cn.cjp.sina.weibo.login.service;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class LoginCookieService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(LoginCookieService.class);

	private static List<String> loginedUserList = new ArrayList<String>();

	private static Map<String, String> loginCookieMap = new HashMap<String, String>();

	/**
	 * 保存登录cookie
	 * 
	 * @param username 登录账号
	 * @param cookieStore 登录后获取到的登录Cookie
	 * @throws NullPointerException
	 *             if username is blank or cookieStore is null
	 */
	public static void saveLoginCookies(String username,
			CookieStore cookieStore) throws NullPointerException {

		if (StringUtils.isBlank(username)) {
			throw new NullPointerException("username is blank");
		}
		if (null == cookieStore) {
			throw new NullPointerException("CookieStore is null");
		}
		
		synchronized (loginedUserList) {
			if(!loginedUserList.contains(username)){
				// 验证是否需要设置cookie，一般在登录操作的时候执行
				List<Cookie> cookieList = cookieStore.getCookies();
				String loginCookie = "";
				for (Cookie cookie : cookieList) {
					loginCookie += cookie.getName() + "=" + cookie.getValue() + ";";
				}
				loginedUserList.add(username);
				loginCookieMap.put(username, loginCookie);
			}
		}
	}

	/**
	 * 随机获取一条cookie
	 * 
	 * @return
	 */
	public static String getRandomLoginCookie() {
		String randomUid = "";
		synchronized (loginedUserList) {
			// 这里用不用让线程等待呢
			if (loginedUserList.size() == 0) {
				logger.warn("当前无爬虫登录");
				try {
					loginedUserList.wait();
				} catch (InterruptedException e) {
				}
			}
			int random = RandomUtils.nextInt(loginedUserList.size());
			randomUid = loginedUserList.get(random);
		}

		return loginCookieMap.get(randomUid);
	}

	/**
	 * 随机获取一条cookie，key=Cookie
	 * 
	 * @return
	 */
	public static Map<String, String> getRandomLoginCookieMap() {
		final String loginCookie = getRandomLoginCookie();

		return new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				put("Cookie", loginCookie);
			}
		};
	}

}
