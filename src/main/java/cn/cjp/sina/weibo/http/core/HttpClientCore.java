package cn.cjp.sina.weibo.http.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.http.proxy.bean.HttpProxyBean;
import cn.cjp.sina.weibo.http.proxy.service.HttpProxyService;

public class HttpClientCore {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HttpClientCore.class);

	public DefaultHttpClient httpClient = null;

	/**
	 * 初始化HttpClient
	 */
	public HttpClientCore() {
		// 初始化连接池
		PoolingClientConnectionManager connManager = new PoolingClientConnectionManager();
		// 连接池最大连接数
		connManager.setMaxTotal(200);
		connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());

		httpClient = new DefaultHttpClient();
		// 重新请求次数
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
						3, true));
	}

	/**
	 * 封装HttpPOST，利用代理发送请求
	 * 
	 * @param url
	 * @param headers
	 * @param datas
	 * @return
	 */
	public HttpResponse executePost(String url, Map<String, String> headers,
			Map<String, String> datas) {

		HttpPost httpPost = new HttpPost(url);
		// header setting
		for (String key : Const.HEADER_DEFAULT.keySet()) {
			httpPost.setHeader(key, Const.HEADER_DEFAULT.get(key));
		}
		// header replacing
		if (headers != null && headers.size() != 0) {
			for (String key : headers.keySet()) {
				httpPost.setHeader(key, headers.get(key));
			}
		}
		// set request params
		if (datas != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (String key : datas.keySet()) {
				pairs.add(new BasicNameValuePair(key, datas.get(key)));
			}
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						pairs);
				httpPost.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		HttpResponse httpResponse = null;
		httpResponse = this.executeByProxy(httpPost);
		return httpResponse;
	}

	/**
	 * 封装HttpGet，利用代理发送请求
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public HttpResponse executeGet(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);
		// set header
		for (String key : Const.HEADER_DEFAULT.keySet()) {
			httpGet.setHeader(key, Const.HEADER_DEFAULT.get(key));
		}
		// replace header
		if (headers != null && headers.size() != 0) {
			for (String key : headers.keySet()) {
				httpGet.setHeader(key, headers.get(key));
			}
		}

		HttpResponse httpResponse = this.executeByProxy(httpGet);
		return httpResponse;
	}

	protected HttpResponse execute(HttpRequestBase httpRequest) {
		logger.info("Requesting: " + httpRequest.getURI());
		HttpResponse httpResponse = null;
		// 重新连接次数
		synchronized (httpClient) {

			int reConnectTimes = 3;
			while (reConnectTimes-- != 0) {
				try {
					httpResponse = httpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains(
							"Software caused connection abort")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
						continue;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			// 如果重新连接3次不成功，休眠更长的时间进行重新连接
			if (reConnectTimes == 0) {
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
				}
				try {
					httpResponse = httpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains(
							"Software caused connection abort")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
					}
					logger.error("连接失败", e);
					return null;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	/**
	 * 通过代理服务执行请求
	 * 
	 * @param httpRequest
	 * @return
	 */
	protected HttpResponse executeByProxy(HttpRequestBase httpRequest) {
		logger.info("Requesting: " + httpRequest.getURI());
		HttpResponse httpResponse = null;
		// 重新连接次数
		synchronized (httpClient) {
			// 设置代理
			HttpProxyBean proxyBean = HttpProxyService.getRandomProxy();
			HttpHost httpHost = new HttpHost(proxyBean.getHost(),
					proxyBean.getPort());
			this.httpClient.getParams().setParameter(
					ConnRoutePNames.DEFAULT_PROXY, httpHost);
			logger.info("setting proxy ：" + httpHost.getHostName() + ":"
					+ httpHost.getPort());

			// 请求重连机制
			int reConnectTimes = 3;
			while (reConnectTimes-- != 0) {
				try {
					httpResponse = httpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains(
							"Software caused connection abort")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
						continue;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			// 如果重新连接3次不成功，休眠更长的时间进行重新连接
			if (reConnectTimes == 0) {
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
				}
				try {
					httpResponse = httpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains(
							"Software caused connection abort")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
					}
					logger.error("连接失败", e);
					return null;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	/**
	 * 获取文本信息
	 * 
	 * @param httpResponse
	 * @return
	 */
	public String getText(HttpResponse httpResponse) {
		HttpEntity httpEntity = httpResponse.getEntity();
		try {
			return EntityUtils.toString(httpEntity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
