package cn.cjp.spider.chinanews.news.analyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cn.cjp.spider.chinanews.news.domain.ChinanewsDomain;
import cn.cjp.utils.Tools;

/**
 * HTML 解析器，HTML-->Domain
 * @author REAL
 *
 */
public class HtmlAnalyzer {
	
	/**
	 * 把HTML解析为 {@link ChinanewsDomain}
	 * @param doc
	 * @return
	 */
	public static ChinanewsDomain analyzeChinanew(Document doc){
		// 不是新闻页
		if (doc.getElementById("cont_1_1_2") == null)
			return null;
		
		ChinanewsDomain news = new ChinanewsDomain();

		Element ele_id_1 = doc.getElementById("cont_1_1_2")
				.getElementsByTag("h1").get(0);
		news.setTitle(Tools.handingNewsStr(ele_id_1.text()));

		Element ele_class_date_and_source = doc
				.getElementsByClass("left-time").get(0)
				.getElementsByClass("left-t").get(0);
		
		// 处理数据，得出时间和来源
		String date_and_source = ele_class_date_and_source.text();
		int date_index = date_and_source.indexOf("2");// 年的开头
		
		try {
			String pubTimeStr = date_and_source.substring(date_index, date_index + 17);
			SimpleDateFormat sdf = new SimpleDateFormat("Y年M月D日 H:m");
			news.setPubTime(sdf.parse(pubTimeStr).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String sourceFrom = date_and_source;
		if (sourceFrom != null && sourceFrom != "") {
			int beginIndex = 0, tempIndex;
			tempIndex = sourceFrom.indexOf("来源");
			beginIndex = tempIndex >= 0 ? tempIndex + 2 : beginIndex;
			tempIndex = sourceFrom.indexOf("来源：");
			beginIndex = tempIndex >= 0 ? tempIndex + 3 : beginIndex;
			tempIndex = sourceFrom.indexOf("来源： ");
			beginIndex = tempIndex >= 0 ? tempIndex + 4 : beginIndex;
			int source_end = 0;
			if (date_and_source.indexOf("参与互动") != -1) {
				source_end = date_and_source.indexOf("参与互动") - 1;
			} else {
				source_end = date_and_source.length();
			}
			news.setSourceFrom(date_and_source.substring(beginIndex,
					source_end).trim());
		} else {
			news.setSourceFrom("中国新闻网");
		}
		news.setContent(Tools.handingNewsStr(doc.getElementsByClass(
				"left_zw").text()));
		news.setCrawledTime(System.currentTimeMillis());
		
		return news;
	}

}
