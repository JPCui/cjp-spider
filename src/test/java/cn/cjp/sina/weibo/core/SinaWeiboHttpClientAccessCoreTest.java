package cn.cjp.sina.weibo.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.cjp.sina.weibo.core.SinaWeiboHttpClientAccessCore;
import cn.cjp.sina.weibo.domain.StatusPubWeibo;

public class SinaWeiboHttpClientAccessCoreTest {
	
	@Test
	public void testPubWeibo() throws IOException {

		SinaWeiboHttpClientAccessCore accessCore = SinaWeiboHttpClientAccessCore
				.getInstance("1367471019@qq.com", "15838228248");
		
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("content", "我是一条微博");
		StatusPubWeibo status = accessCore.pubWeibo(datas);
		System.out.println(status);
	}
}

