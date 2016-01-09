package cn.cjp.sina.weibo.demo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpProxyDemo{
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		HttpClient client = new DefaultHttpClient();
		
		HttpHost httpHost = new HttpHost("113.253.44.78", 80);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
		
		HttpGet httpGet = new HttpGet("http://wap.baidu.com");
		
		httpGet.abort();
		HttpResponse httpResponse = client.execute(httpGet);
		
		HttpEntity entity = httpResponse.getEntity();
		
		System.out.println(EntityUtils.toString(entity));
		
	}
	
}

