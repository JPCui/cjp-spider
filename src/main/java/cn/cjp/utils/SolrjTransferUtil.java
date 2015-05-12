package cn.cjp.utils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SolrjTransferUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SolrjTransferUtil.class);

	public static Map<String, Object> toMap(QueryResponse response)
	{
		logger.info("getResults=============================");
		//保存内容结果
		SolrDocumentList resList = response.getResults();
		if(resList != null)
		{
			for (int i = 0; i < resList.size(); i++) {
				logger.info(resList.get(i));
			}
		}

		logger.info("getFacetDates=============================");
		//保存facetDate结果
		Map<String, List<Map<String, Object>>> resFacetDateMap = new HashMap<String, List<Map<String, Object>>>();
		List<FacetField> facetDateList = response.getFacetDates();
		if(facetDateList != null)
		{
			for (FacetField facetField : facetDateList)
			{
				//遍历所有的facet.date字段，并把每个facet统计结果保存至数组
				List<Map<String, Object>> subMapsList = new ArrayList<Map<String,Object>>();
				
				logger.info(facetField.getName() + " : {");
				for (Count count : facetField.getValues())
				{
					logger.info("\t\t [{ 'key' : " + count.getName() + ", value : " + count.getCount() + " }]");

					//每个facet.date字段的统计结果都是一个map
					Map<String, Object> subMaps = new HashMap<String, Object>();
					subMaps.put("key",count.getName());
					subMaps.put("value", count.getCount());
					subMapsList.add(subMaps);
				}
				resFacetDateMap.put(facetField.getName(), subMapsList);
				
				logger.info("}");
			}
		}
		logger.info("getFacetFields=============================");
		//保存facet结果
		Map<String, List<Map<String, Object>>> resFacetMap = new HashMap<String, List<Map<String, Object>>>();
		List<FacetField> facetList = response.getFacetFields();
		if(facetDateList != null)
		{
			for (FacetField facetField : facetList)
			{
				//遍历所有的facet.date字段，并把每个facet统计结果保存至数组
				List<Map<String, Object>> subMapsList = new ArrayList<Map<String,Object>>();
				
				logger.info(facetField.getName() + " : {");
				for (Count count : facetField.getValues())
				{
					logger.info("\t\t [{ 'key' : " + count.getName() + ", value : " + count.getCount() + " }]");

					//每个facet.date字段的统计结果都是一个map
					Map<String, Object> subMaps = new HashMap<String, Object>();
					subMaps.put("key",count.getName());
					subMaps.put("value", count.getCount());
					subMapsList.add(subMaps);
				}
				resFacetMap.put(facetField.getName(), subMapsList);
				
				logger.info("}");
			}
		}
		logger.info("getHighlighting=============================");
		//保存highlight结果
		List<Map<String, Object>> highList = new ArrayList<Map<String,Object>>();
		Map<String, Map<String, List<String>>> responseHighLightList = response.getHighlighting();
		if(responseHighLightList != null)
		{
			for (String key : responseHighLightList.keySet())
			{
				Map<String, Object> map = new HashMap<String, Object>();
				logger.info("id : "+key);
				map.put("id", key);
				for (String key2 : responseHighLightList.get(key).keySet())
				{
					map.put(key2, responseHighLightList.get(key).get(key2));
					logger.info(key2+" : "+responseHighLightList.get(key).get(key2));
				}
				highList.add(map);
			}
		}
		logger.info("getGroupResponse=============================");
		//
		List<Map<String, Object>> groupList = new ArrayList<Map<String,Object>>();
		GroupResponse groupResponse = response.getGroupResponse();
		if(groupResponse != null)
		{
			List<GroupCommand> responseGroupList = groupResponse.getValues();
			for (GroupCommand groupCommand : responseGroupList) {
				logger.info(groupCommand.getName());
				Map<String, Object> map = new HashMap<String, Object>();
				for (Group group : groupCommand.getValues()) {
					map.put(groupCommand.getName(), group.getGroupValue());	//group.field
					map.put("docs", group.getResult());
//					SolrDocumentList groupDocs = group.getResult();
//					for (SolrDocument groupDoc : groupDocs) {
//						logger.info(groupDoc);
//					}
					logger.info(group.getGroupValue() + " : " + group.getResult());
				}
				groupList.add(map);
				
			}
		}
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("result", resList);
		resMap.put("facet", resFacetMap);
		resMap.put("facetDate", resFacetDateMap);
		resMap.put("highlight", highList);
		resMap.put("group", groupList);
		
		return resMap;
	}
	
}
