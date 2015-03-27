package cn.cjp.spider.demo;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import cn.cjp.base.utils.FileUtil;
import cn.cjp.spider.domain.tencent.pengyou.PengyouUser;

public class PengyouDemo {

	String url = "http://friend.pengyou.com/index.php?mod=friends&act=tab&u=c265e4bd629300c517748ef20b81a23eb19c77beb09e2b9c&adtag=from_profile_menu_1";

	@Test
	public void test() throws IOException, JSONException {
		Connection conn = Jsoup.connect(url).timeout(30000);

		conn.cookie("bai_ck", "2ad2a46286458cb889162897b4e94d94_1427334787");
		conn.cookie("pst", "1427270878");
		conn.cookie("pt2gguin", "o1367471019");
		conn.cookie("ptcz",
				"23a52e73a1dbac345ddf68ef2793e979f8c4a611b6f7ee0747cfb0d75b0d51d7");
		conn.cookie("ptisp", "ctc");
		conn.cookie("ptui_loginuin", "1367471019");
		conn.cookie("pvid", "2560682208");
		conn.cookie("skey", "@dy6K6y19j");
		conn.cookie("uin", "o1367471019");

		Document doc = conn.get();

		String html = doc.html();
		System.out.println(html);

		// 找到var friendList的坐标
		int friendListIndex = html.indexOf("var friendList");
		int groupListIndex = html.indexOf("var groupList");
		html = html.substring(friendListIndex, groupListIndex);

		// 找到friends的json开始坐标
		int jsonBeginIndex = html.indexOf("{");
		// 找到friends的json结束坐标
		int jsonEndIndex = html.lastIndexOf("}") + 1;

		String json = html.substring(jsonBeginIndex, jsonEndIndex);

		System.out.println(PengyouUser.fromJson(json));

		FileUtil.write("\r\n总数：", "D:\\__JAVA__\\pengyou.txt", "UTF-8",
				true);

	}

}
