package com.streaming.settlement.batchserver.statistic.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.batchserver.statistic.dto.DailyContentAggregationDto;
import com.streaming.settlement.batchserver.statistic.dto.QDailyContentAggregationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.streaming.settlement.batchserver.statistic.domain.QUserContentStreamingLog.userContentStreamingLog;


@Repository
@RequiredArgsConstructor
public class UserContentStreamingLogQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public DailyContentAggregationDto sumPlaybackTimeAndCountStreamingByContentIdAndDate(Long contentId, LocalDate playbackStartDate) {
        return jpaQueryFactory
                .select(new QDailyContentAggregationDto(
                        userContentStreamingLog.count(),
                        userContentStreamingLog.totalPlaybackTime.sum()
                ))
                .from(userContentStreamingLog)
                .where(
                        userContentStreamingLog.contentId.eq(contentId),
                        userContentStreamingLog.playbackStartDate.eq(playbackStartDate)
                )
                .fetchOne();
    }
}
