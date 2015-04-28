package cn.cjp.spider.sina.weibo.spider;

import cn.cjp.sina.weibo.spider.GetFansSpider;

public class SinaWeiboSpiderTest {

	public static void main(String[] args) {
		final GetFansSpider spider = new GetFansSpider();

		spider.setAcount("1367471019@qq.com", "15838228248");
		spider.setSavedFileDir("D:/_weibo_data_/");
		
		
		
//		for (int i = 0; i < 5; i++) {
			new Thread(spider).start();
//		}
	}
}
