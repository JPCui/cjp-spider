package cn.cjp.spider.common.redis;

import java.util.List;

public interface RedisListOps {

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public abstract Long lpush(String key, String value);

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public abstract String lpop(String key);

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public abstract Long rpush(String key, String value);

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public abstract String rpop(String key);

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	public abstract Long length(String key);

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public abstract List<String> range(String key, int start, int end);

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public abstract void remove(String key, long i, String value);

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public abstract String index(String key, long index);

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public abstract void set(String key, long index, String value);

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public abstract void trim(String key, long start, int end);

}