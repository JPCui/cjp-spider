package cn.cjp.spider.sina.weibo.http.proxy.bean;

/**
 * http代理实体
 * @author REAL
 *
 */
public class HttpProxyBean {

	private String host;
	
	private int port;
	
	public HttpProxyBean(String host, int port){
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
