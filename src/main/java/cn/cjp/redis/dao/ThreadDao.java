package cn.cjp.redis.dao;

import cn.cjp.redis.domain.ThreadDomain;

/**
 * 
 * @version 1.0
 * @since 1.0
 */
public interface ThreadDao {
	/**
	 * @param user
	 */
	void save(ThreadDomain thread);

	/**
	 * @param threadId
	 * @return
	 */
	ThreadDomain read(String threadId);

	/**
	 * @param threadId
	 */
	void delete(String threadId);
	
	/**
	 * 
	 * @param thread
	 * @return 队列大小
	 */
	public long lpush(ThreadDomain thread);
	
	public ThreadDomain lpop();
}
