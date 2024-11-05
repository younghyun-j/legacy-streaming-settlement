package com.streaming.settlement.scheduler.global.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String VIEW_COUNT_KEY_PREFIX = "content:viewCount:time:";
    private static final int BATCH_SIZE = 500;

    /**
     * Redis에서 이전 1분간의 영상별 조회수를 조회합니다.
     * https://redis.io/docs/latest/commands/scan/
     */
    public Map<Long, Long> fetchPreviousMinuteViewCounts(String timeWindowKey) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        ScanOptions options = ScanOptions.scanOptions()
                .match("*")
                .count(BATCH_SIZE)
                .build();
        Cursor<Map.Entry<String, String>> cursor = hashOps.scan(timeWindowKey, options);

        Map<Long, Long> contentViewCount = new HashMap<>();

        while (cursor.hasNext()) {
            Map.Entry<String, String> entry = cursor.next();
            log.info("key: {}, value: {}", entry.getKey(), entry.getValue());
            contentViewCount.put(Long.parseLong(entry.getKey()), Long.parseLong(entry.getValue()));
        }

        return contentViewCount;

    }

    public void deleteProcessedKeys(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 직전 1분 동안의 조회수를 조회하기 위한 key를 생성합니다.
     * 예시:
     * - 현재 시간: 2024-10-27 16:10
     * - 생성되는 key: "viewCount:time:202410271609"
     */
    public String getPreviousMinuteViewCountKey() {
        return VIEW_COUNT_KEY_PREFIX + getPreviousTimeWindow();
    }

    private static String getPreviousTimeWindow() {
        LocalDateTime oneMinuteBefore = LocalDateTime.now().minusMinutes(1);
        return oneMinuteBefore.truncatedTo(ChronoUnit.MINUTES).toString();
    }}
