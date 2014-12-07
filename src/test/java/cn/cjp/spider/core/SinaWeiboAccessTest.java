package cn.cjp.spider.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.cjp.base.utils.CodeUtil;
import cn.cjp.base.utils.JacksonUtil;
import cn.cjp.spider.domain.weibo.sina.HomeWeibo;
import cn.cjp.spider.domain.weibo.sina.LoginDomain;

public class SinaWeiboAccessTest {

	Map<String, String> cookies = new HashMap<String, String>();
	
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
		
		System.out.println(JacksonUtil.toJson(loginDomain));
	}
	
	@Test
	public void testGetHomeWeibo() throws IOException, InterruptedException, JSONException{
		String weibosJson = SinaWeiboAccess.getHomeWeibo(cookies, null, 1);
		System.out.println(new JSONArray(weibosJson));
		
		HomeWeibo homeWeibo = HomeWeibo.fromJson(weibosJson);
		System.out.println(JacksonUtil.toJson(homeWeibo));
	}
	
	@Test
	public void testPubWeibo() throws IOException, InterruptedException{
		Map<String, String> datas = new HashMap<String, String>();
		long time = new Date().getTime();
		System.out.println(time);
		datas.put("content", time+" @Real金鹏 ");
		
		// 位置（这里是我设置的一个韩国的位置）
		datas.put("poiid", "B2094457D269A6FC489F");
		
		String json = SinaWeiboAccess.pubWeibo(cookies, datas);
		
		System.out.println(CodeUtil.unicodeToString(json));
	}
	
	/**
	 * 打算用来测试发布微博并带有终端信息的功能（Android、iPhone .etc）
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testMPubWeibo() throws IOException, InterruptedException{
		Map<String, String> datas = new HashMap<String, String>();
//		// 校验参数（两个参数同时校验）
//		datas.put("c", "android");
//		datas.put("s", "142c4198");
		
		long time = new Date().getTime();
		System.out.println(time);
		datas.put("content", time+" @Real金鹏 ");
		
		// 位置（这里是我设置的一个韩国的位置）
		datas.put("poiid", "B2094457D269A6FC489F");
		
		String json = SinaWeiboAccess.pubMWeibo(cookies, "android", datas);
		
		System.out.println(CodeUtil.unicodeToString(json));
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
