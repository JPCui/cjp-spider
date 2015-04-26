package cn.cjp.sina.weibo.analyzer;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import cn.cjp.sina.weibo.domain.StatusPubWeibo;

public class ResponseJsonAnalyzer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ResponseJsonAnalyzer.class);

	/**
	 * 解析发表微博返回的Json
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static StatusPubWeibo analyzePubWeibo(String json) {
		StatusPubWeibo statusPubWeibo = new StatusPubWeibo();

		try {
			JSONObject jsonObject = new JSONObject(json);
			statusPubWeibo.setOk(jsonObject.getInt("ok"));
			if (statusPubWeibo.getOk() == 1) {
				statusPubWeibo.setId(jsonObject.getString("id"));
			}
			statusPubWeibo.setMsg(jsonObject.getString("msg"));
		} catch (JSONException e) {
			logger.error("JSON转换失败 : " + json, e);

		}
		return statusPubWeibo;
	}

}
