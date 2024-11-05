package com.streaming.settlement.batchserver.statistic.dto;

public record NewCumulativeAndDailyStatisticDto(
        CumulativeStatisticDto cumulativeStatisticDto,
        DailyStatisticDto dailyStatisticDto
) {}
