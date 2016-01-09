package cn.cjp.common.dao;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BaseDao<T> {

	public abstract boolean add(T object);

	public abstract void update(T object);

	public abstract void deleteById(String id);

	public abstract T getById(String uid);

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public abstract List<T> getAll();

	public List<T> exeHqlSearch(String hql,int first,int max);
	
	public boolean exeQuery(String q);

}