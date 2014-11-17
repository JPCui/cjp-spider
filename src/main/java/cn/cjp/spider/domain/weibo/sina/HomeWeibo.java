package cn.cjp.spider.domain.weibo.sina;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��ҳ΢��
 * 
 * @author REAL
 */
public class HomeWeibo {

	int maxPage = 0;

	long previous_cursor = 0L;

	long next_cursor = 0L;

	List<Weibo> weibos = new ArrayList<Weibo>();

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public long getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(long previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public long getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(long next_cursor) {
		this.next_cursor = next_cursor;
	}

	public List<Weibo> getWeibos() {
		return weibos;
	}

	public void setWeibos(List<Weibo> weibos) {
		this.weibos = weibos;
	}

	public static HomeWeibo fromJson(String json) throws JSONException {
		HomeWeibo homeWeibo = new HomeWeibo();
		List<Weibo> weibos = new ArrayList<Weibo>();

		JSONObject jo = new JSONObject(json);
		homeWeibo.setMaxPage(jo.getInt("maxPage"));
		homeWeibo.setNext_cursor(jo.getLong("next_cursor"));
		homeWeibo.setPrevious_cursor(jo.getLong("previous_cursor"));

		JSONArray jsonArray = jo.getJSONArray("cards").getJSONObject(0)
				.getJSONArray("card_group");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);

			Weibo weibo = new Weibo();
			JSONObject weiboJsonObj = obj.getJSONObject("mblog");
			weibo.setMid(weiboJsonObj.getLong("mid"));
			weibo.setSource(weiboJsonObj.getString("source"));
			weibo.setCreateTime(weiboJsonObj.getString("created_at"));
			weibo.setText(weiboJsonObj.getString("text"));
			weibo.setReportsNum(weiboJsonObj.getInt("reposts_count"));
			weibo.setCommentsNum(weiboJsonObj.getInt("comments_count"));
			weibo.setAttitudesNum(weiboJsonObj.getInt("attitudes_count"));

			JSONObject userJsonObj = weiboJsonObj.getJSONObject("user");
			User user = new User();
			user.setHeadUrl(userJsonObj.getString("profile_image_url"));
			user.setScreenName(userJsonObj.getString("screen_name"));
			weibo.setUser(user);

			weibos.add(weibo);
		}

		homeWeibo.setWeibos(weibos);

		return homeWeibo;
	}

	public String toHtml() {
		if (null == this.weibos)
			return "";
		String cardDom = "<div class=\"card card9 line-around\">";
		String headerDom = "<header class=\"layout-box media-graphic\">";
		String sectionDom = "<section class=\"weibo-detail\">";
		String footerDom = "<footer class=\"more-detail line-top layout-box box-center-v\">";

		String newHtml = "";
		for (int i = 0; i < this.weibos.size(); i++) {
			Weibo weibo = weibos.get(i);

			newHtml += cardDom;
			newHtml += "<span class=\"midArea\" style=\"display:none;\">"+weibo.getMid()+"</span>";
			// ========== Header Begin
			newHtml += headerDom;
			newHtml += "<a href=\"" + weibo.getUser().getUid()
					+ "\" class=\"mod-media size-xs\">";
			newHtml += "	<div class=\"media-main\">";
			newHtml += "		<img width=\"34\" height=\"34\" src=\""
					+ weibo.getUser().getHeadUrl() + "\">";
			newHtml += "	</div>";
			newHtml += "</a>";
			newHtml += "<div class=\"box-col item-list\">";
			newHtml += "	<a href=\"" + weibo.getUser().getUid()
					+ "\" class=\"item-main txt-l mct-a txt-cut\"> <span>"
					+ weibo.getUser().getScreenName() + "</span>";
			newHtml += "		<i class=\"icon icon-vip\"></i>";
			newHtml += "	</a>";
			newHtml += "	<div class=\"item-minor txt-xxs mct-d txt-cut\">";
			newHtml += "		<span class=\"time\">" + weibo.getCreateTime()
					+ "</span> <span class=\"from\">" + weibo.getSource()
					+ "</span>";
			newHtml += "	</div>";
			newHtml += "</div>";
			newHtml += "<a class=\"operate-box\" data-act-type=\"hover\"> <i class=\"icon-font icon-font-arrow-down txt-s\"></i>";
			newHtml += "</a>";
			newHtml += "</header>";
			// ========== Header End
			// ========== Section Begin
			newHtml += sectionDom;
			newHtml += "<p class=\"default-content txt-xl\">" + weibo.getText()
					+ "</p>";
			// 微博图片区域
			if (weibo.getPic_ids().size() == 1) {
				newHtml += "<div class=\"media-pic\">";
				newHtml += "<img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/"
						+ weibo.getPic_ids().get(0)
						+ ".jpg\" data-node=\"pic\" data-act-type=\"hover\">";
				newHtml += "</div>";
			} else if (weibo.getPic_ids().size() > 1) {
				newHtml += "<div class=\"media-pic-list\">";
				newHtml += "<ul>";
				for (String pic_id : weibo.getPic_ids())
					newHtml += "<li><img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/"
							+ pic_id
							+ ".jpg\" data-node=\"pic\" data-act-type=\"hover\"></li>";
				newHtml += "</ul>";
				newHtml += "</div>";
			}
			// 源微博区域
			if (null != weibo.getSrcWeibo()) {
				newHtml += "<div class=\"extend-content\">";
				newHtml += "<div class=\"inner\">";
				newHtml += "<p class=\"weibo-original txt-l\">";
				newHtml += "<a href=\"/u/"
						+ weibo.getSrcWeibo().getUser().getUid()
						+ "\" class=\"\">@"+weibo.getSrcWeibo().getUser().getScreenName()+"</a>："
						+ weibo.getSrcWeibo().getText();
				newHtml += "</p>";
				if (weibo.getSrcWeibo().getPic_ids().size() == 1) {
					newHtml += "<div class=\"media-pic\">";
					newHtml += "<img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/"
							+ weibo.getSrcWeibo().getPic_ids().get(0)
							+ ".jpg\" data-node=\"pic\" data-act-type=\"hover\">";
					newHtml += "</div>";
				} else if (weibo.getSrcWeibo().getPic_ids().size() > 1) {
					newHtml += "<div class=\"media-pic-list\">";
					newHtml += "<ul>";
					for (String pic_id : weibo.getSrcWeibo().getPic_ids())
						newHtml += "<li><img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/"
								+ pic_id
								+ ".jpg\" data-node=\"pic\" data-act-type=\"hover\"></li>";
					newHtml += "</ul>";
					newHtml += "</div>";
				}
				newHtml += "</div>";
				newHtml += "</div>";
			}
			newHtml += "</section>";
			// ========== Section End
			// ========== Footer Begin
			newHtml += footerDom;
			newHtml += "<a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"forward\"><i class=\"icon-font icon-font-forward\"></i><em class=\"num mct-d\">转发("+weibo.getReportsNum()+")</em></a>";
			newHtml += "<i class=\"line-gradient\"></i> <a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"comment\"><i class=\"icon-font icon-font-comment\"></i><em class=\"num mct-d\">评论("+weibo.getCommentsNum()+")</em></a>";
			newHtml += "<i class=\"line-gradient\"></i> <a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"like\"><i class=\"icon icon-likesmall\"></i><em class=\"num mct-d\">赞("+weibo.getAttitudesNum()+")</em></a>";
			newHtml += "</footer>";
			// ========== Footer End
			newHtml += "</div>";
		}
		return newHtml;
	}

}
