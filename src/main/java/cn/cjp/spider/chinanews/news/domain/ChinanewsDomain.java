package cn.cjp.spider.chinanews.news.domain;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import cn.cjp.spider.common.constant.SolrSearchAble;
import cn.cjp.spider.common.domain.SolrNewsDomain;

public class ChinanewsDomain extends SolrNewsDomain{

	private static final long serialVersionUID = 1L;

	// 频道
	@Field(SolrSearchAble.CHANNEL)
	private String channel;
	
	// 新闻来源
	@Field(SolrSearchAble.SOURCE_FROM)
	private String sourceFrom;

	// 新闻url
	@Id
	@Field(SolrSearchAble.ID)
	@Indexed(SolrSearchAble.ID)
	private String id;
	
	@Field(SolrSearchAble.URL)
	private String url;
  
	// 作者
	@Field(SolrSearchAble.AUTHOR)
	private String author;
	
	// 标题
	@Field(SolrSearchAble.TITLE)
	private String title;

	// 新闻内容
	@Field(SolrSearchAble.CONTENT)
	private String content;

	// 原格式新闻内容
	@Field(SolrSearchAble.PRE_CONTENT)
	private String preContent;
	
	// 新闻内容
	@Field(SolrSearchAble.SUMMARY)
	private String summary;
	
	// 新闻Id
	@Field(SolrSearchAble.NEWS_ID)
	private String newsId;
	
	//评论的url
	@Field(SolrSearchAble.COMMENT_URL)
	private String commentUrl;
	
	// 评论数
	@Field(SolrSearchAble.COMMENT_NUM)
	private int commentNum;
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthorName(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getCommentUrl() {
		return commentUrl;
	}

	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}


	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
