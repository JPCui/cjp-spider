package cn.cjp.spider.chinanews.news.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring-solr.xml","classpath:/spring-redis.xml"})
public class ChinanewsSpiderTest {

	@Autowired
	ChinanewsSpider chinanewsSpider;
	
	@Test
	public void testRun(){
		
		chinanewsSpider.main();
		
	}
	
}
