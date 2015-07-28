package cn.cjp.spider.chinanews.news.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cjp.base.utils.JacksonUtil;
import cn.cjp.chinanews.news.analyzer.HtmlAnalyzer;
import cn.cjp.chinanews.news.domain.ChinanewsDomain;
import cn.cjp.chinanews.news.repo.ChinanewsRepo;
import cn.cjp.common.redis.RedisListOps;
import cn.cjp.utils.NewsDomainUtil;
import cn.cjp.utils.URLUtils;

/**
 * <a href="http://www.chinanews.com/">中国新闻网</a>新闻采集
 * 
 * @author REAL
 * 
 */
// @Transactional
public class ChinanewsThread extends Thread{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ChinanewsThread.class);

	@Autowired
	private ChinanewsRepo chinanewsRepo;
	@Autowired
	private RedisListOps redisListOps;

	public static final String SPIDER_LIST_REDIS_KEY = "chinanews_spider_list";

	// 网站首页
	private String rootURL = "http://www.chinanews.com/?y=yjwt09";
	// 新闻页面域名
	private String domain_name = "http://www.chinanews.com/";

	/**
	 * 获取主页的所有新闻链接
	 */
	public void run() {
		Document doc = null;
		int retryTimes = 3;
		
		while(retryTimes -- != 0){
			try {
				doc = Jsoup.connect(rootURL).get();
				break;
			} catch (Exception e) {
				System.out.println("连接超时。正在重新连接");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue;
			}
		}

		if(doc != null){
			getNewsDomainPageUrls(doc);
		}
		
		getNewsDomainFromUrls();
	}

	/**
	 * 根据html文档提取新闻实体
	 * @param doc
	 */
	public void getNewsDomainPageUrls(Document doc) {
		if (doc.getElementsByTag("a") == null)
			return;
		Elements tags_a = doc.getElementsByTag("a");// 获取所有的<a>标签
		int n = tags_a.size();
		if (n == 0) {
			System.out.println(doc);
			System.out.println("URL获取失败，重新获取，同时请查看网络连接状态");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			run();
			return;
		}
		for (int i = 0; i < n; i++) {
			String href = tags_a.get(i).attr("href");
			if (href.indexOf(domain_name) != -1 && href.indexOf("htm") != -1) {
				// 将新URL入队列
				synchronized (redisListOps) {
					redisListOps.lpush(SPIDER_LIST_REDIS_KEY, href);
					redisListOps.notifyAll();
					logger.info("共获取url数量："
							+ redisListOps.length(SPIDER_LIST_REDIS_KEY));
				}
			}
		}

	}

	/**
	 * 从队列中提取新闻实体 <br>
	 * 对不同网站分析需要做更改
	 */
	public void getNewsDomainFromUrls() {
		long n = 0;
		String urlFrom = null;
		// 从队列中取出一个url
		synchronized (redisListOps) {
			n = redisListOps.length(SPIDER_LIST_REDIS_KEY);
			if (n == 0) {
				try {
					redisListOps.wait();
				} catch (InterruptedException e) {
					logger.error("InterruptedException", e);
				}
				n = redisListOps.length(SPIDER_LIST_REDIS_KEY);
			}
			urlFrom = redisListOps.rpop(ChinanewsThread.SPIDER_LIST_REDIS_KEY);
		}
		try {
			// 可能会因网速问题，出现socket异常
			urlFrom = URLUtils.getQueryURL(urlFrom);
			Document doc = Jsoup.connect(urlFrom).get();
			final ChinanewsDomain news = HtmlAnalyzer.analyzeChinanew(doc);
			if (null != news) {
				news.setUrl(urlFrom);
				news.setId(UUID.nameUUIDFromBytes(urlFrom.getBytes("UTF-8"))
						.toString());
				// 保存到DB
				if (saveNewsDomainInDB(news)) {
					// 另开一个线程进行文件保存
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								saveNewsDomainInFile(news);
							} catch (IOException e) {
								logger.error("IOException", e);
							}
						}
					}).start();
				}
			}
		} catch (SocketTimeoutException e) {
			logger.debug("SocketTimeoutException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		this.getNewsDomainFromUrls();
	}

	public boolean saveNewsDomainInDB(ChinanewsDomain news) {
		try {
			news = chinanewsRepo.save(news);
		} catch (Exception e) {
			logger.error(JacksonUtil.toJson(news), e);
		}
		if (news != null) {
			return true;
		} else {
			return false;
		}
	}

	public void saveNewsDomainInFile(ChinanewsDomain news) throws IOException {
		NewsDomainUtil newsUtil = new NewsDomainUtil(news);
		File file = new File(newsUtil.buildHtmlFileName());
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fout = new FileOutputStream(file, true);

		fout.write(("标题：" + news.getTitle()).getBytes());
		fout.write("\r\n".getBytes());
		fout.write(("来源：" + news.getSourceFrom()).getBytes());
		fout.write("\r\n".getBytes());
		fout.write(("日期：" + news.getPubTime()).getBytes());
		fout.write("\r\n".getBytes());
		fout.write(("内容：" + news.getContent()).getBytes());
		fout.write("\r\n\r\n\r\n".getBytes());
		System.out.println("Save in file.");

		fout.close();
	}

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}

	public String getDomain_name() {
		return domain_name;
	}

	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}

	public ChinanewsRepo getChinanewsRepo() {
		return chinanewsRepo;
	}

	public void setChinanewsRepo(ChinanewsRepo chinanewsRepo) {
		this.chinanewsRepo = chinanewsRepo;
	}

	public RedisListOps getRedisListOps() {
		return redisListOps;
	}

	public void setRedisListOps(RedisListOps redisListOps) {
		this.redisListOps = redisListOps;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getSpiderListRedisKey() {
		return SPIDER_LIST_REDIS_KEY;
	}

}
