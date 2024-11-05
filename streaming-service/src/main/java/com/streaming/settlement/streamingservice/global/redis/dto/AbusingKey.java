package com.streaming.settlement.streamingservice.global.redis.dto;

public record AbusingKey(
        Long userId,
        Long contentId,
        Long creatorId,
        String ip
) {
}
