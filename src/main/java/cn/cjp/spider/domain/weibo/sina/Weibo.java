package cn.cjp.spider.domain.weibo.sina;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Weibo {

	long mid;
	
	String text;
	
	String source;
	
	// 发布时间
	String createTime;
	
	List<String> pic_ids = new ArrayList<String>();
	
	/**
	 * 转发
	 */
	int reportsNum;
	
	/**
	 * 评论
	 */
	int commentsNum;
	
	/**
	 * 赞
	 */
	int attitudesNum;
	
	User user;
	
	Weibo srcWeibo;
	
	public static Weibo fromJson(JSONObject weiboJsonObj) throws JSONException{
		Weibo weibo = new Weibo();
		weibo.setMid(weiboJsonObj.getLong("mid"));
		weibo.setSource(weiboJsonObj.getString("source"));
		weibo.setCreateTime(weiboJsonObj.getString("created_at"));
		weibo.setText(weiboJsonObj.getString("text"));
		weibo.setReportsNum(weiboJsonObj.getInt("reposts_count"));
		weibo.setCommentsNum(weiboJsonObj.getInt("comments_count"));
		weibo.setAttitudesNum(weiboJsonObj.getInt("attitudes_count"));
		if(!weiboJsonObj.isNull("pics")){
			JSONArray picsJsonArray = weiboJsonObj.getJSONArray("pics");
			for(int i=0; i<picsJsonArray.length(); i++){
				JSONObject picsJsonObj = picsJsonArray.getJSONObject(i);
				weibo.getPic_ids().add(picsJsonObj.getString("url"));
			}
		}
		if(!weiboJsonObj.isNull("user")){
			weibo.setUser(User.fromJson(weiboJsonObj.getJSONObject("user")));
		}
		if(!weiboJsonObj.isNull("retweeted_status")){
			weibo.setSrcWeibo(Weibo.fromJson(weiboJsonObj.getJSONObject("retweeted_status")));
		}
		return weibo;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public User getUser() {
		return user;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getReportsNum() {
		return reportsNum;
	}

	public void setReportsNum(int reportsNum) {
		this.reportsNum = reportsNum;
	}

	public int getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(int commentsNum) {
		this.commentsNum = commentsNum;
	}

	public int getAttitudesNum() {
		return attitudesNum;
	}

	public void setAttitudesNum(int attitudesNum) {
		this.attitudesNum = attitudesNum;
	}

	public Weibo getSrcWeibo() {
		return srcWeibo;
	}

	public void setSrcWeibo(Weibo srcWeibo) {
		this.srcWeibo = srcWeibo;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getPic_ids() {
		return pic_ids;
	}

	public void setPic_ids(List<String> pic_ids) {
		this.pic_ids = pic_ids;
	}
	
	
	
}
