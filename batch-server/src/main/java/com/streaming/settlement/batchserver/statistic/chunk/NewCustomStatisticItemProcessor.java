package com.streaming.settlement.batchserver.statistic.chunk;

import com.streaming.settlement.batchserver.global.util.CreateDateJobParameter;
import com.streaming.settlement.batchserver.statistic.entity.AdvertisementRevenueRange;
import com.streaming.settlement.batchserver.statistic.entity.ContentRevenueRange;
import com.streaming.settlement.batchserver.statistic.dto.*;
import com.streaming.settlement.batchserver.statistic.repository.UserAdStreamingLogRepository;
import com.streaming.settlement.batchserver.statistic.repository.UserContentStreamingLogQueryDSLRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
// @Component
// @StepScope
@RequiredArgsConstructor
public class NewCustomStatisticItemProcessor implements ItemProcessor<CumulativeStatisticDto, NewCumulativeAndDailyStatisticDto> {

    private final CreateDateJobParameter createDateJobParameter;
    private final UserContentStreamingLogQueryDSLRepository contentStreamingLogRepository;
    private final UserAdStreamingLogRepository adStreamingLogRepository;

    @Override
    public NewCumulativeAndDailyStatisticDto process(CumulativeStatisticDto cumulativeStatisticDto) throws Exception {
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

    private NewCumulativeAndDailyStatisticDto createCumulativeAndDailyStatisticsDto(CumulativeStatisticDto cumulativeStatisticDto, long dailyContentViews, long dailyContentRevenue, long dailyAdViews, long dailyAdRevenue, long dailyContentTotalPlaybackTime, long totalContentViews, long totalContentRevenue, long totalAdvertisementViews, long totalAdvertisementRevenue, long totalContentPlaybackTime) {
        DailyStatisticDto dailyStatisticDto = new DailyStatisticDto(
                cumulativeStatisticDto.id(),
                dailyContentViews,
                dailyContentRevenue,
                dailyAdViews,
                dailyAdRevenue,
                dailyContentTotalPlaybackTime);
        CumulativeStatisticDto updateCumulativeStatisticDto = new CumulativeStatisticDto(
                cumulativeStatisticDto.id(),
                cumulativeStatisticDto.contentId(),
                totalContentViews,
                totalContentRevenue,
                totalAdvertisementViews,
                totalAdvertisementRevenue,
                totalContentPlaybackTime
        );
        return new NewCumulativeAndDailyStatisticDto(updateCumulativeStatisticDto, dailyStatisticDto);
    }
}

