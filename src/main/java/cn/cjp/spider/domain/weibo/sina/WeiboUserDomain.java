package cn.cjp.spider.domain.weibo.sina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiboUserDomain {

	String uid;
	String screenName;
	/**
	 * 头像URL
	 */
	String profileImageUrl;
	/**
	 * 微博数
	 */
	int mBlogNum;
	int fansNum;
	int followNum;
	/**
	 * 收藏数
	 */
	int favorNum;
	String gender;
	Date createAt;

	public WeiboUserDomain setFieldFromJson(String json) {
		try {
			if (json.startsWith("[")) {
				JSONArray cards = new JSONArray(json);
				
				JSONObject cardUser = cards.getJSONObject(0)
						.getJSONArray("card_group").getJSONObject(0);
				this.setUid(cardUser.getJSONObject("user").getString("id"));
				this.setScreenName(cardUser.getJSONObject("user").getString("screen_name"));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);
					String createAt = cardUser.getJSONObject("user").getString("created_at");
					this.setCreateAt(sdf.parse(createAt));
				} catch (ParseException e) {
				}
				this.setGender(cardUser.getJSONObject("user").getString("ta")=="他"?"m":"w");
			}
		} catch (JSONException e) {
			return null;
		}
		return this;
	}

	public static WeiboUserDomain fromJson(String json) {
		WeiboUserDomain model = new WeiboUserDomain();

		try {
			if (json.startsWith("[")) {
				JSONArray cards = new JSONArray(json);
				
				JSONObject cardUser = cards.getJSONObject(0)
						.getJSONArray("card_group").getJSONObject(0);
				model.setUid(cardUser.getJSONObject("user").getString("id"));
				model.setScreenName(cardUser.getJSONObject("user").getString("screen_name"));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);
					String createAt = cardUser.getJSONObject("user").getString("created_at");
					model.setCreateAt(sdf.parse(createAt));
				} catch (ParseException e) {
				}
				model.setGender(cardUser.getJSONObject("user").getString("ta")=="他"?"m":"w");
			}
		} catch (JSONException e) {
			return null;
		}

		return model;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName
	 *            the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the profileImageUrl
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	/**
	 * @param profileImageUrl
	 *            the profileImageUrl to set
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * @return the mBlogNum
	 */
	public int getmBlogNum() {
		return mBlogNum;
	}

	/**
	 * @param mBlogNum
	 *            the mBlogNum to set
	 */
	public void setmBlogNum(int mBlogNum) {
		this.mBlogNum = mBlogNum;
	}

	/**
	 * @return the fansNum
	 */
	public int getFansNum() {
		return fansNum;
	}

	/**
	 * @param fansNum
	 *            the fansNum to set
	 */
	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	/**
	 * @return the followNum
	 */
	public int getFollowNum() {
		return followNum;
	}

	/**
	 * @param followNum
	 *            the followNum to set
	 */
	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	/**
	 * @return the favorNum
	 */
	public int getFavorNum() {
		return favorNum;
	}

	/**
	 * @param favorNum
	 *            the favorNum to set
	 */
	public void setFavorNum(int favorNum) {
		this.favorNum = favorNum;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the createAt
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * @param createAt
	 *            the createAt to set
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
