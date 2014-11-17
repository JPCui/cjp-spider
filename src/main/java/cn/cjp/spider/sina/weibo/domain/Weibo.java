package cn.cjp.spider.sina.weibo.domain;

import java.util.ArrayList;
import java.util.List;

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
	
	public static Weibo fromJson(JSONObject jsonObj){
		Weibo weibo = new Weibo();
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
