/**
 * @author REAL
 */
package cn.cjp.sina.weibo.http.proxy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.cjp.sina.weibo.http.proxy.bean.HttpProxyBean;

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
	private static final Logger logger = Logger.getLogger(HttpProxyService.class);

	/**
	 * 获取所有的代理
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<HttpProxyBean> getAll() throws IOException {
		Document doc = Jsoup.connect("http://www.proxy360.cn/default.aspx").ignoreContentType(true).get();
		Element projectListDiv = doc.getElementById("ctl00_ContentPlaceHolder1_upProjectList");

		Elements items = projectListDiv.getElementsByClass("proxylistitem");

		List<HttpProxyBean> proxyBeans = new ArrayList<HttpProxyBean>();
		for (int i = 0; i < items.size(); i++) {
			Element item = items.get(i);
			Elements itemProps = item.getElementsByClass("tbBottomLine");
			String host = itemProps.get(0).text().trim();
			int port = Integer.parseInt(itemProps.get(1).text().trim());
			proxyBeans.add(new HttpProxyBean(host, port));
		}
		return proxyBeans;
	}

	public static void main(String[] args) throws IOException {
		List<HttpProxyBean> list = getAll();
		logger.info(list);
		logger.info(list.size());
	}

}
