package com.project.HotelManagementSystem.service.cache;

import com.project.HotelManagementSystem.dto.booking.HotelSearchResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelSearchCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public List<HotelSearchResultDTO> get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return (List<HotelSearchResultDTO>) value;
    }

    public void set(String key, List<HotelSearchResultDTO> data) {
        redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(10));
    }

}
