package cn.cjp.spider.demo;

import java.util.HashMap;
import java.util.Map;

import cn.cjp.spider.sina.weibo.domain.Const;
import cn.cjp.spider.sina.weibo.domain.LoginDomain;
import cn.cjp.spider.sina.weibo.domain.StatusPubWeibo;
import cn.cjp.spider.sina.weibo.http.client.core.SWClient;

public class Demo3 {

	public static void main(String[] args) throws Exception {
		String json = null;
		
		Map<String,String> loginDatas = new HashMap<String,String>();
		loginDatas.put("username","1367471019@qq.com");
		loginDatas.put("password","15838228248");
		
		Map<String,String> pubDatas = new HashMap<String,String>();
		pubDatas.put("content", "11111111111");
		
		SWClient clientCore = new SWClient();
		
		json = clientCore.executePost(Const.LOGIN_URL, Const.HEADER_FOR_LOGIN, loginDatas);
		System.out.println(LoginDomain.fromJson(json));
		
		
		json = clientCore.executePost(Const.B_PUB_WEIBO, Const.HEADER_DEFAULT, pubDatas);
		System.out.println(StatusPubWeibo.fromJson(json));

	}
	
	
}
