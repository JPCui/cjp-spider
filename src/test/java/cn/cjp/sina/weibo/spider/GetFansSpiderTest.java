package cn.cjp.sina.weibo.spider;

import java.util.HashMap;
import java.util.Map;

import cn.cjp.sina.weibo.spider.GetFansSpider;

public class GetFansSpiderTest {

	public static void main(String[] args) {
		Map<String, String> accounts = new HashMap<String, String>();
		accounts.put("1367471019@qq.com", "15838228248");
		accounts.put("15838228248", "jinpeng2mingli");

		GetFansSpider spider = new GetFansSpider(accounts);
		spider.setSavedFileDir("D:/_weibo_data_/");
		
		spider.initWaitingUidList("1092617025");

		for (int i = 0; i < 3; i++) {
			new Thread(spider).start();
		}
	}

}
