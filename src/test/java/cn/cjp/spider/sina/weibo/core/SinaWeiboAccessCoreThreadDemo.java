package cn.cjp.spider.sina.weibo.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.cjp.base.utils.FileUtil;
import cn.cjp.sina.weibo.analyzer.GetFansAnalyzer;
import cn.cjp.sina.weibo.core.SinaWeiboAccessCore;
import cn.cjp.sina.weibo.domain.UserDomain;

/**
 * 在线程中测试SinaWeiboAccessCore
 * @author REAL
 *
 */
@SuppressWarnings("deprecation")
public class SinaWeiboAccessCoreThreadDemo {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SinaWeiboAccessCoreThreadDemo.class);

	SinaWeiboAccessCore accessCore = null;
	Set<String> grabbedUidSet = new HashSet<String>();
	List<String> waitingUidList = new ArrayList<String>();
	Set<String> grabbingUidSet = new HashSet<String>();

	private int grabPageNum = 10;

	private static final String savedFileDir = "D:/_weibo_data_/";

	public void init() {
		String username = "1367471019@qq.com";
		String password = "15838228248";

		accessCore = SinaWeiboAccessCore.getInstance(username, password);
		// 初始化等待队列
		waitingUidList.add("5574133962");
	}

	/**
	 * 从待抓取Uid队列中取出一个Uid
	 * 
	 * @return
	 */
	private synchronized String borrowOne() {
		if (waitingUidList.size() == 0) {
			try {
				logger.error("wait " + waitingUidList.size());
				this.wait();
				logger.error("wake " + waitingUidList.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String borrowingUid = waitingUidList.remove(0);
		grabbingUidSet.add(borrowingUid);
		return borrowingUid;
	}

	public void run() {
		logger.error("Thread start");

		while (true) {
			toGrab();
			synchronized (System.out) {
				System.out.println("waiting : " + waitingUidList.size());
				System.out.println("grabbing : " + grabbingUidSet.size());
				System.out.println("grabbed : " + grabbedUidSet.size());
			}
		}

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
			for (UserDomain fan : fansList) {
				String uid = fan.getId() + "";
				if (!grabbedUidSet.contains(uid)
						&& !grabbingUidSet.contains(uid)) {
					addOne(uid);
				}
			}
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
		String data = "";
		for (UserDomain fan : fansList) {
			data += fan.toString() + "\r\n";
		}

		FileUtil.write(data, savedFileDir + grabbingUid + ".fans", true);
	}

	public static void main(String[] args) {
		final SinaWeiboAccessCoreThreadDemo threadDemo = new SinaWeiboAccessCoreThreadDemo();

		threadDemo.init();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					threadDemo.run();
				}
			}).start();
		}
	}

}
