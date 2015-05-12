package cn.cjp.spider.common.domain;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import cn.cjp.spider.common.constant.SolrSearchAble;

public class SolrBaseDomain {

	// 抓取时间
	@Field(SolrSearchAble.CRAWLED_TIME)
	private long crawledTime = new Date().getTime();
	
	// 存储时间
	@Field(SolrSearchAble.SAVE_TIME)
	private long saveTime = new Date().getTime();
	
	@Field(SolrSearchAble.__TYPE__)
	public String TYPE = this.getClass().getSimpleName();
	
	@Field(SolrSearchAble.IS_DELETE)
	private boolean isDelete = false;

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}
	
	public long getCrawledTime() {
		return crawledTime;
	}

	public void setCrawledTime(long crawledTime) {
		this.crawledTime = crawledTime;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
