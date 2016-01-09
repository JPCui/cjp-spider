package cn.cjp.common.domain;

import org.apache.solr.client.solrj.beans.Field;

import cn.cjp.common.constant.SolrSearchAble;

/**
 * 新闻Solr基类
 * @author REAL
 *
 */
public class SolrNewsDomain extends SolrBaseDomain{

	/**
	 * 发布时间
	 */
	@Field(SolrSearchAble.PUB_TIME)
	private long pubTime;

	/**
	 * @return 发布时间
	 */
	public long getPubTime() {
		return pubTime;
	}

	/**
	 * @param pubTime 发布时间
	 */
	public void setPubTime(long pubTime) {
		this.pubTime = pubTime;
	}
	
}
