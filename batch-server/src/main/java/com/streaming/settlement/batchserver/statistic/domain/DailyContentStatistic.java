package com.streaming.settlement.batchserver.statistic.domain;

import com.streaming.settlement.batchserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyContentStatistic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cumulative_content_statistic_id")
    private CumulativeContentStatistic cumulativeContentStatistic;

    @Column(nullable = false)
    private Long totalContentViews;

    @Column(nullable = false)
    private Long totalContentRevenue;

    @Column(nullable = false)
    private Long totalAdvertisementViews;

    @Column(nullable = false)
    private Long totalAdvertisementRevenue;

    @Column(nullable = false)
    private Long totalPlaybackTime;

    @Builder
    public DailyContentStatistic(CumulativeContentStatistic cumulativeContentStatistic, Long totalContentViews, Long totalContentRevenue, Long totalAdvertisementViews, Long totalAdvertisementRevenue, Long totalPlaybackTime) {
        this.cumulativeContentStatistic = cumulativeContentStatistic;
        this.totalContentViews = totalContentViews;
        this.totalContentRevenue = totalContentRevenue;
        this.totalAdvertisementViews = totalAdvertisementViews;
        this.totalAdvertisementRevenue = totalAdvertisementRevenue;
        this.totalPlaybackTime = totalPlaybackTime;
    }
}
