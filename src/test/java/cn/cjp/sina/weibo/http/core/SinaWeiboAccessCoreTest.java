package cn.cjp.sina.weibo.http.core;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.cjp.sina.weibo.analyzer.GetFansAnalyzer;
import cn.cjp.sina.weibo.domain.UserDomain;
import cn.cjp.sina.weibo.http.core.SinaWeiboAccessCore;

@SuppressWarnings("deprecation")
public class SinaWeiboAccessCoreTest {

	SinaWeiboAccessCore accessCore;
	
	@Before
	public void before() throws IOException, InterruptedException, JSONException{
		String username = "1367471019@qq.com";
		String password = "15838228248";
		
		accessCore = SinaWeiboAccessCore.getInstance(username, password);
		
		Assert.assertNotNull(accessCore);
	}
	
	@Test
	public void testGetFans(){
		String json = accessCore.getFansByUid("3664167204", 1);

		List<UserDomain> userDomains = GetFansAnalyzer.analyzerJson(json);
		
		System.out.println(userDomains);
	}
	
	
}
