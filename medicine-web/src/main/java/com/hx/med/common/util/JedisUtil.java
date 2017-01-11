package com.hx.med.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.TimeUnit;

/**
 * Created by huaxiao on 2016/12/27.
 *
 * @author huaxiao
 * @since 1.6
 */
@Component
public final class JedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 构造函数.
     */
    private JedisUtil() {
    }

    /**
     * getResource.
     *
     * @return Jedis
     * @throws JedisException JedisException
     */
    public RedisTemplate getResource() throws JedisException {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
////            jedis.select(Integer.parseInt(DB_INDEX));
////            logger.debug("getResource.", jedis);
//        } catch (final JedisException e) {
//            if (jedis != null){
//                jedis.close();
//            }
//            throw e;
//        }
        return null;
    }

    /**
     * set.
     *
     * @param key     key
     * @param capText capText
     */
    public void set(final String key, final String capText) {
        final ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, capText);
    }

    /**
     * expire.
     *
     * @param key key
     * @param i   i
     */
    public void expire(final String key, final int i) {
        redisTemplate.expire(key, i, TimeUnit.SECONDS);
    }

    /**
     * get.
     *
     * @param key key
     * @return String
     */
    public String get(final String key) {
        final ValueOperations<String, String> value = redisTemplate.opsForValue();
        return value.get(key);
    }

    /**
     * incr.
     *
     * @param roomNum roomNum
     * @return Long
     */
    public Long incr(final String roomNum) {
        final ValueOperations<String, String> value = redisTemplate.opsForValue();
        return value.increment(roomNum, 1);
    }
}
