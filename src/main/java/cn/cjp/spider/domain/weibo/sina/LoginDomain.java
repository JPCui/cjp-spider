package cn.cjp.spider.domain.weibo.sina;

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> data;
	
	@SuppressWarnings("serial")
	public static LoginDomain fromJson(String json) throws JSONException{
		LoginDomain loginDomain = new LoginDomain();
		JSONObject jo = new JSONObject(json);
		
		loginDomain.retcode = jo.getInt("retcode");
		if(loginDomain.retcode != LoginDomain.LOGIN_SUCCESS){
			return null;
		}
		loginDomain.msg = jo.getString("msg");
		JSONObject dataObj = jo.getJSONObject("data");
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("uid", dataObj.get("uid"));
		data.put("loginresulturl", dataObj.get("loginresulturl"));
		
		final JSONObject crossObj = dataObj.getJSONObject("crossdomainlist");
		data.put("crossdomainlist", new HashMap<String, String>(){{
			put("sina.com.cn", crossObj.getString("sina.com.cn"));
			put("weibo.com", crossObj.getString("weibo.com"));
		}});
		loginDomain.data = data;
		
		return loginDomain;
	}
	
}
