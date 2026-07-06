package com.project.HotelManagementSystem.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisKvService {
    private final RedisTemplate<String, Object> redis;

    public void put(String key, Object value, Duration ttl) {
        redis.opsForValue().set(key, value, ttl);
    }

    public <T> T get(String key, Class<T> type) {
        Object val = redis.opsForValue().get(key);
        return val == null ? null : type.cast(val);
    }

    public Boolean delete(String key) {
        return redis.delete(key);
    }
}
