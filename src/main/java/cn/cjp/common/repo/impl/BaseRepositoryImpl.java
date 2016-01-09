package cn.cjp.common.repo.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import cn.cjp.common.constant.SolrSearchAble;
import cn.cjp.common.repo.BaseRepository;
import cn.cjp.utils.NumberParserUtil;

/**
 * 
 * @author 崔金鹏 <br>
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class BaseRepositoryImpl<T> extends
		SimpleSolrRepository<T, Serializable> implements BaseRepository<T> {

	protected int KV_SIZE = 3;
	/**
	 * 限制Facet返回结果的个数
	 */
	static int FACET_LIMIT_NUM = 50;
	/**
	 * Facet统计数的下限
	 */
	static int FACET_RESULT_LIMIT_NUM = 50;

	protected Pageable pageable = null;
	Class<T> t = null;

	protected FilterQuery filter = null;
	private Criteria filterCrit;

	public BaseRepositoryImpl() {
	}

	/**
	 * 查询初始化，根据__TYPE__作为过滤区别实体
	 */
	protected void init() {
		t = (Class<T>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		filter = new SimpleFilterQuery(new Criteria("__TYPE__").is(t
				.getSimpleName()));

	}

	/**
	 * 统计本类文档总数
	 */
	@Override
	public long count() {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.addFilterQuery(filter);
		simpleQuery.addCriteria(new Criteria().expression("*:*"));
		return this.getSolrOperations().queryForPage(simpleQuery, t)
				.getTotalElements();
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.addFilterQuery(filter);
		simpleQuery.setPageRequest(pageable);
		simpleQuery.addCriteria(new Criteria().expression("*:*"));
		return this.getSolrOperations().queryForPage(simpleQuery, t);
	}

	/**
	 * 同{@link BaseRepositoryImpl#findAll(Pageable)}
	 * 
	 * @see org.springframework.data.solr.repository.support.SimpleSolrRepository#findAll()
	 */
	@Override
	public Iterable<T> findAll() {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.addFilterQuery(filter);
		simpleQuery.setPageRequest(pageable);
		simpleQuery.addCriteria(new Criteria().expression("*:*"));
		return this.getSolrOperations().queryForPage(simpleQuery, t);
	}

	/**
	 * 用于屏蔽deleteAll，暂未实现
	 * 
	 * @see org.springframework.data.solr.repository.support.SimpleSolrRepository#deleteAll()
	 */
	public void deleteAll() {
	}

	public FilterQuery getFilter() {
		return filter;
	}

	public void setFilter(FilterQuery filter) {
		this.filter = filter;
	}

	@Override
	public ScoredPage<T> exeQuery(Pageable pageable, List<Criteria> criterias) {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.addFilterQuery(filter);
		for (int i = 0; i < criterias.size(); i += 1) {
			simpleQuery.addCriteria(criterias.get(i));
		}
		if (pageable != null)
			simpleQuery.setPageRequest(pageable);
		else
			simpleQuery.setPageRequest(new PageRequest(0, 5));

		return getSolrOperations().queryForPage(simpleQuery, t);
	}

	/**
	 * @param kv
	 *            键值对，例如：{"title", "白菜涨价", 2.0}，表示：{字段名, 字段值, 权重}
	 */
	@Override
	public ScoredPage<T> exeSimpleQuery(String[] kv) {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.addFilterQuery(filter);
		if (kv.length % KV_SIZE != 0)
			return new SolrResultPage<T>(new ArrayList<T>());
		if (kv.length < KV_SIZE) {
			simpleQuery.addCriteria(new Criteria().expression("*:*"));
		} else {
			for (int i = 0; i < kv.length; i += KV_SIZE) {
				simpleQuery.addCriteria(new Criteria(kv[i]).is(kv[i + 1])
						.boost(NumberParserUtil.toFloat(kv[i + 2])));
			}
		}
		simpleQuery.setPageRequest(pageable);
		return getSolrOperations().queryForPage(simpleQuery, t);
	}

	/**
	 * @param kv
	 *            键值对
	 */
	@Override
	public FacetPage<T> exeFacetQuery(String[] kv, String[] fields) {
		FacetQuery facetQuery = new SimpleFacetQuery();
		facetQuery.addFilterQuery(filter);
		if (kv.length % KV_SIZE != 0)
			return new SolrResultPage<T>(new ArrayList<T>());
		if (kv.length < KV_SIZE) {
			facetQuery.addCriteria(new Criteria().expression("*:*"));
		} else {
			for (int i = 0; i < kv.length; i += KV_SIZE) {
				facetQuery.addCriteria(new Criteria(kv[i]).is(kv[i + 1]).boost(
						NumberParserUtil.toFloat(kv[i + 2])));
			}
		}
		facetQuery.setFacetOptions(new FacetOptions(fields));
		facetQuery.setPageRequest(pageable);
		return getSolrOperations().queryForFacetPage(facetQuery, t);
	}

	/**
	 * @param kv
	 *            键值对
	 */
	@Override
	public FacetPage<T> exeFacetQuery(List<Criteria> criterias,
			FacetOptions facetOptions) {
		FacetQuery facetQuery = new SimpleFacetQuery();
		facetQuery.addFilterQuery(filter);
		for (Criteria criteria : criterias) {
			facetQuery.addCriteria(criteria);
		}
		facetQuery.setFacetOptions(facetOptions);
		facetQuery.setPageRequest(pageable);
		return getSolrOperations().queryForFacetPage(facetQuery, t);
	}

	/**
	 * @param kv
	 *            键值对
	 */
	@Override
	public HighlightPage<T> exeHighlightQuery(String[] kv, String[] fields) {
		HighlightQuery highlightQuery = new SimpleHighlightQuery();
		highlightQuery.addFilterQuery(filter);
		if (kv.length % KV_SIZE != 0)
			return new SolrResultPage<T>(new ArrayList<T>());
		highlightQuery.setHighlightOptions(new HighlightOptions()
				.addField(fields));
		if (kv.length < KV_SIZE) {
			highlightQuery.addCriteria(new Criteria().expression("*:*"));
		} else {
			for (int i = 0; i < kv.length; i += KV_SIZE) {
				highlightQuery.addCriteria(new Criteria(kv[i]).is(kv[i + 1])
						.boost(NumberParserUtil.toFloat(kv[i + 2])));
			}
		}
		highlightQuery.setPageRequest(pageable);
		return getSolrOperations().queryForHighlightPage(highlightQuery, t);
	}

	public Pageable getPageable() {
		return pageable;
	}

	/**
	 * 设置分页参数，参数使用见：{@link Pageable}
	 */
	@Override
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public Criteria getFilterCrit() {
		return filterCrit;
	}

	public void setFilterCrit(Criteria filterCrit) {
		this.filterCrit = filterCrit;
	}

	@Override
	public T findOne(Serializable id) {
		return getSolrOperations().queryForObject(
				new SimpleQuery(new Criteria(SolrSearchAble.ID).is(id)), t);
	}

	@Override
	public void deleteAll(List<String> ids) {
		this.getSolrOperations().deleteById(ids);
	}

}
