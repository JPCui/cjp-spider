package cn.cjp.redis.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import cn.cjp.base.utils.JacksonUtil;
import cn.cjp.redis.dao.ThreadDao;
import cn.cjp.redis.domain.ThreadDomain;

/**
 * 
 * @version 1.0
 * @since 1.0
 */
@Repository("threadDao")
public class ThreadDaoImpl implements ThreadDao {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	public void save(final ThreadDomain thread) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(
						redisTemplate.getStringSerializer().serialize(
								"thread.id." + thread.getId()),
						redisTemplate.getStringSerializer().serialize(
								JacksonUtil.toJson(thread)));
				return null;
			}
		});
	}

	public long lpush(ThreadDomain thread) {
		return redisTemplate.opsForList().leftPush(
				ThreadDomain.class.getSimpleName(), JacksonUtil.toJson(thread));
	}

	public ThreadDomain lpop() {
		String json = redisTemplate.opsForList().leftPop(
				ThreadDomain.class.getSimpleName()).toString();
		return JacksonUtil.toObj(json, ThreadDomain.class);
	}

	/*
	 * 
	 * @see ThreadDao#read(java.lang.String)
	 */
	public ThreadDomain read(final String uid) {
		return redisTemplate.execute(new RedisCallback<ThreadDomain>() {
			public ThreadDomain doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] key = redisTemplate.getStringSerializer().serialize(
						"thread.uid." + uid);
				if (connection.exists(key)) {
					byte[] value = connection.get(key);
					String json = redisTemplate.getStringSerializer()
							.deserialize(value);
					ThreadDomain threadDomain = JacksonUtil.toObj(json,
							ThreadDomain.class);
					return threadDomain;
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ThreadDao#delete(java.lang.String)
	 */
	public void delete(final String uid) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) {
				connection.del(redisTemplate.getStringSerializer().serialize(
						"Thread.uid." + uid));
				return null;
			}
		});
	}
}
