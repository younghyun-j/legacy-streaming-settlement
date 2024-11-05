package com.streaming.settlement.batchserver.statistic.chunk;

import com.streaming.settlement.batchserver.global.util.CreateDateJobParameter;
import com.streaming.settlement.batchserver.statistic.entity.AdvertisementRevenueRange;
import com.streaming.settlement.batchserver.statistic.entity.ContentRevenueRange;
import com.streaming.settlement.batchserver.statistic.entity.CumulativeContentStatistic;
import com.streaming.settlement.batchserver.statistic.entity.DailyContentStatistic;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeAndDailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.DailyContentAggregationDto;
import com.streaming.settlement.batchserver.statistic.repository.UserAdStreamingLogRepository;
import com.streaming.settlement.batchserver.statistic.repository.UserContentStreamingLogQueryDSLRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class CustomStatisticItemProcessor implements ItemProcessor<CumulativeStatisticDto, CumulativeAndDailyStatisticDto> {

    private final CreateDateJobParameter createDateJobParameter;
    private final UserContentStreamingLogQueryDSLRepository contentStreamingLogRepository;
    private final UserAdStreamingLogRepository adStreamingLogRepository;

    @Override
    public CumulativeAndDailyStatisticDto process(CumulativeStatisticDto cumulativeStatisticDto) throws Exception {
        DailyContentAggregationDto dailyContentAggregationDto = contentStreamingLogRepository.sumPlaybackTimeAndCountStreamingByContentIdAndDate(cumulativeStatisticDto.contentId(), createDateJobParameter.getCreateDate());
        // 일일 영상 조회수, 광고 조회수, 총 재생 시간
        long dailyAdViews = adStreamingLogRepository.countAdStreamingByContentIdAndDate(cumulativeStatisticDto.contentId(), createDateJobParameter.getCreateDate());
        long dailyContentViews = dailyContentAggregationDto.views();
        long dailyContentTotalPlaybackTime = dailyContentAggregationDto.totalPlaybackTime();

        // 누적 영상 조회수, 광고 조회수, 총 재생 시간
        long totalContentViews = cumulativeStatisticDto.totalContentViews() + dailyContentViews;
        long totalContentPlaybackTime = cumulativeStatisticDto.totalPlaybackTime() + dailyContentTotalPlaybackTime;
        long totalAdvertisementViews = cumulativeStatisticDto.totalAdvertisementViews() + dailyAdViews;

        // 조회수, 광고 조회수를 기반으로 누적 수익 계산
        long totalContentRevenue = ContentRevenueRange.calculateRevenueByViews(totalContentViews);
        long totalAdvertisementRevenue = AdvertisementRevenueRange.calculateRevenueByViews(totalAdvertisementViews);

        // 기존 누적 수익 - 현재 누적 수익 = 일일 수익
        long dailyContentRevenue = totalContentRevenue - cumulativeStatisticDto.totalContentRevenue();
        long dailyAdRevenue = totalAdvertisementRevenue - cumulativeStatisticDto.totalAdvertisementRevenue();

        CumulativeContentStatistic updateCumulativeContentStatistic = CumulativeContentStatistic.builder()
                .id(cumulativeStatisticDto.id())
                .contentId(cumulativeStatisticDto.contentId())
                .totalContentViews(totalContentViews)
                .totalContentRevenue(totalContentRevenue)
                .totalAdvertisementViews(totalAdvertisementViews)
                .totalAdvertisementRevenue(totalAdvertisementRevenue)
                .totalPlaybackTime(totalContentPlaybackTime)
                .build();
        DailyContentStatistic dailyContentStatistic = DailyContentStatistic.builder()
                .cumulativeContentStatistic(updateCumulativeContentStatistic)
                .totalContentViews(dailyContentViews)
                .totalContentRevenue(dailyContentRevenue)
                .totalAdvertisementViews(dailyAdViews)
                .totalAdvertisementRevenue(dailyAdRevenue)
                .totalPlaybackTime(dailyContentTotalPlaybackTime)
                .build();

        return new CumulativeAndDailyStatisticDto(updateCumulativeContentStatistic, dailyContentStatistic);
    }

}

