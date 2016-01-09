package cn.cjp.spider.dao.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

import cn.cjp.spider.dao.BaseDao;

public class BaseDaoImpl<T> implements BaseDao<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);

	@Resource
	private HibernateTemplate hibernateTemplate;

	public void save(T entity) {
		logger.info(hibernateTemplate.save(entity));
	}
	
	public void update(T entity){
		hibernateTemplate.update(entity);
	}
	
	
	

}
