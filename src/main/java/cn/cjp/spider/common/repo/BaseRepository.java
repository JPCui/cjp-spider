package cn.cjp.spider.common.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.repository.SolrCrudRepository;

import cn.cjp.spider.common.repo.impl.BaseRepositoryImpl;

/**
 * 
 * @author <a href="http://wpa.qq.com/msgrd?v=3&uin=624498030&site=qq&menu=yes">崔金鹏</a>
 * @param <T>
 */
public interface BaseRepository<T> extends SolrCrudRepository<T, Serializable>{

	/**
	 * @see BaseRepositoryImpl#exeSimpleQuery(String[])
	 */
	ScoredPage<T> exeSimpleQuery(String[] kv);

	/**
	 * @see BaseRepositoryImpl#exeFacetQuery(String[], String[])
	 * @deprecated use {@link BaseRepository#exeFacetQuery(List, FacetOptions)} instead
	 */
	FacetPage<T> exeFacetQuery(String[] kv,String[] fields);

	/**
	 * @see BaseRepositoryImpl#exeHighlightQuery(String[], String[])
	 */
	HighlightPage<T> exeHighlightQuery(String[] kv,String[] fields);

	/**
	 * @see BaseRepositoryImpl#setPageable(Pageable)
	 */
	void setPageable(Pageable pageable);

	/**
	 * 根据id批量删除
	 * @param ids
	 */
	void deleteAll(List<String> ids);

	/**
	 * Facet统计查询
	 * @param criterias 查询结构化参数，见{@link Criteria}
	 * @param facetOptions Facet 选项
	 * @return
	 */
	FacetPage<T> exeFacetQuery(List<Criteria> criterias,FacetOptions facetOptions);

	/**
	 * 根据Criteria 执行检索，同样属于 {@link SimpleQuery}的一种
	 * @param pageable 分页参数
	 * @param criterias 查询条件，见{@link Criteria}
	 * @return
	 */
	ScoredPage<T> exeQuery(Pageable pageable, List<Criteria> criterias);




}
