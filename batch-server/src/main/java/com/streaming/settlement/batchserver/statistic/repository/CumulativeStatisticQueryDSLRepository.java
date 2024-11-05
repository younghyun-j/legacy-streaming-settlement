package com.streaming.settlement.batchserver.statistic.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.QCumulativeStatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.streaming.settlement.batchserver.statistic.entity.QCumulativeContentStatistic.cumulativeContentStatistic;

@Repository
@RequiredArgsConstructor
public class CumulativeStatisticQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CumulativeStatisticDto> findCumulativeContentStatisticsByContentId(List<Long> contentIds) {
            return jpaQueryFactory
                    .select(
                            new QCumulativeStatisticDto(
                                    cumulativeContentStatistic.id,
                                    cumulativeContentStatistic.contentId,
                                    cumulativeContentStatistic.totalContentViews,
                                    cumulativeContentStatistic.totalContentRevenue,
                                    cumulativeContentStatistic.totalAdvertisementViews,
                                    cumulativeContentStatistic.totalAdvertisementRevenue,
                                    cumulativeContentStatistic.totalPlaybackTime
                            )
                    )
                    .from(cumulativeContentStatistic)
                    .where(cumulativeContentStatistic.contentId.in(contentIds))
                    .fetch();
    }
}
