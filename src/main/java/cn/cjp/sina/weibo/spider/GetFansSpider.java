package cn.cjp.sina.weibo.spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.cjp.base.utils.FileUtil;
import cn.cjp.sina.weibo.domain.UserDomain;
import cn.cjp.sina.weibo.http.core.SinaWeiboHttpClientAccessCore;

/**
 * 应该把登录账号也作为一个登录账号池
 * 
 * @author REAL
 * 
 */
public class GetFansSpider implements Runnable {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GetFansSpider.class);

	SinaWeiboHttpClientAccessCore accessCore = null;

	/**
	 * 已抓取的Uid队列
	 */
	private static Set<String> grabbedUidSet = new HashSet<String>();
	/**
	 * 等待抓取的Uid队列
	 */
	private static List<String> waitingUidList = new ArrayList<String>();
	/**
	 * 正在抓取的Uid队列
	 */
	private static Set<String> grabbingUidSet = new HashSet<String>();

	private int grabPageNum = 10;

	/**
	 * 是否存储到文件
	 */
	private static String savedFileDir = "";

	/**
	 * Instantiation DefaultSpider <br>
	 * 
	 * @param accounts
	 *            for logging on to sina weibo
	 */
	public GetFansSpider(Map<String, String> accounts) {
		accessCore = SinaWeiboHttpClientAccessCore.getInstance(accounts);
	}

	/**
	 * Instantiation DefaultSpider <br>
	 * 
	 * @param username
	 *            for logging on to sina weibo
	 * @param password
	 *            for logging on to sina weibo
	 * @param saveDir
	 *            设置存储路径，不设置则不存
	 */
	public GetFansSpider(Map<String, String> accounts, String saveDir) {

		savedFileDir = saveDir;
		new GetFansSpider(accounts);
	}

	/**
	 * @param saveDir
	 *            设置存储路径，不设置则不存
	 */
	public void setSavedFileDir(String saveDir) {
		savedFileDir = saveDir;
	}

	/**
	 * 初始化待抓取队列
	 * 
	 * @param uid
	 *            （UID for test：5574133962）
	 */
	public void initWaitingUidList(String uid) {
		waitingUidList.add(uid);
	}

	/**
	 * 初始化待抓取队列
	 * 
	 * @param uids
	 *            （UID for test：5574133962）
	 */
	public void initWaitingUidList(List<String> uids) {
		waitingUidList.addAll(uids);
	}

	public void run() {
		/**
		 * 验证是否登录成功
		 */
		if (accessCore == null) {
			logger.error(
					"SinaWeiboHttpClientAccessCore为空",
					new Throwable(
							"SinaWeiboHttpClientAccessCore is null, please call check account for login"));
			return;
		}
		logger.info("Thread start...\n");

		while (true) {
			toGrab();
			synchronized (logger) {
				logger.error("======================");
				logger.error("等待队列 : " + waitingUidList.size());
				logger.error("正在抓取队列 : " + grabbingUidSet.size());
				logger.error("已抓取队列 : " + grabbedUidSet.size());
				logger.error("======================");
			}
		}
	}
	
	/**
	 * 从待抓取Uid队列中取出一个Uid
	 * 
	 * @return
	 */
	private String borrowUidFromList() {
		String borrowingUid = null;
		synchronized (waitingUidList) {
			// while 比 if 更好点
			while (waitingUidList.size() == 0) {
				try {
					waitingUidList.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			borrowingUid = waitingUidList.remove(0);
			grabbingUidSet.add(borrowingUid);
		}
		return borrowingUid;
	}

	/**
	 * 开始抓取
	 */
	private void toGrab() {
		String grabbingUid = borrowUidFromList();
		for (int i = 1; i <= grabPageNum; i++) {
			// 抓取,解析Json
			logger.error("抓取-" + grabbingUid + "," + i);
			List<UserDomain> fansList = accessCore.requestFansByUid(
					grabbingUid, i);
			logger.error("抓取完成-" + grabbingUid + "," + i);
			if (fansList.size() == 0) {
				break;
			}
			// 新增Uid存入 待抓取 队列
			int addingCount = 0;
			for (UserDomain fan : fansList) {
				String uid = fan.getId() + "";
				if (!grabbedUidSet.contains(uid)
						&& !grabbingUidSet.contains(uid)) {
					addingCount++;
					addUidToList(uid);
				}
			}
			logger.info("新增Uid个数 : " + addingCount);
			// 保存数据
			saveGrabbedFans(grabbingUid, fansList);
			// 从 正在抓取队列 删除当前Uid，并存入已抓取队列
			grabbingUidSet.remove(grabbingUid);
			grabbedUidSet.add(grabbingUid);

		}
	}

	private void addUidToList(String uid) {
		synchronized (waitingUidList) {
			waitingUidList.add(uid);
			waitingUidList.notify();
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param grabbingUid
	 *            正在抓取的Uid
	 * @param fansList
	 *            抓取到的fans列表
	 */
	private void saveGrabbedFans(String grabbingUid, List<UserDomain> fansList) {

		if (savedFileDir == null || savedFileDir == "") {
			return;
		}
		String data = "";
		for (UserDomain fan : fansList) {
			data += fan.toString() + "\r\n";
		}

		FileUtil.write(data, savedFileDir + "/" + grabbingUid + ".fans", true);
	}

}
