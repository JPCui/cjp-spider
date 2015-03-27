package cn.cjp.spider.domain.tencent.pengyou;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.cjp.base.utils.FileUtil;

public class PengyouSpider {

	String url = "http://friend.pengyou.com/index.php?mod=friends&act=tab&u={hash}&adtag=from_profile_menu_1";
	String currUserHash = "c265e4bd629300c5e4d4e068ddf80f3cd270af60c7ae8ff0";

	Map<String, Boolean> userSpiderring = new HashMap<String, Boolean>();
	
	String dirStr = "D:\\__JAVA__\\pengyou";
	
	private void init(){
		// 初始化userSpiderring
		File fileDir = new File(dirStr);
		File[] files = fileDir.listFiles();
		for(File file : files){
			userSpiderring.put(file.getName(), true);
		}
		
	}

	public void beginSpider() throws IOException {
		init();
		
		List<String> hashs = new ArrayList<String>();
		hashs.add(currUserHash);

		while (true) {
			if (hashs.size() == 0)
				break;
			List<String> tempHashs = new ArrayList<String>();

			for (int i = 0; i < hashs.size(); i++) {
				currUserHash = hashs.get(i);
				if (userSpiderring.containsKey(currUserHash)) {
					System.out.println(currUserHash + "已存在");
					continue;
				}
				userSpiderring.put(currUserHash, true);
				String json = this.getJson(currUserHash);
				if (json == null)
					continue;

				List<PengyouUser> users = PengyouUser.fromJson(json);

				for (int j = 0; j < users.size(); j++) {
					PengyouUser user = users.get(j);
					if (!userSpiderring.containsKey(user.hash)) {
						tempHashs.add(user.hash);
					}

					try {
//						File file = new File("D:\\__JAVA__\\pengyou\\"
//								+ currUserHash + ".txt");
//						if (file.exists()) {
//							file.delete();
//						}
						FileUtil.write(user.firstletter + " " + user.hash + " "
								+ user.info + " " + user.logo + " "
								+ user.realname + " " + user.sex + "\r\n",
								"D:\\__JAVA__\\pengyou\\" + currUserHash
										+ ".txt", "UTF-8", true);
					} catch (Exception e) {
					}
				}
				System.out.println("\r\n" + i);
			}
			System.out.println("\r\n=============" + userSpiderring.size());
			hashs.clear();
			hashs.addAll(tempHashs);
		}

	}

	public String getJson(String userHash) throws IOException {
		Connection conn = Jsoup.connect(url.replace("{hash}", userHash))
				.timeout(30000);

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

		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			return null;
		}
		String html = doc.html();

		// 找到var friendList的坐标
		int friendListIndex = html.indexOf("var friendList");
		int groupListIndex = html.indexOf("var groupList");
		if(friendListIndex==-1 || groupListIndex==-1){
			System.out.println(html);
			return null;
		}
		html = html.substring(friendListIndex, groupListIndex);

		// 找到friends的json开始坐标
		int jsonBeginIndex = html.indexOf("{");
		// 找到friends的json结束坐标
		int jsonEndIndex = html.lastIndexOf("}") + 1;

		String json = html.substring(jsonBeginIndex, jsonEndIndex);
		return json;
	}

	public static void main(String[] args) throws IOException {
		new PengyouSpider().beginSpider();
	}

}
