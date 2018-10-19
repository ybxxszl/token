package com.wjy.jedis;

import redis.clients.jedis.Jedis;

/**
 * @date 2018年10月10日
 * @author ybxxszl
 * @description Redis工具类
 */
public class RedisUtil {

	/**
	 * @date 2018年10月10日
	 * @author ybxxszl
	 * @description TODO
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param seconds
	 *            秒
	 */
	public static void set(String key, String value, int seconds) {

		Jedis jedis = JedisPoolUtil.getInstance().getResource();

		jedis.set(key, value);

		jedis.expire(key, seconds);

		jedis.close();

	}

	/**
	 * @date 2018年10月10日
	 * @author ybxxszl
	 * @description TODO
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void set(String key, String value) {

		Jedis jedis = JedisPoolUtil.getInstance().getResource();

		jedis.set(key, value);

		jedis.close();

	}

	/**
	 * @date 2018年10月10日
	 * @author ybxxszl
	 * @description TODO
	 * @param key
	 *            键
	 * @return String 值
	 */
	public static String get(String key) {

		Jedis jedis = JedisPoolUtil.getInstance().getResource();

		String value = jedis.get(key);

		jedis.close();

		return value;

	}

	/**
	 * @date 2018年10月19日
	 * @author ybxxszl
	 * @description TODO
	 * @param key
	 *            键
	 */
	public static void del(String key) {

		Jedis jedis = JedisPoolUtil.getInstance().getResource();

		jedis.del(key);

		jedis.close();

	}

}
