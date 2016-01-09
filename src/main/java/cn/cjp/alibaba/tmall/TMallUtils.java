package cn.cjp.alibaba.tmall;

import java.util.Map;

import cn.cjp.utils.PropertiesUtils;

/**
 * 天猫爬虫的工具方法
 * @author Administrator
 *
 */
public class TMallUtils {

	/**
	 * 获取登陆cookies
	 * 
	 * @return
	 */
	public static Map<String, String> getLoginCookies() {
		PropertiesUtils propsUtils = new PropertiesUtils(
				"alibaba/tmall/login.cookie.properties");
		return propsUtils.getAll();
	}

	/**
	 * 获取登陆POST参数
	 * 
	 * @return
	 */
	public static Map<String, String> getLoginPostParams() {
		PropertiesUtils propsUtils = new PropertiesUtils(
				"alibaba/tmall/login.properties");
		return propsUtils.getAll();
	}

}
