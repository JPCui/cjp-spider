package cn.cjp.chinanews.news.spider;

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

import cn.cjp.chinanews.news.analyzer.HtmlAnalyzer;
import cn.cjp.chinanews.news.domain.ChinanewsDomain;
import cn.cjp.utils.JacksonUtil;
import cn.cjp.utils.NewsDomainUtil;
import cn.cjp.utils.URLUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * <a href="http://www.chinanews.com/">中国新闻网</a>新闻采集
 * 
 * @author REAL
 * @deprecated use {@link URLSpider}, {@link NewsSpider}
 */
// @Transactional
@Deprecated
public class ChinanewsThread extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ChinanewsThread.class);

	@Autowired
	JedisPool jedisPool;

	/**
	 * 获取主页的所有新闻链接
	 */
	public void run() {
		Document doc = null;
		int retryTimes = 3;

		// 获取root页面
		while (retryTimes-- != 0) {
			try {
				doc = Jsoup.connect(Config.rootURL).get();
				break;
			} catch (Exception e) {
				logger.info("连接超时。正在重新连接");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					logger.warn(e1);
				}
				continue;
			}
		}

		// 爬取种子url
		if (doc != null) {
			getNewsDomainPageUrls(doc);
		}

		// 获取数据主题
		getNewsDomainFromUrls();
	}

	/**
	 * 
	 * @param doc
	 */
	public void getNewsDomainPageUrls(Document doc) {
		if (doc.getElementsByTag("a") == null)
			return;
		Elements tags_a = doc.getElementsByTag("a");// 获取所有的<a>标签
		int n = tags_a.size();
		if (n == 0) {
			logger.error("URL获取失败，重新获取，同时请查看网络连接状态");
			logger.error(doc);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
			// run();
			return;
		}
		for (int i = 0; i < n; i++) {
			String href = tags_a.get(i).attr("href");
			if (href.indexOf(Config.domain_name) != -1 && href.indexOf("htm") != -1) {
				// 将新URL入队列
				try (Jedis jedis = jedisPool.getResource()) {
					long reply = jedis.sadd(Config.SPIDER_SET_REDIS_KEY, href);
					if (reply != 0) {
						jedis.lpush(Config.SPIDER_LIST_REDIS_KEY, href);
					}
					logger.info("共获取url数量：" + jedis.llen(Config.SPIDER_LIST_REDIS_KEY));
				}
			}
		}

	}

	/**
	 * 从队列中提取新闻实体 <br>
	 * 对不同网站分析需要做更改
	 */
	public void getNewsDomainFromUrls() {
		String urlFrom = null;
		// 从队列中取出一个url
		try (Jedis jedis = jedisPool.getResource()) {
			urlFrom = jedis.brpop(new String[] { Config.SPIDER_LIST_REDIS_KEY, "0" }).get(1);
		}
		try {
			// 可能会因网速问题，出现socket异常
			urlFrom = URLUtils.getQueryURL(urlFrom);
			Document doc = Jsoup.connect(urlFrom).get();
			final ChinanewsDomain news = HtmlAnalyzer.analyzeChinanew(doc);
			if (null != news) {
				news.setUrl(urlFrom);
				news.setId(UUID.nameUUIDFromBytes(urlFrom.getBytes("UTF-8")).toString());
				// 保存到DB
				if (saveNewsDomainInDB(news)) {
					// 另开一个线程进行文件保存
					new Thread(new Runnable() {
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
			// news = chinanewsRepo.save(news);
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

		FileOutputStream fout = new FileOutputStream(file, false);

		fout.write(("URL：" + news.getUrl()).getBytes());
		fout.write("\r\n".getBytes());
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

}
