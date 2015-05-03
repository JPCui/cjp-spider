package cn.cjp.spider.sina.weibo.spider;

import java.util.HashMap;
import java.util.Map;

public class GetFansThreadTest {

	public static void main(String[] args) {
		Map<String, String> accounts = new HashMap<String, String>();
		accounts.put("1367471019@qq.com", "15838228248");
		accounts.put("15838228248", "CJP15838228248");

		String saveDir = "D:/_weibo_data_/";
		String waitingUid = "1092617025";
		
		GetFansThread getFansThread = new GetFansThread(accounts, saveDir, waitingUid);
		
		getFansThread.start();
	}
	
}
