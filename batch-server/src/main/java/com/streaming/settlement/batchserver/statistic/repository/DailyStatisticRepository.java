package com.streaming.settlement.batchserver.statistic.repository;

import com.streaming.settlement.batchserver.statistic.dto.DailyStatisticDto;

import java.util.List;

public interface DailyStatisticRepository {
    void bulkInsertDailyStatistic(List<DailyStatisticDto> cumulativeStatistics);
}
