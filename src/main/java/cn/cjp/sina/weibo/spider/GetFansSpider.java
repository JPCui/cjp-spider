package cn.cjp.sina.weibo.spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.cjp.base.utils.FileUtil;
import cn.cjp.sina.weibo.analyzer.GetFansAnalyzer;
import cn.cjp.sina.weibo.core.SinaWeiboAccessCore;
import cn.cjp.sina.weibo.domain.UserDomain;

@SuppressWarnings("deprecation")
public class GetFansSpider implements Runnable {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(GetFansSpider.class);

	SinaWeiboAccessCore accessCore = null;
	private static Set<String> grabbedUidSet = new HashSet<String>();
	private static List<String> waitingUidList = new ArrayList<String>();
	private static Set<String> grabbingUidSet = new HashSet<String>();

	private int grabPageNum = 10;

	/**
	 * 是否存储到文件
	 */
	private static String savedFileDir = "";

	/**
	 * 设置账号
	 * 
	 * @param username
	 * @param password
	 */
	public void setAcount(String username, String password) {

		accessCore = SinaWeiboAccessCore.getInstance(username, password);
		// 初始化等待队列
		waitingUidList.add("5574133962");
	}

	/**
	 * 设置存储路径，不设置则不存
	 * 
	 * @param dir
	 */
	@SuppressWarnings("static-access")
	public void setSavedFileDir(String dir) {
		this.savedFileDir = dir;
	}

	public void run() {
		/**
		 * 验证是否登录成功
		 */
		if (accessCore == null) {
			logger.error("未登录或登录失败", new Throwable(
					"Please call setAcount for login"));
			return;
		}
		synchronized (logger) {
			logger.info("Thread start...\n");
		}

		while (true) {
			toGrab();
			synchronized (logger) {
				logger.info("======================");
				logger.info("等待队列 : " + waitingUidList.size());
				logger.info("正在抓取队列 : " + grabbingUidSet.size());
				logger.info("已抓取队列 : " + grabbedUidSet.size());
				logger.info("======================");
			}
		}
	}

	/**
	 * 从待抓取Uid队列中取出一个Uid
	 * 
	 * @return
	 */
	private synchronized String borrowOne() {
		// while 比 if 更好点
		while (waitingUidList.size() == 0) {
			try {
				logger.info("wait 等待队列大小：" + waitingUidList.size());
				this.wait();
				logger.info("awake 等待队列大小：" + waitingUidList.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String borrowingUid = waitingUidList.remove(0);
		grabbingUidSet.add(borrowingUid);
		return borrowingUid;
	}

	/**
	 * 开始抓取
	 */
	private void toGrab() {
		String grabbingUid = borrowOne();
		for (int i = 1; i <= grabPageNum; i++) {
			// 抓取Json
			String json = accessCore.getFansByUid(grabbingUid, i);
			// 解析Json
			List<UserDomain> fansList = GetFansAnalyzer.analyzerJson(json);
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
					addOne(uid);
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

	private synchronized void addOne(String uid) {
		waitingUidList.add(uid);
		this.notify();
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

		FileUtil.write(data, savedFileDir + grabbingUid + ".fans", true);
	}

}
