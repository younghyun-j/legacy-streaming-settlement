package com.streaming.settlement.scheduler.global.redis.dto;

public record UpdateViewCount(
        Long contentId,
        Long count
) {
}
