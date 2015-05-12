package cn.cjp.spider.common.redis.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import cn.cjp.spider.common.redis.RedisListOps;

public class RedisListOpsImpl implements RedisListOps {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#lpush(java.lang.String, java.lang.String)
	 */
	@Override
	public Long lpush(String key, String value) {
		return stringRedisTemplate.opsForList().leftPush(key, value);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#lpop(java.lang.String)
	 */
	@Override
	public String lpop(String key) {
		return stringRedisTemplate.opsForList().leftPop(key);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#rpush(java.lang.String, java.lang.String)
	 */
	@Override
	public Long rpush(String key, String value) {
		return stringRedisTemplate.opsForList().rightPush(key, value);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#rpop(java.lang.String)
	 */
	@Override
	public String rpop(String key) {
		return stringRedisTemplate.opsForList().rightPop(key);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#length(java.lang.String)
	 */
	@Override
	public Long length(String key) {
		return stringRedisTemplate.opsForList().size(key);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#range(java.lang.String, int, int)
	 */
	@Override
	public List<String> range(String key, int start, int end) {
		return stringRedisTemplate.opsForList().range(key, start, end);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#remove(java.lang.String, long, java.lang.String)
	 */
	@Override
	public void remove(String key, long i, String value) {
		stringRedisTemplate.opsForList().remove(key, i, value);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#index(java.lang.String, long)
	 */
	@Override
	public String index(String key, long index) {
		return stringRedisTemplate.opsForList().index(key, index);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#set(java.lang.String, long, java.lang.String)
	 */
	@Override
	public void set(String key, long index, String value) {
		stringRedisTemplate.opsForList().set(key, index, value);
	}

	/* (non-Javadoc)
	 * @see cn.cjp.spider.common.redis.impl.RedisListOps#trim(java.lang.String, long, int)
	 */
	@Override
	public void trim(String key, long start, int end) {
		stringRedisTemplate.opsForList().trim(key, start, end);
	}

	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

}
