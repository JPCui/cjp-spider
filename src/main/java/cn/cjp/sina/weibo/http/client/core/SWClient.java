package cn.cjp.sina.weibo.http.client.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.domain.Const;
import cn.cjp.sina.weibo.http.proxy.bean.HttpProxyBean;
import cn.cjp.sina.weibo.http.proxy.service.HttpProxyService;
import cn.cjp.utils.SynchUtils;

/**
 * 新浪微博HttpClient<br>
 * 新浪的Header验证
 * 
 * @author REAL
 * 
 */
@SuppressWarnings("deprecation")
public class SWClient {
	private static final Logger logger = Logger.getLogger(SWClient.class);

	public List<DefaultHttpClient> proxyHttpClients = null;
	public DefaultHttpClient httpClient = null;

	/**
	 * 初始化HttpClient
	 * 
	 * @throws IOException
	 */
	public SWClient() {
		// // 初始化连接池
		// PoolingClientConnectionManager connManager = new
		// PoolingClientConnectionManager();
		// // 连接池最大连接数
		// connManager.setMaxTotal(200);
		// connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());

		initHttpClient();
		initProxyHttpClientPool();
	}

	private void initHttpClient() {
		httpClient = this.structHttpClient();
		// 重新请求次数
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
	}

	/**
	 * 初始化代理池
	 * 
	 * @throws IOException
	 */
	private void initProxyHttpClientPool() {
		proxyHttpClients = new ArrayList<DefaultHttpClient>();
		// 获取所有代理服务
		List<HttpProxyBean> proxyBeans = null;
		try {
			proxyBeans = HttpProxyService.getAll();
		} catch (IOException e) {
			logger.error(e);
		}
		// 实例化所有代理Http客户端
		for (int i = 0; i < proxyBeans.size(); i++) {
			HttpProxyBean proxyBean = proxyBeans.get(i);
			HttpHost httpHost = new HttpHost(proxyBean.getHost(), proxyBean.getPort());

			DefaultHttpClient proxyHttpClient = this.structHttpClient();
			// 重新请求次数
			proxyHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
			proxyHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
			proxyHttpClients.add(proxyHttpClient);
			logger.error("proxy setted ：" + httpHost.getHostName() + ":" + httpHost.getPort());
		}
	}

