/**
 * @author REAL
 */
package cn.cjp.spider.sina.weibo.http.proxy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import cn.cjp.spider.sina.weibo.http.proxy.bean.HttpProxyBean;
import cn.cjp.utils.PropertiesUtils;

/**
 * Http代理服务业务类
 * 
 * @author REAL
 * 
 */
public class HttpProxyService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(HttpProxyService.class);

	static PropertiesUtils propsUtils;
	public static int proxyNum;

	static {
		propsUtils = new PropertiesUtils("http-proxy.properties");
		proxyNum = Integer.parseInt(propsUtils.getPropValue("proxy.num"));
	}
	
	/**
	 * 获取所有的代理
	 * @return
	 */
	public static List<HttpProxyBean> getAll(){
		List<HttpProxyBean> proxyBeans = new ArrayList<HttpProxyBean>();
		for(int i=1; i<=proxyNum; i++){
			String host = propsUtils.getPropValue("host" + i);
			int port = Integer.parseInt(propsUtils
					.getPropValue("port" + i));
			proxyBeans.add(new HttpProxyBean(host, port));
		}
		return proxyBeans;
	}

	/**
	 * 随机获取一条代理
	 * 
	 * @return
	 */
	public static HttpProxyBean getRandomProxy() {
		if (proxyNum == 0) {
			return null;
		}

		int random = RandomUtils.nextInt(proxyNum) + 1;
		try {
			String host = propsUtils.getPropValue("host" + random);
			int port = Integer.parseInt(propsUtils
					.getPropValue("port" + random));
			return new HttpProxyBean(host, port);
		} catch (Exception e) {
			logger.error("获取代理错误(" + random + ")[proxyNum: " + proxyNum + "]",
					e);
		}
		return null;
	}

}
