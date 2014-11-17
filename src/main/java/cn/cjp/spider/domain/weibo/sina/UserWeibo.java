package cn.cjp.spider.domain.weibo.sina;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户的微博
 * @author REAL
 */
public class UserWeibo {

	int ok = 0;
	
	long count = 0L;

	List<Weibo> weibos = new ArrayList<Weibo>();
	
	public String toString(){
		return "{"
				+ "\"ok\"" + ok + ","
				+ "\"count\"" + count + ","
				+ "\"weibos\"" + weibos
				+ "}";
	}

	public List<Weibo> getWeibos() {
		return weibos;
	}

	public void setWeibos(List<Weibo> weibos) {
		this.weibos = weibos;
	}
	
	public static UserWeibo fromJson(String json) throws JSONException
	{
		UserWeibo userWeibo = new UserWeibo();
		List<Weibo> weibos = new ArrayList<Weibo>();
		
		JSONObject jo = new JSONObject(json);
		userWeibo.setOk(jo.getInt("ok"));
		try {
			userWeibo.setCount(jo.getLong("count"));
		} catch (Exception e) {
			userWeibo.setCount(0);
		}
		
		JSONArray jsonArray = ((JSONObject)jo.getJSONArray("cards").get(0)).getJSONArray("card_group");
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			
			JSONObject weiboJsonObj = obj.getJSONObject("mblog");
			Weibo weibo = Weibo.fromJson(weiboJsonObj);
			
			JSONObject userJsonObj = weiboJsonObj.getJSONObject("user");
			User user = User.fromJson(userJsonObj);
			weibo.setUser(user);
			
			weibos.add(weibo);
		}
		
		userWeibo.setWeibos(weibos);
		
		return userWeibo;
	}

	public int getOk() {
		return ok;
	}

	public void setOk(int ok) {
		this.ok = ok;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	
}
