package com.streaming.settlement.batchserver.statistic.dto;

import com.querydsl.core.annotations.QueryProjection;

public record DailyContentAggregationDto(Long views, Long totalPlaybackTime) {
    @QueryProjection
    public DailyContentAggregationDto {
    }
}
