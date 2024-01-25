package org.tenten.tentenbe.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.tenten.tentenbe.global.common.constant.RedisConstant.CACHE_EXPIRE_TIME_MS;

@Component
@RequiredArgsConstructor
public class RedisCache {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String topic, String key, Object data) {
        redisTemplate.opsForValue().set(topic + ":" + key, data, CACHE_EXPIRE_TIME_MS, TimeUnit.MILLISECONDS);
    }

    public Object get(String topic, String key) {
        return redisTemplate.opsForValue().get(topic + ":" + key);
    }

    public Object get(String topic, String key, String visitDate) {
        return redisTemplate.opsForValue().get(topic + ":" + key + ":" + visitDate);
    }

    public void delete(String topic, String key) {
        redisTemplate.delete(topic + ":" + key);
    }

}