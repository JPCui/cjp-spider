package cn.cjp.spider.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.jsoup.Connection.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.cjp.base.utils.CodeUtil;
import cn.cjp.spider.domain.weibo.sina.LoginDomain;

public class SinaWeiboAccessTest {

	Map<String, String> cookies = null;
	
	@SuppressWarnings("serial")
	@Before
	public void before() throws IOException, InterruptedException, JSONException{
		Response response = SinaWeiboAccess.login(new HashMap<String, String>(){{
			put("username", "1367471019@qq.com");
			put("password", "15838228248");
			put("savestate", "1");
			put("ec", "0");
			put("entry", "mweibo");
		}});
		LoginDomain loginDomain = LoginDomain.fromJson(response.body());
			Assert.assertEquals("登录", loginDomain.retcode, LoginDomain.LOGIN_SUCCESS);
		cookies = response.cookies();
	}
	
	@Test
	public void testGetWeibosOfUser() throws IOException, InterruptedException{
		String json = SinaWeiboAccess.getWeibosOfUser(cookies, "1813080181", 1);
		
		json = CodeUtil.unicodeToString(json);
		System.out.println(json);
	}
	
	@Test
	public void testGetPLsOfWeibo() throws IOException, InterruptedException{
		String json = SinaWeiboAccess.getPLsOfWeibo(cookies, "1813080181", "3776550627257375", 1);
		
		json = CodeUtil.unicodeToString(json);
		System.out.println(json);
	}
	
	@Test
	public void testGetAttitudesOfWeibo() throws IOException, InterruptedException{
		String json = SinaWeiboAccess.getAttitudesOfWeibo(cookies, "3776550627257375", 1);
		
		json = CodeUtil.unicodeToString(json);
		System.out.println(json);
	}
	
}