	/**
	 * 构造一个支持多线程的DefaultHttpClient
	 * 
	 * @return
	 */
	private DefaultHttpClient structHttpClient() {
		// 设置访问协议
		SchemeRegistry schreg = new SchemeRegistry();
		schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		// 构造一个支持多线程的DefaultHttpClient
		DefaultHttpClient httpClient = new DefaultHttpClient(
				new PoolingClientConnectionManager(schreg, 30, TimeUnit.SECONDS));
		// 重新请求次数
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));

		return httpClient;
	}

	/**
	 * Get a HttpClient randomly.
	 * 
	 * @return
	 */
	private HttpClient getRandomProxyHttpClient() {
		int random = RandomUtils.nextInt(proxyHttpClients.size());
		HttpClient httpClient = proxyHttpClients.get(random);
		return httpClient;
	}

	/**
	 * 封装HttpPOST，利用代理发送请求
	 * 
	 * @param url
	 * @param headers
	 * @param datas
	 * @return
	 */
	public String executePost(String url, Map<String, String> headers, Map<String, String> datas) {

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
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs);
				httpPost.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		int retryTimes = 3;
		String text = null;
		// 网络异常TruncatedChunkException，重新请求
		while (retryTimes-- != 0) {
			HttpResponse httpResponse = this.execute(httpPost, false);
			if (null == httpResponse) {
				continue;
			}
			try {
				text = this.getText(httpResponse);
			} catch (TruncatedChunkException e) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
				}
				logger.warn(e.getMessage());
				continue;
			}
		}
		httpPost.releaseConnection();
		return text;
	}

	/**
	 * 封装HttpGet，利用代理发送请求
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public String executeGet(String url, Map<String, String> headers) {
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

		HttpResponse httpResponse = this.execute(httpGet, false);
		if (null == httpResponse) {
			return null;
		}
		StatusLine statusLine = httpResponse.getStatusLine();
		int httpCode = statusLine.getStatusCode();
		if (httpCode / 100 != 2) {
			return null;
		}

		try {
			String text = this.getText(httpResponse);
			httpGet.releaseConnection();
			return text;
		} catch (TruncatedChunkException e) {
		}
		return null;
	}

	/**
	 * 执行http请求
	 * 
	 * @param httpRequest
	 *            http请求
	 * @param useProxy
	 *            使用代理标记， true for use ,or no use
	 * @return
	 */
	protected HttpResponse execute(HttpRequestBase httpRequest, boolean useProxy) {
		if (useProxy) {
			return this.executeByProxy(httpRequest);
		} else {
			return this.execute(httpRequest);
		}
	}

	/**
	 * 无代理发送请求
	 * 
	 * @param httpRequest
	 * @return
	 */
	protected HttpResponse execute(HttpRequestBase httpRequest) {
		logger.error("Requesting: " + httpRequest.getURI());
		HttpResponse httpResponse = null;
		// 重新连接次数
		synchronized (httpClient) {
			long now = System.currentTimeMillis();
			SynchUtils.put(now, "httpClient 占用", this.getClass());
			int reConnectTimes = 3;
			while (reConnectTimes-- != 0) {
				try {
					httpResponse = httpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains("Software caused connection abort")) {
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
					if (e.getMessage().contains("Software caused connection abort")) {
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
			SynchUtils.remove(now);
		}
		return httpResponse;
	}

	/**
	 * 通过代理服务执行请求
	 * 
	 * @param httpRequest
	 * @return
	 */
	protected HttpResponse executeByProxy(final HttpRequestBase httpRequest) {
		logger.warn("Requesting: " + httpRequest.getURI());
		HttpResponse httpResponse = null;
		// 从代理池里随机获取一个Http客户端
		HttpClient proxyHttpClient = this.getRandomProxyHttpClient();
		synchronized (proxyHttpClient) {
			// 异常重连机制
			long retryTime = System.currentTimeMillis();
			SynchUtils.put(retryTime, "异常重连机制", this.getClass());
			int reConnectTimes = 3;
			while (reConnectTimes-- != 0) {
				long executeTime = System.currentTimeMillis();
				SynchUtils.put(executeTime, "第" + reConnectTimes + "次发送请求 : " + httpRequest.getURI(), this.getClass());
				try {
					final String threadName = Thread.currentThread().getName();
					// 处理超时
					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(30 * 1000);
							} catch (InterruptedException e) {
							}
							if (!httpRequest.isAborted()) {
								httpRequest.abort();
								logger.warn(threadName + "-强行关闭请求: " + httpRequest.getURI());
							}
						}
					}).start();
					httpResponse = proxyHttpClient.execute(httpRequest);
				} catch (IllegalStateException e) {
					logger.error(e.getMessage());
					if (e.getMessage().contains("connection still allocated")) {
						// Invalid use of BasicClientConnManager: connection
						// still allocated
						httpRequest.abort();
						SynchUtils.remove(executeTime);
						continue;
					}
				} catch (SocketException e) {
					logger.error("SocketException : " + e.getMessage());
					if (e.getMessage().contains("Software caused connection abort")
							|| e.getMessage().contains("Connection reset")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
					}
					SynchUtils.remove(executeTime);
					continue;
				} catch (ClientProtocolException e) {
					logger.error(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				SynchUtils.remove(executeTime);
				break;
			}
			SynchUtils.remove(retryTime);
			// 如果重新连接3次不成功，休眠更长的时间进行重新连接
			if (reConnectTimes == 0) {
				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
				}
				long executeTime = System.currentTimeMillis();
				SynchUtils.put(executeTime, "最后一次发送请求 : " + httpRequest.getURI(), this.getClass());
				try {
					httpResponse = proxyHttpClient.execute(httpRequest);
				} catch (SocketException e) {
					if (e.getMessage().contains("Software caused connection abort")) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e1) {
						}
					}
					SynchUtils.remove(executeTime);
					logger.error("连接失败", e);
					return null;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				SynchUtils.remove(executeTime);
			}
		}
		// httpRequest.releaseConnection();
		return httpResponse;
	}

	/**
	 * 获取文本信息
	 * 
	 * @param httpResponse
	 * @return
	 * @throws TruncatedChunkException
	 */
	public String getText(HttpResponse httpResponse) throws TruncatedChunkException {
		try {
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		} catch (TruncatedChunkException e) {
			throw new TruncatedChunkException("Truncated chunk");
		} catch (ParseException e) {
			logger.error("请求数据转换失败", e);
		} catch (IOException e) {
			logger.error("请求数据转换失败", e);
		} catch (NullPointerException e) {
			logger.error("请求失败，导致无数据返回", e);
		}
		return null;
	}

}
