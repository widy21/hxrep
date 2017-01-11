package com.hx.med.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Date;

@Component("redisClusterCache")
public class RedisClusterCache {
    private static final int SECONDS = 1000;

    private final JedisCluster jedisCluster;
    private RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();

    /**
     * 缓存对象.
     * @param jedisCluster 缓存对象
     */
    @Autowired
    public RedisClusterCache(final JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    /**
     * redis set方法.
     * @param key key
     * @param obj 对象
     * @param time 时间
     */
    public void put(final String key, final Object obj, final long time) {
        jedisCluster.setex(rawKey(key), (int) (time / SECONDS), rawValue(obj));
    }

    /**
     * redis setex.
     * @param key key
     * @param value 对象
     * @param date 时间
     */
    public void put(final String key, final Object value, final Date date) {
        jedisCluster.setex(rawKey(key), (int) (date.getTime() / SECONDS), rawValue(value));
    }

    /**
     * redis setex.
     * @param key key
     * @param value 对象
     * @param date 时间
     */
    public void putStr(final String key, final String value, final Date date) {
        jedisCluster.setex(rawKey(key), (int) (date.getTime() / SECONDS), rawKey(value));
    }

    /**
     * redis set.
     * @param key key
     * @param value obj
     */
    public void put(final String key, final Object value) {
        jedisCluster.set(rawKey(key), rawValue(value));
    }

    /**
     * redis  set.
     * @param key key
     * @param value 字符串
     */
    public void put(final String key, final String value) {
        jedisCluster.set(rawKey(key), rawKey(value));
    }

    /**
     * redis get .
     * @param key key
     * @return 获取对象
     */
    public Object get(final String key) {
        return deserialize(jedisCluster.get(rawKey(key)));
    }

    /**
     * redis get.
     * @param key key
     * @return 获取字符串
     */
    public Object getString(final String key) {
        return deserializeString(jedisCluster.get(rawKey(key)));
    }

    /**
     * redis del .
     * @param key key
     */
    public void remove(final String key) {
        jedisCluster.del(rawKey(key));
    }

    /**
     * redis zadd .
     * @param listName 队列名
     * @param score 队列值
     * @param obj 对象
     */
    public void inQueue(final String listName, final long score,
                        final Object obj) {
        jedisCluster.zadd(rawKey(listName), (double) score, rawValue(obj));
    }

    /**
     * redis sadd .
     * @param listName 队列名
     * @param username 队列值
     * @return 是否成功，失败-1
     */
    public long addSet(final String listName, final String username) {
        return jedisCluster.sadd(rawKey(listName), rawKey(username));
    }

    /**
     * redis srem .
     * @param listName 队列名
     * @param username 队列值
     * @return 是否成功，失败-1
     */
    public long remSet(final String listName, final String username) {
        return jedisCluster.srem(rawKey(listName), rawKey(username));
    }

    /**
     * redis incr .
     * @param key key
     * @return incr之后结果
     */
    public Long incr(final String key) {
        return jedisCluster.incr(rawKey(key));
    }

    /**
     * redis zrangebyscore .
     * @param listName 队列名
     * @param score 队列值
     * @param score2 队列值
     * @return 获取从score到score2的队列值
     */
    public Object getFromQueue(final String listName, final long score,
                               final long score2) {
        return SerializationUtils.deserialize(jedisCluster.zrangeByScore(rawKey(listName), score, score2), defaultSerializer);
    }

    /**
     * redis zremrangebyscore .
     * @param listName 队列名
     * @param score score
     * @param score2 score2
     */
    public void deleteFromQueue(final String listName, final long score,
                                final long score2) {
        jedisCluster.zremrangeByScore(rawKey(listName), score, score2);
    }

    /**
     * 内部序列化key .
     * @param key 目标字符串
     * @return 序列化之后bytes
     */
    private byte[] rawKey(final String key) {
        return stringSerializer.serialize(key);
    }

    /**
     * 序列化obj.
     * @param obj 目标对象
     * @return 序列化之后bytes
     */
    private byte[] rawValue(final Object obj) {
        return defaultSerializer.serialize(obj);
    }

    /**
     * 反序列化对象 .
     * @param str bytes
     * @return 对象
     */
    private Object deserialize(final byte[] str) {
        if (str == null) {
            return null;
        }
        return defaultSerializer.deserialize(str);
    }

    /**
     * 反序列化字符串 .
     * @param str bytes
     * @return 返回字符串
     */
    private Object deserializeString(final byte[] str) {
        if (str == null) {
            return null;
        }
        return stringSerializer.deserialize(str);
    }

    /**
     * redis ttl .
     * @param key 获得key剩余时间
     * @return 永久或者不存在为-1
     */
    public long ttl(final String key) {
        return jedisCluster.ttl(rawKey(key));
    }

}
