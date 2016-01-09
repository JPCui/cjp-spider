package cn.cjp.alibaba.tmall;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.cjp.utils.EncodingUtil;

/**
 * 测试天猫数据的抓取
 * @author Administrator
 *
 */
public class TMallSpider {

	public static void login() throws IOException {
		// 读取登陆POST参数
		Map<String, String> props = TMallUtils.getLoginPostParams();

		// 初始化连接
		Connection conn = Jsoup
				.connect("https://login.taobao.com/member/login.jhtml")
				.referrer("https://login.taobao.com/member/login.jhtml?tpl_redirect_url=http%3A%2F%2Fwww.tmall.com%2F&style=miniall&enup=true&full_redirect=true&from=tmall&allp=assets_css%3D2.0.0/login_pc.css%26enup_css%3D2.0.0/enup_pc.css%26assets_js%3D2.3.8/login_performance.js&pms=1435572814")
				.data(props)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
						.header("content-type",
								"application/x-www-form-urlencoded; charset=UTF-8")
				.header("Host", "login.taobao.com")
				.header("Content-Length", "2488")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.followRedirects(true).ignoreContentType(true);

		// 添加参数
		Document doc = conn.post();

		System.out.println(doc.html());
	}

	public static void runLoginCookies() throws IOException {
		Connection conn = Jsoup
				.connect("http://buy.tmall.com/login/buy.do?from=itemDetail&var=login_indicator&id=undefined&shop_id=undefined&cart_ids=&t=1435562926147");

		Map<String, String> cookies = TMallUtils.getLoginCookies();
		conn.cookies(cookies);

		String str = conn.get().text();
		System.out.println(str);
	}

	public static void main(String[] args) throws Exception {
		 runLoginCookies();

//		login();
		
//		testLocalLogin();

		// crawlItemDetail();
	}

	// http://mdskip.taobao.com/core/initItemDetail.htm?isAreaSell=true&cartEnable=true&queryMemberRight=true&itemId=41213879012&service3C=false&isForbidBuyItem=false&sellerPreview=false&addressLevel=3&sellerUserTag=34672672&household=false&isSecKill=false&tryBeforeBuy=false&isUseInventoryCenter=true&sellerUserTag3=144185556820066336&sellerUserTag4=137438953859&tgTag=false&isIFC=false&progressiveSupport=false&isRegionLevel=true&itemTags=258,513,651,843,1163,1478,1483,1547,1611,2049,2059,2178,2443,2507,2562,2635,3974,4166,4491,6146,6603,8833,18049,19457,19585,20289,20545,25281,25985,27073,28802,48706,65026&showShopProm=false&isApparel=false&sellerUserTag2=18015635460065288&tmallBuySupport=true&notAllowOriginPrice=false&offlineShop=false&callback=onMdskip&areaId=410100&cat_id=2&ref=http%253A%252F%252Flist.tmall.com%252Fsearch_product.htm%253Fq%253D%2525C4%2525EA%2525D6%2525D0%2525B4%2525F3%2525B4%2525D9%2526click_id%253D%2525C4%2525EA%2525D6%2525D0%2525B4%2525F3%2525B4%2525D9%2526from%253Dmallfp..pc_1.2_hq%2526spm%253D3.7396704.a1z5h.3.6HnfmL&brandSiteId=0

	public static void crawlItemDetail() throws IOException,
			InterruptedException {
		String url = "http://mdskip.taobao.com/core/initItemDetail.htm?progressiveSupport=false&tryBeforeBuy=false&queryMemberRight=true&itemTags=258,513,651,843,1163,1478,1483,1547,1611,2049,2059,2178,2443,2507,2562,2635,3974,4166,4491,6146,6603,8833,18049,19457,19585,20289,20545,25281,25985,27073,28802,48706,53954,65026&household=false&addressLevel=3&sellerPreview=false&tgTag=false&offlineShop=false&isAreaSell=true&isSecKill=false&isUseInventoryCenter=true&sellerUserTag2=18015635460065288&cartEnable=true&tmallBuySupport=true&notAllowOriginPrice=false&sellerUserTag=34672672&isIFC=false&showShopProm=false&isApparel=false&itemId=37108315319&isRegionLevel=true&isForbidBuyItem=false&service3C=false&sellerUserTag3=144185556820066336&sellerUserTag4=137438953859&callback=onMdskip&ref=http%253A%252F%252Fchaoshi.detail.tmall.com%252Fitem.htm%253Fspm%253Da220m.1000858.1000725.1.p1zE4z%2526areaId%253D410100%2526cat_id%253D2%2526rn%253Df07e506f194db6b69638b8e07e5fe989%2526user_id%253D1955345225%2526is_b%253D1%2526userBucket%253D2%2526id%253D41213879012&brandSiteId=0";

		int i = 200;
		while (i-- != 0) {

			Connection conn = Jsoup.connect(url)
					// .referrer("http://chaoshi.detail.tmall.com/item.htm?spm=1.7406545.1998025129.1.22NcBC&abbucket=_AB-M224_B14&rn=f07e506f194db6b69638b8e07e5fe989&acm=03227.1003.1.63887&aldid=sI2heX53&uuid=null&abtest=_AB-LR224-PR224&scm=1003.1.03227.ITEM_37108315319_63887&pos=1&userBucket=2&id=37108315319")
					.referrer("http://detail.tmall.com")
					// 不加referrer会抓取不到数据
					.header("Origin", "http://chaoshi.detail.tmall.com")
					.ignoreContentType(true);

			String str = conn.get().text();

			System.out.println(str);
		}
	}
	
	public static void testLocalLogin() throws IOException{
		Connection conn = Jsoup.connect("https://sso.tomcat.com:8443/");
		conn.ignoreContentType(true);
		conn.followRedirects(true);
		
		Response resp = conn.execute();
		
		String str = resp.body();
		System.out.println(str);
		
		System.out.println(new String(str.getBytes("UTF-8")));
		
		System.out.println(EncodingUtil.judgeCoding(str));
		
	}
}
