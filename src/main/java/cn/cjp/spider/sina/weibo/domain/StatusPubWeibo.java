package cn.cjp.spider.sina.weibo.domain;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cjp.base.utils.JacksonUtil;

public class StatusPubWeibo {

	/**
	 * 1 or -1<br>
	 * -100 as you should login again
	 */
	int ok;
	
	String msg;
	/**
	 * 微博id
	 */
	String id;
	
	public static StatusPubWeibo fromJson(String json) throws JSONException{
		StatusPubWeibo statusPubWeibo = new StatusPubWeibo();
		
		JSONObject jsonObject = new JSONObject(json);
		statusPubWeibo.setOk(jsonObject.getInt("ok"));
		if(statusPubWeibo.getOk() == 1){
			statusPubWeibo.setId(jsonObject.getString("id"));
		}
		statusPubWeibo.setMsg(jsonObject.getString("msg"));
		return statusPubWeibo;
	}
	
	public String toString(){
		return JacksonUtil.toJson(this);
	}
	
	public int getOk() {
		return ok;
	}
	public void setOk(int ok) {
		this.ok = ok;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
