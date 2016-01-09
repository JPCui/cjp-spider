package cn.cjp.sina.weibo.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import cn.cjp.utils.JacksonUtil;

public class LoginDomain {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LoginDomain.class);

	public static int LOGIN_SUCCESS = 20000000;
	public static int LOGIN_FAIL = 50011015;

	/**
	 * 20000000：成功<br>
	 * 50011015：用户名或密码错误<br>
	 */
	public int retcode;
	/**
	 * 显示错误信息
	 */
	public String msg;
	/**
	 * 一些参数，如果登录成功，会返回uid
	 */
	public Map<String, Object> data;

	@SuppressWarnings("serial")
	public static LoginDomain fromJson(String json) {
		LoginDomain loginDomain = new LoginDomain();
		JSONObject jo;
		try {
			jo = new JSONObject(json);

			loginDomain.retcode = jo.getInt("retcode");
			if (loginDomain.retcode != LoginDomain.LOGIN_SUCCESS) {
				return null;
			}
			loginDomain.msg = jo.getString("msg");
			JSONObject dataObj = jo.getJSONObject("data");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("uid", dataObj.get("uid"));
			data.put("loginresulturl", dataObj.get("loginresulturl"));

			final JSONObject crossObj = dataObj
					.getJSONObject("crossdomainlist");
			data.put("crossdomainlist", new HashMap<String, String>() {
				{
					put("sina.com.cn", crossObj.getString("sina.com.cn"));
					put("weibo.com", crossObj.getString("weibo.com"));
				}
			});
			loginDomain.data = data;
		} catch (JSONException e) {
			if (logger.isInfoEnabled()) {
				logger.info("Json转换失败 - JSONException e=" + e);
			}
			loginDomain = null;
		}

		return loginDomain;
	}

	public String toString(){
		return JacksonUtil.toJson(this);
	}
	
}
