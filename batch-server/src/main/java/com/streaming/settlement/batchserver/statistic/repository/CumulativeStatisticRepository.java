package com.streaming.settlement.batchserver.statistic.repository;


import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;

import java.util.List;

public interface CumulativeStatisticRepository {
    void bulkUpdateCumulativeStatistics(List<CumulativeStatisticDto> cumulativeStatistics);
}
