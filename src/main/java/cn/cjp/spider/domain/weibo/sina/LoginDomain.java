package cn.cjp.spider.domain.weibo.sina;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginDomain {
	
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
	public String data;
	
	public static LoginDomain fromJson(String json) throws JSONException{
		LoginDomain loginDomain = new LoginDomain();
		JSONObject jo = new JSONObject(json);
		
		loginDomain.retcode = jo.getInt("retcode");
		loginDomain.msg = jo.getString("msg");
		loginDomain.data = jo.getString("data");
		
		return loginDomain;
	}
	
}
