package cn.cjp.sina.weibo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import cn.cjp.sina.weibo.http.proxy.service.HttpProxyService;
import cn.cjp.utils.JacksonUtil;

@SuppressWarnings("deprecation")
public class HttpProxyServiceTest {

	@Test
	public void testGetProxy() throws IOException {
		Object obj = HttpProxyService.getAll();

		System.out.println(JacksonUtil.toJson(obj));
	}

	@Test
	public void testRequestByProxy() throws ClientProtocolException, IOException {
		String host = "218.97.194.220";
		int port = 80;

		HttpHost httpHost = new HttpHost(host, port);
		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
		HttpGet httpGet = new HttpGet("http://wap.baidu.com");

		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();

		System.out.println(EntityUtils.toString(httpEntity));

	}

}
