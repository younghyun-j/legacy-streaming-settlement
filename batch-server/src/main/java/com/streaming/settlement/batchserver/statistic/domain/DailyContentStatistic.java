package com.streaming.settlement.batchserver.statistic.domain;

import com.streaming.settlement.batchserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private Long totalViews;

    @Column(nullable = false)
    private Long totalContentRevenue;

    @Column(nullable = false)
    private Long totalAdvertisementViews;

    @Column(nullable = false)
    private Long totalAdvertisementRevenue;

    @Column(nullable = false)
    private Long totalPlaybackTime;

    public DailyContentStatistic(CumulativeContentStatistic cumulativeContentStatistic, Long totalViews, Long totalContentRevenue, Long totalAdvertisementViews, Long totalAdvertisementRevenue, Long totalPlaybackTime) {
        this.cumulativeContentStatistic = cumulativeContentStatistic;
        this.totalViews = totalViews;
        this.totalContentRevenue = totalContentRevenue;
        this.totalAdvertisementViews = totalAdvertisementViews;
        this.totalAdvertisementRevenue = totalAdvertisementRevenue;
        this.totalPlaybackTime = totalPlaybackTime;
    }
}
