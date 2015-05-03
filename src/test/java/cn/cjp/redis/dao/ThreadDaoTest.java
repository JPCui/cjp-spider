package cn.cjp.redis.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.cjp.redis.domain.ThreadDomain;


/**
 * 
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-redis.xml")
public class ThreadDaoTest {
	
	@Autowired
	ThreadDao threadDao;
	
	@Test
	public void testSave(){
		ThreadDomain threadDomain = new ThreadDomain();
		
		threadDomain.setId("123456");
		threadDomain.setName("helloThread-1");
		
		threadDao.save(threadDomain);
		
		ThreadDomain threadDomainFromDB = threadDao.read("123456");
		
		Assert.assertEquals(threadDomain.getName(), threadDomainFromDB.getName());
		
		
	}
	
	

}
