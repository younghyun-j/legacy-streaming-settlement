package com.streaming.settlement.streamingservice.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.streaming.settlement.streamingservice.global.redis.constant.RedisKeyConstants.DAILY_VIEWED_CONTENT_KEY;


@Slf4j
@Service
@RequiredArgsConstructor
public class DailyViewedContentCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setContentId(Long contentId) {
        String key = generateDailyViewedContentKey();
        redisTemplate.opsForZSet().add(key, String.valueOf(contentId), contentId);
    }

    private static String generateDailyViewedContentKey() {
        return DAILY_VIEWED_CONTENT_KEY + LocalDate.now();
    }

}
