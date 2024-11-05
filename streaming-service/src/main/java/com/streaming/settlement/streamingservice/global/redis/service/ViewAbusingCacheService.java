package com.streaming.settlement.streamingservice.global.redis.service;

import com.streaming.settlement.streamingservice.global.redis.dto.AbusingKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.streaming.settlement.streamingservice.global.redis.constant.RedisKeyConstants.ABUSE_KEY_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewAbusingCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration ABUSE_WINDOW = Duration.ofSeconds(30);

    public boolean isAbusing(AbusingKey key) {
        String abuseKey = generateAbuseKey(key.contentId(), key.userId(), key.ip());
        return key.userId().equals(key.creatorId()) || Boolean.TRUE.equals(redisTemplate.hasKey(abuseKey));
    }

    public void setAbusing(AbusingKey key) {
        String abuseKey = generateAbuseKey(key.contentId(), key.userId(), key.ip());
        redisTemplate.opsForValue().set(abuseKey, abuseKey, ABUSE_WINDOW);
    }

    public String generateAbuseKey(Long contentId, Long userId, String ipAddress) {
        return String.format("%s:contentId:%d:userId:%d:ip:%s", ABUSE_KEY_PREFIX, contentId, userId, ipAddress);
    }

}
