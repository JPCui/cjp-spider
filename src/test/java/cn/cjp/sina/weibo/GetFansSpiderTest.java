package cn.cjp.sina.weibo;

import org.junit.Test;

import cn.cjp.sina.weibo.proxy.bean.HttpProxyBean;
import cn.cjp.sina.weibo.proxy.service.HttpProxyService;
import cn.cjp.sina.weibo.spider.GetFansSpider;

public class GetFansSpiderTest {

	@Test
	public void testGetFans(){
		HttpProxyBean proxyBean = HttpProxyService.getRandomProxy();
		GetFansSpider fansSpider = new GetFansSpider(proxyBean);
		
		fansSpider.setAcount("1367471019@qq.com", "15838228248");
		fansSpider.setSavedFileDir("D:/_weibo_data_");
		fansSpider.initWaitingUidList("5574133962");
		
		for(int i=0; i<5; i++){
			new Thread(fansSpider).start();
		}
		
		
	}
	
}
