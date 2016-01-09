package cn.cjp.sina.weibo.domain;

/**
 * 新浪微博兴趣点
 * @author REAL
 *
 */
public class PoiDomain {
	
	// 唯一标识poi
	public String poiid;
	
	// 标题
	public String title;
	
	// 准确位置
	public String address;
	
	// 经度
	public String lon;
	
	// 纬度
	public String lat;
	
	// 区号
	public String city;
	
	// 描述（xxx人签到）
	public String desc;
	
	// 地点的图片
	public String pic;
	
	public static PoiDomain fromJson(String json){
		PoiDomain poiDomain = new PoiDomain();
		
		return poiDomain;
	}
	
}
