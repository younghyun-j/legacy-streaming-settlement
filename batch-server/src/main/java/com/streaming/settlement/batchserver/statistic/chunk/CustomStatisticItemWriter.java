package com.streaming.settlement.batchserver.statistic.chunk;


import com.streaming.settlement.batchserver.statistic.dto.CumulativeAndDailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.DailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.NewCumulativeAndDailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.repository.CumulativeStatisticJdbcRepository;
import com.streaming.settlement.batchserver.statistic.repository.DailyStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
// @Component
// @StepScope
@RequiredArgsConstructor
public class CustomStatisticItemWriter implements ItemWriter<NewCumulativeAndDailyStatisticDto> {

    private final CumulativeStatisticJdbcRepository cumulativeStatisticJdbcRepository;
    private final DailyStatisticRepository dailyStatisticRepository;

    @Override
    public void write(Chunk<? extends NewCumulativeAndDailyStatisticDto> chunk) throws Exception {
        List<CumulativeStatisticDto> cumulativeStatistics = new ArrayList<>();
        List<DailyStatisticDto> dailyStatistics = new ArrayList<>();

        for(NewCumulativeAndDailyStatisticDto cumulativeAndDailyStatisticDto : chunk.getItems()) {
            cumulativeStatistics.add(cumulativeAndDailyStatisticDto.cumulativeStatisticDto());
            dailyStatistics.add(cumulativeAndDailyStatisticDto.dailyStatisticDto());
        }

        cumulativeStatisticJdbcRepository.bulkUpdateCumulativeStatistics(cumulativeStatistics);
        dailyStatisticRepository.bulkInsertDailyStatistic(dailyStatistics);

    }
}
