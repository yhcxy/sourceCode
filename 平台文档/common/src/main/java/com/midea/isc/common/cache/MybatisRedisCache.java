package com.midea.isc.common.cache;

import com.midea.isc.common.component.DynamicDataSourceAspect;
import com.midea.isc.common.sys.ApplicationContextHolder;
import com.midea.isc.common.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache {

    private static final Logger log = LoggerFactory.getLogger(MybatisRedisCache.class);

    /**
     * 读写锁
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    /**
     * ID
     */
    private String id;

    /**
     * 集成redisTemplate
     */
    protected static RedisTemplate<String, Object> redisTemplate;

    public MybatisRedisCache() {
    }

    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        } else {
            log.debug("MybatisRedisCache.id={}", id);
            this.id = id;
        }
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        try {
            Long size =   getRedisTemplate().opsForHash().size(this.id.toString());
            log.debug("MybatisRedisCache.getSize: {}->{}", id, size);
            return size.intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public void putObject(final Object key, final Object value) {
        try {
            log.debug("MybatisRedisCache.putObject: {}->{}->{}", id, key, JacksonUtils.obj2json(value));
            getRedisTemplate().opsForHash().put(this.id.toString(), key.toString(), value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Object getObject(final Object key) {
        try {
            Object hashVal = getRedisTemplate().opsForHash().get(this.id.toString(), key.toString());
            log.debug("MybatisRedisCache.getObject: {}->{}->{}", id, key, JacksonUtils.obj2json(hashVal));
            return hashVal;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Object removeObject(final Object key) {
        try {
            getRedisTemplate().opsForHash().delete(this.id.toString(), key.toString());
            log.debug("MybatisRedisCache.removeObject: {}->{}->{}", id, key);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() {
        try {
            redisTemplate.delete(this.id.toString());
            log.debug("MybatisRedisCache.clear: {}", id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    @Override
    public String toString() {
        return "MybatisRedisCache {" + this.id + "}";
    }

    /**
     * 设置redisTemplate
     *
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        MybatisRedisCache.redisTemplate = redisTemplate;
    }

    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        return redisTemplate;
    }
}