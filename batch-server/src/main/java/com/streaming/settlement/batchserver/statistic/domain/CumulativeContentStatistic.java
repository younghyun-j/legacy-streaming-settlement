package com.streaming.settlement.batchserver.statistic.domain;

import com.streaming.settlement.batchserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CumulativeContentStatistic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

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

    public CumulativeContentStatistic(Long id, Long contentId, Long totalContentViews, Long totalContentRevenue, Long totalAdvertisementViews, Long totalAdvertisementRevenue, Long totalPlaybackTime) {
        this.id = id;
        this.contentId = contentId;
        this.totalContentViews = totalContentViews;
        this.totalContentRevenue = totalContentRevenue;
        this.totalAdvertisementViews = totalAdvertisementViews;
        this.totalAdvertisementRevenue = totalAdvertisementRevenue;
        this.totalPlaybackTime = totalPlaybackTime;
    }
}
