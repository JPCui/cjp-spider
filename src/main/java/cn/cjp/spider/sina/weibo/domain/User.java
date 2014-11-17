package cn.cjp.spider.sina.weibo.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	//
	String uid;
	
	/** 昵称 */
	String screenName;
	
	/** 头像 */
	String headUrl;
	
	public String toString(){
		return "{"
				+"\"uid\":"+uid+","
				+"\"screenName\":"+screenName+","
				+"\"headUrl\":"+headUrl
				+"}";
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public static User fromJson(JSONObject userJsonObj) throws JSONException {
		User user = new User();
		user.uid = userJsonObj.getString("id");
		user.setHeadUrl(userJsonObj.getString("profile_image_url"));
		user.setScreenName(userJsonObj.getString("screen_name"));
		return user;
	}
	
	
	
}
