package cn.cjp.spider.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import cn.cjp.sina.weibo.core.HttpClientCore;
import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.domain.LoginDomain;
import cn.cjp.sina.weibo.domain.StatusPubWeibo;

public class Demo3 {

	public static void main(String[] args) throws Exception {
		HttpResponse httpResponse = null;
		String json = null;
		
		Map<String,String> loginDatas = new HashMap<String,String>();
		loginDatas.put("username","1367471019@qq.com");
		loginDatas.put("password","15838228248");
		
		Map<String,String> pubDatas = new HashMap<String,String>();
		pubDatas.put("content", "11111111111");
		
		HttpClientCore clientCore = new HttpClientCore();
		
		httpResponse = clientCore.executePost(Const.LOGIN_URL, Const.HEADER_FOR_LOGIN, loginDatas);
		json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		System.out.println(LoginDomain.fromJson(json));
		
		
		httpResponse = clientCore.executePost(Const.B_PUB_WEIBO, Const.HEADER_DEFAULT, pubDatas);
		json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		System.out.println(StatusPubWeibo.fromJson(json));

	}
	
	
}
