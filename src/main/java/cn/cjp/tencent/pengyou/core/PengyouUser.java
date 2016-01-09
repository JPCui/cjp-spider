package cn.cjp.tencent.pengyou.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cjp.utils.JacksonUtil;
import cn.cjp.utils.JsonUtil;

/**
 * 朋友网用户实体
 * @author Administrator
 *
 */
public class PengyouUser {

	/**
	 * 朋友网为用户设置的唯一标识
	 */
	String hash;
	/**
	 * 真实姓名
	 */
	String realname;
	
	int sex;
	/**
	 * 头像
	 */
	String logo;
	/**
	 * 姓名首字母
	 */
	char firstletter;
	/**
	 * 用户的一些信息（地址、行业）
	 */
	String info;
	
	@SuppressWarnings("unchecked")
	public static List<PengyouUser> fromJson(String json){
		List<PengyouUser> users = new ArrayList<PengyouUser>();
		
		Map<String, Object> map = JsonUtil.jsonToMap(json);
		for(String firstKey : map.keySet()){
			LinkedHashMap<Object, Object> userMap = (LinkedHashMap<Object, Object>) map.get(firstKey);
			PengyouUser user = new PengyouUser();
			try {
				user.firstletter = userMap.get("firstletter").toString().charAt(0);
			} catch (Exception e) {
			}
			user.hash = userMap.get("hash") + "";
			user.info = userMap.get("info") + "";
			user.logo = userMap.get("logo") + "";
			user.realname = userMap.get("realname") + "";
			try {
				user.sex = Integer.parseInt(userMap.get("sex").toString());
			} catch (Exception e) {
			}
			users.add(user);
		}
		
		return users;
	}
	
	@Override
	public String toString(){
		return JacksonUtil.toJson(this);
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public char getFirstletter() {
		return firstletter;
	}

	public void setFirstletter(char firstletter) {
		this.firstletter = firstletter;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	
}
