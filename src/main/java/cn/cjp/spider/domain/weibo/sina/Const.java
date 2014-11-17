package cn.cjp.spider.domain.weibo.sina;

import java.util.HashMap;
import java.util.Map;

public class Const {

	@SuppressWarnings("serial")
	public static final Map<String, String> header = new HashMap<String, String>() {
		{
			put("Referer",
					"https://passport.sina.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
			put("User-Agent",
					"Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			put("Host", "passport.sina.cn");
		}
	};

	/**
	 * 用于登录 <br>
	 * Method : POST
	 */
	public static final String LOGIN_URL = "https://passport.sina.cn/sso/login";
	/**
	 * 用于发微博时，预上传图片
	 */
	public static final String ADD_PIC_URL = "http://m.weibo.cn/mblogDeal/addPic";

	public static final String LOGIN_SUCCESS = "success";
	public static final String LOGIN_FAIL = "fail";

	/**
	 * 登录用户的首页，显示关注的用户的微博 <br>
	 * Method : GET <br>
	 * 额外参数 <br>
	 * page : 可以不加，但不可以为空，最小为1 <br>
	 * next_cursor : 可以不加，但不可以为空
	 */
	public static final String HOME_WEIBO_URL = "http://m.weibo.cn/index/feed?format=cards&next_cursor={next_cursor}&page={page}";

	public static final String WEIBOs_OF_USER_URL = "http://m.weibo.cn/page/json?containerid=100505{uid}_-_WEIBO_SECOND_PROFILE_WEIBO&page={page}";
	
	public static final String PLs_OF_WEIBO_URL = "http://m.weibo.cn/{uid}/{mid}/getRCList?format=cards&type=pl&page={page}";
	
	public static final String ZFs_OF_WEIBO_URL = "http://m.weibo.cn/{uid}/{mid}/getRCList?format=cards&type=zf&page={page}";
	
	public static final String ATTITUDEs_OF_WEIBO_URL = "http://m.weibo.cn/attitudesDeal/getAttitudeList?id={mid}&format=cards&page={page}";
	
	
	
}
