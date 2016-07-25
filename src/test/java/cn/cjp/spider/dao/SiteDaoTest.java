package cn.cjp.spider.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.cjp.spider.domain.SiteDomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spider/spring.xml" })
@Transactional
public class SiteDaoTest {

	@Autowired
	SiteDao siteDao;

	@Test
	public void testSave() {
		SiteDomain siteDomain = new SiteDomain();
		siteDomain.setId("111");
		siteDomain.setSiteUrl("http://www.baidu.com/?k=Hello");
		siteDomain.setSiteDomain("baidu.com");

		siteDao.save(siteDomain);
	}

}
