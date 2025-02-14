package com.gnimtier.api.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithTTL(String key, String value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
    }

    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public boolean putIfAbsent(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate
                .opsForValue()
                .setIfAbsent(key, value));
    }

    // Hash Operations
    public void addToHash(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object getFromHash(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> getAllFromHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void deleteFromHash(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    // Set Operations
    public void addToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void removeFromSet(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    // Delete Operations
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // Update Operations
    public void updateValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void updateHashField(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }
}
