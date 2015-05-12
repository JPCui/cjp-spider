package cn.cjp.spider.common.constant;

/**
 * solr 公共检索字段
 * @author REAL
 *
 */
public class SolrSearchAble {

	/**
	 * 实体类型
	 */
	public static final String __TYPE__ = "__TYPE__";
	/**
	 * ID
	 */
	public static final String ID = "id";
	/** 抓取时间 */
	public static final String CRAWLED_TIME = "crawled_time";
	/** 存储时间 */
	public static final String SAVE_TIME = "save_time";
	/** 情感处理时间 */
	public static final String SENTI_HANDLER_TIME = "senti_handler_time";
	/** 命名实体 */
	public static final String NAME_ENTITY = "name_entity";
	/** 删除标记 */
	public static final String IS_DELETE = "is_delete";
	/**
	 * 情感处理标记
	 */
	public static final String SENTI_HANDLER_FLAG = "sentiHandlerFlag";

	/** 新闻标题 */
	public static final String TITLE = "title";
	/** 新闻频道（类别） */
	public static final String CHANNEL = "channel";
	/** 新闻来源 */
	public static final String SOURCE_FROM = "source_from";
	/**
	 * url of comment
	 */
	public static final String COMMENT_URL = "comment_url";
	/**
	 * num of comment
	 */
	public static final String COMMENT_NUM = "comment_num";
	
	/**
	 * 新闻ID（一些新闻有自己的ID）
	 */
	public static final String NEWS_ID = "news_id";
	/**
	 * （新闻/博客/帖子）发表时间
	 */
	public static final String PUB_TIME = "pub_time";
	/**
	 * （新闻/博客/帖子）作者
	 */
	public static final String AUTHOR = "author";
	public static final String CONTENT = "content";
	public static final String SUMMARY = "summary";
	public static final String KEY_WORDS = "key_words";
	public static final String CONTENT_SENTENCES = "content_sentences";
	
	// sentiment
	/**
	 * 是否为情感句
	 */
	public static final String IS_SENTIMENT = "is_sentiment";
	/**
	 * 情感值
	 */
	public static final String SENTI_VALUE = "sentiValue";
	/**
	 * 正面情感值
	 */
	public static final String POS_VALUE = "posValue";
	/**
	 * 负面情感值
	 */
	public static final String NEG_VALUE = "negValue";
	/**
	 * 中性情感值
	 */
	public static final String NEUTER_VALUE = "neuterValue";
	
	public static final String URL = "url";
	
	public static final String COUNT = "count";
	
	//TOPIC

	public static final String TOPIC_ID = ID;
	public static final String TOPIC_CONTENT = "topicContent";
	public static final String TOPIC_GRADE = "topicGrade";
	public static final String TOPIC_CLASS = "topicClass";
	public static final String TOPIC_SET_TIME = "topicSetTime";

	public static final String NEWS_TOPIC_IDS = "newsTopicId";
	public static final String NEWS_TOPIC_WEIGHTS = "newsTopicWeights";
	
	//Weibo
	public static final String WEIBO_UID = "weiboUid";
	public static final String WEIBO_COMMENT_NUM = "weiboCommentNum";
	public static final String WEIBO_FORWARD_NUM = "weiboForwardNum";
	public static final String WEIBO_LIKE_NUM = "weiboLikeNum";
	public static final String WEIBO_FOWARD = "weiboForword";
	
	public static final String WEIBO_OWEIBO_UNAME = "oWeiboUname";
	public static final String WEIBO_OWEIBO_UID = "oWeiboUid";
	public static final String WEIBO_OWEIBOID = "oWeiboId";
	
	public static final String WEIBO_TIME = "weiboTime";
	public static final String WEIBO_DEV = "weiboDev";
	public static final String WEIBO_CONTEXT_LINK = "weiboContextLink";
	public static final String WEIBO_CONTEXT = "weiboContext";
	public static final String WEIBO_CONTEXT_HREF = "weiboHref";
	
	// WEIBO TOPIC
	public static final String WEIBO_ID_FK = "weiboId_FK";

	public static final String WEIBO_TOPIC_IDS = "weiboTopicId";
	public static final String WEIBO_TOPIC_WEIGHTS = "weiboTopicWeights";
	
	
	
}
