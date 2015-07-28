package cn.cjp.redis.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.cjp.chinanews.news.spider.ChinanewsThread;
import cn.cjp.redis.domain.ThreadDomain;
import cn.cjp.utils.JacksonUtil;


/**
 * 
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring-redis.xml","classpath:/spring-solr.xml","classpath:/spring-beans.xml"})
public class ThreadDaoTest {
	
	@Autowired
	ThreadDao threadDao;
	@Autowired
	ChinanewsThread chinanewsSpider;
	
	@Test
	public void testSave(){
		ThreadDomain threadDomain = new ThreadDomain();
		
		threadDomain.setId("123456");
		threadDomain.setName("helloThread-1");
		
		threadDao.save(threadDomain);
		
		ThreadDomain threadDomainFromDB = threadDao.read("123456");
		
		Assert.assertEquals(threadDomain.getName(), threadDomainFromDB.getName());
	}
	
	@Test
	public void testListOps(){
		ThreadDomain threadDomain = new ThreadDomain();
		threadDomain.setId("1");
		threadDomain.setName("thread-1");
		
		long l = threadDao.lpush(threadDomain);
		System.out.println(l);
		
		l = threadDao.lpush(threadDomain);
		System.out.println(l);
		
		ThreadDomain threadDomain2 = threadDao.lpop();
		
		System.out.println(JacksonUtil.toJson(threadDomain2));
		System.out.println(ThreadDomain.class.getSimpleName());
		
	}
	
	

}
