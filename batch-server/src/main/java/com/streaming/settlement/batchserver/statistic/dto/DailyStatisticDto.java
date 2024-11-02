package com.streaming.settlement.batchserver.statistic.dto;

public record DailyStatisticDto(
        Long cumulativeStatisticId,
        Long totalViews,
        Long totalContentRevenue,
        Long totalAdvertisementViews,
        Long totalAdvertisementRevenue,
        Long totalPlaybackTime
) { }
