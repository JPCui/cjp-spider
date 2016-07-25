package cn.cjp.spider.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao<T> {

	@Autowired
	SessionFactory sessionFactory;

	public void save(T t) {
		sessionFactory.getCurrentSession().persist(t);
	}

}
