package com.streaming.settlement.batchserver.statistic.dto;

import com.querydsl.core.annotations.QueryProjection;

public record CumulativeStatisticDto(
        Long id,
        Long contentId,
        Long totalContentViews,
        Long totalContentRevenue,
        Long totalAdvertisementViews,
        Long totalAdvertisementRevenue,
        Long totalPlaybackTime) {
    @QueryProjection
    public CumulativeStatisticDto {
    }
}
