package cn.cjp.common.dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

import cn.cjp.utils.SolrjTransferUtil;

public class SolrjDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SolrjDao.class);

	/**
	 * Load Balenced HttpSolrService：可设置 SolrCloud 中每一台服务器地址，它会轮流使用设置的服务器。
	 * <br>由于增删改是post请求，所以引用HttpServer是为了解决spring data solr不能进行复杂的查询
	 */
	LBHttpSolrServer lbsServer = null;
//	ResourceUtil resourceUtil;
//	/**
//	 * 暂时先指定 5 个server url
//	 */
//	String[] solr_hosts = {"solr.host1","solr.host2","solr.host3","solr.host4","solr.host5"};
//	
//	/**
//	 * 加载solr服务配置
//	 */
//	public SolrjDao()
//	{
//		InputStream inStream = SolrjDao.class.getClassLoader().getResourceAsStream("edu/zut/cs/nlp/data/solr/config.properties");
//		resourceUtil = new ResourceUtil(inStream);
//		try {
//			lbsServer = new LBHttpSolrServer();
//			//加载资源文件
//			for(int i=0; i<solr_hosts.length; i++)
//			{
//				lbsServer.addSolrServer(resourceUtil.getValue(solr_hosts[i]));
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 注入
	 * @param solrHosts
	 */
	public SolrjDao(String... solrHosts)
	{
		try {
			lbsServer = new LBHttpSolrServer();
			//加载资源文件
			for(int i=0; i<solrHosts.length; i++)
			{
				lbsServer.addSolrServer(solrHosts[i]);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 查询
	 * @param kvs 键值对，即数组长度必须是2的倍数，例如:
	 * <pre>{
	 *  	"q", "contentSentences:警察",
	 *		"facet", "on",
	 *  	"facet.date.start","2013-1-1T0:0:0Z",
	 *  	"facet.date.end", "2013-10-10T0:0:0Z",
	 *  	"facet.date.gap", "+1MONTH"
	 * }</pre>
	 * @return 查询结果，对结果{@code QueryResponse}内各值获取方法见测试：{@link SolrjTransferUtil#toMap(QueryResponse)}
	 */
	public QueryResponse query(String[] kvs)
	{
		ModifiableSolrParams params = new ModifiableSolrParams();
		
		for (int i=0; i+1<kvs.length; i+=2) {
			params.set(kvs[i], kvs[i+1]);
		}
		
		return this.query(params);
	}

	public QueryResponse query(ModifiableSolrParams params)
	{
		try {
			QueryResponse response = lbsServer.query(params);
			return response;
		} catch (SolrServerException e) {
			logger.debug("出现异常，请检查：\n"
					+ "\t\t1. params参数设置是否正确\n"
					+ "\t\t2. 检查服务是否开启\n"
					+ "\t\t异常详情：" + e);
		}
		
		return new QueryResponse();
	}
	
	/**
	 * 以文档形式存储 
	 * @param doc
	 * @return
	 */
	public UpdateResponse save(SolrInputDocument doc)
	{
		UpdateResponse response = null;
		try {
			response = lbsServer.add(doc);
			lbsServer.commit();
		} catch (SolrServerException | IOException e) {
			logger.error(this.getClass(), e);
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 以实体形式存储
	 * @param doc
	 * @return
	 */
	public UpdateResponse save(Object bean)
	{
		UpdateResponse response = null;
		try {
			response = lbsServer.addBean(bean);
		} catch (SolrServerException | IOException e) {
			logger.error(this.getClass(), e);
		}
		return response;
	}
	
	/**
	 * @param docs
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public UpdateResponse save(List<SolrInputDocument> docs) throws SolrServerException, IOException
	{
		UpdateResponse response = null;
		response = lbsServer.add(docs);
		return response;
	}
	
	/**
	 * 根据id删除文档
	 * @param id
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public UpdateResponse deleteById(String id) throws SolrServerException, IOException
	{
		UpdateResponse response = null;
		response = lbsServer.deleteById(id);
		
		return response;
	}

	public UpdateResponse deleteAll(List<String> ids) throws SolrServerException, IOException
	{
		UpdateResponse response = null;
		response = lbsServer.deleteById(ids);
		
		return response;
	}
	
	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException
	{
		UpdateResponse response = lbsServer.deleteByQuery(query);
		
		return response;
	}
	
	
}
