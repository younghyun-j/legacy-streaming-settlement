package com.streaming.settlement.streamingservice.global.redis.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.streaming.settlement.streamingservice.global.redis.constant.RedisKeyConstants.VIEW_COUNT_KEY_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void incrementViewCount(Long contentId) {
        // content:viewCount:time:202410271557
        String key = generateViewCountKey();
        // contentId가 없으면 자동으로 0으로 초기화한 후 1 증가, 있으면 기존 값에 1 증가
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.increment(key, String.valueOf(contentId), 1L);
    }

    /**
     * 현재 시간에서 초 단위를 절삭 후 Key를 생성합니다
     */
    @NotNull
    private static String generateViewCountKey() {
        return VIEW_COUNT_KEY_PREFIX + getTimeWindow(LocalDateTime.now());
    }

    @NotNull
    private static String getTimeWindow(@NotNull LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MINUTES).toString();
    }

}
