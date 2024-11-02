package com.streaming.settlement.batchserver.statistic.dto;

import com.streaming.settlement.batchserver.statistic.domain.CumulativeContentStatistic;
import com.streaming.settlement.batchserver.statistic.domain.DailyContentStatistic;

public record CumulativeAndDailyStatisticDto(
        CumulativeContentStatistic cumulativeContentStatistic,
        DailyContentStatistic dailyContentStatistic
) {}
