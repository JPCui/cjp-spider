package cn.cjp.spider.domain.weibo.sina;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	//
	String uid;
	
	/** 昵称 */
	String screenName;
	
	/** 头像 */
	String headUrl;
	
	boolean follow_me;
	
	/** weibos num */
	int weiboNum;
	
	String fansNum;
	int followNum;
	String gender;
	
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
		user.setGender(userJsonObj.getString("gender"));
		user.setFansNum(userJsonObj.get("fansNum")+"");
		user.setWeiboNum(userJsonObj.getInt("statuses_count"));
		user.setFollow_me(userJsonObj.getBoolean("follow_me"));
		return user;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public int getWeiboNum() {
		return weiboNum;
	}

	public void setWeiboNum(int weiboNum) {
		this.weiboNum = weiboNum;
	}

	public String getFansNum() {
		return fansNum;
	}

	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
}
