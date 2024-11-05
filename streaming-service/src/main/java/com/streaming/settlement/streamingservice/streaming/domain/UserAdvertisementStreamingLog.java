package com.streaming.settlement.streamingservice.streaming.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAdvertisementStreamingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long advertisementId;

    private LocalDate playbackStartDate; // 스트리밍 시작 시간

    private LocalDateTime createdAt; // 로그 생성 일자

    public UserAdvertisementStreamingLog(Long userId, Long contentId, Long advertisementId, LocalDate playbackStartDate) {
        this.userId = userId;
        this.contentId = contentId;
        this.advertisementId = advertisementId;
        this.playbackStartDate = playbackStartDate;
        this.createdAt = LocalDateTime.now();
    }
}