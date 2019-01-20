package com.wjy.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author ybxxszl
 * @date 2018年10月10日
 * @description Redis工具类
 */
public class RedisUtil {

    /**
     * @param key     键
     * @param value   值
     * @param seconds 秒
     * @date 2018年10月10日
     * @author ybxxszl
     * @description TODO
     */
    public static void set(String key, String value, int seconds) {

        Jedis jedis = JedisPoolUtil.getInstance().getResource();

        jedis.set(key, value);

        jedis.expire(key, seconds);

        jedis.close();

    }

    /**
     * @param key   键
     * @param value 值
     * @date 2018年10月10日
     * @author ybxxszl
     * @description TODO
     */
    public static void set(String key, String value) {

        Jedis jedis = JedisPoolUtil.getInstance().getResource();

        jedis.set(key, value);

        jedis.close();

    }

    /**
     * @param key 键
     * @return String 值
     * @date 2018年10月10日
     * @author ybxxszl
     * @description TODO
     */
    public static String get(String key) {

        Jedis jedis = JedisPoolUtil.getInstance().getResource();

        String value = jedis.get(key);

        jedis.close();

        return value;

    }

    /**
     * @param key 键
     * @date 2018年10月19日
     * @author ybxxszl
     * @description TODO
     */
    public static void del(String key) {

        Jedis jedis = JedisPoolUtil.getInstance().getResource();

        jedis.del(key);

        jedis.close();

    }

}
