package com.streaming.settlement.batchserver.statistic.dto;

import com.streaming.settlement.batchserver.statistic.entity.CumulativeContentStatistic;
import com.streaming.settlement.batchserver.statistic.entity.DailyContentStatistic;

public record CumulativeAndDailyStatisticDto(
        CumulativeContentStatistic cumulativeContentStatistic,
        DailyContentStatistic dailyContentStatistic
) {}
