/**
 * @author REAL
 */
package cn.cjp.sina.weibo.proxy.service;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import cn.cjp.sina.weibo.proxy.bean.HttpProxyBean;
import cn.cjp.utils.PropertiesUtils;

/**
 * Http代理服务类
 * @author REAL
 *
 */
public class HttpProxyService{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HttpProxyService.class);
	
	static PropertiesUtils propsUtils;
	static int proxyNum;
	
	static{
		propsUtils = new PropertiesUtils("http-proxy.properties");
		proxyNum = Integer.parseInt(propsUtils.getPropValue("proxy.num"));
	}
	
	/**
	 * 随机获取一条代理
	 * @return
	 */
	public static HttpProxyBean getRandomProxy(){
		if(proxyNum == 0){
			return null;
		}
		
		int random = RandomUtils.nextInt(proxyNum+1);
		try {
			String host = propsUtils.getPropValue("host"+random);
			int port = Integer.parseInt(propsUtils.getPropValue("port"+random));
			return new HttpProxyBean(host, port);
		} catch (Exception e) {
			logger.error("获取代理错误("+random+")", e);
		}
		return null;
	}
	
}

