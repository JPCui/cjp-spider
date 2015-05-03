package cn.cjp.spider.sina.weibo.http.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.cjp.spider.sina.weibo.domain.StatusPubWeibo;
import cn.cjp.spider.sina.weibo.http.access.SWHttpClientAccessCore;

public class SinaWeiboHttpClientAccessCoreTest {
	
	@Test
	public void testPubWeibo() throws IOException {
		Map<String, String> accounts = new HashMap<String, String>();
		accounts.put("1367471019@qq.com", "15838228248");

		SWHttpClientAccessCore accessCore = SWHttpClientAccessCore
				.getInstance(accounts);
		
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("content", "我是一条微博");
		StatusPubWeibo status = accessCore.pubWeibo(datas);
		System.out.println(status);
	}
}

