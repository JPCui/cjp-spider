package cn.cjp.chinanews.news.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.cjp.chinanews.news.spider.ChinanewsSpider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring-solr.xml",
		"classpath:/spring-redis.xml", "classpath:/spring-beans.xml" })
public class ChinanewsSpiderTest {

	@Autowired
	ChinanewsSpider chinanewsSpider;

	@Test
	public void testRun() {
		chinanewsSpider.crawling(2);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/spider/spring.xml", "classpath:/spring-redis.xml",
				"classpath:/spring-beans.xml");
		
		ChinanewsSpider chinanewsSpider = context.getBean("chinanewsSpider", ChinanewsSpider.class);

		chinanewsSpider.crawling(2);
		
//		chinanewsSpider.getThreadMap();
		
		
	}

}
