package com.streaming.settlement.batchserver.statistic.chunk;

import com.streaming.settlement.batchserver.statistic.domain.AdvertisementRevenueRange;
import com.streaming.settlement.batchserver.statistic.domain.ContentRevenueRange;
import com.streaming.settlement.batchserver.statistic.domain.CumulativeContentStatistic;
import com.streaming.settlement.batchserver.statistic.domain.DailyContentStatistic;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeAndDailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.DailyContentAggregationDto;
import com.streaming.settlement.batchserver.statistic.dto.DailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.repository.UserAdStreamingLogRepository;
import com.streaming.settlement.batchserver.global.util.CreateDateJobParameter;
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

        return createCumulativeAndDailyStatisticsDto(cumulativeStatisticDto, dailyContentViews, dailyContentRevenue, dailyAdViews, dailyAdRevenue, dailyContentTotalPlaybackTime, totalContentViews, totalContentRevenue, totalAdvertisementViews, totalAdvertisementRevenue, totalContentPlaybackTime);
    }

    private CumulativeAndDailyStatisticDto createCumulativeAndDailyStatisticsDto(CumulativeStatisticDto cumulativeStatisticDto, long dailyContentViews, long dailyContentRevenue, long dailyAdViews, long dailyAdRevenue, long dailyContentTotalPlaybackTime, long totalContentViews, long totalContentRevenue, long totalAdvertisementViews, long totalAdvertisementRevenue, long totalContentPlaybackTime) {
        CumulativeContentStatistic updateCumulativeContentStatistic = new CumulativeContentStatistic(
                cumulativeStatisticDto.id(),
                cumulativeStatisticDto.contentId(),
                cumulativeStatisticDto.totalContentViews(),
                cumulativeStatisticDto.totalContentRevenue(),
                cumulativeStatisticDto.totalAdvertisementViews(),
                cumulativeStatisticDto.totalAdvertisementRevenue(),
                cumulativeStatisticDto.totalPlaybackTime());
        DailyContentStatistic dailyContentStatistic = new DailyContentStatistic(
                updateCumulativeContentStatistic,
                dailyContentViews,
                dailyAdViews,
                dailyContentRevenue,
                dailyAdRevenue,
                dailyContentTotalPlaybackTime);

        return new CumulativeAndDailyStatisticDto(updateCumulativeContentStatistic, dailyContentStatistic);
    }
}

