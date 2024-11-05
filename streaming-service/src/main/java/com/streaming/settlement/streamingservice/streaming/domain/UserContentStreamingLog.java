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
public class UserContentStreamingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long lastPlaybackPosition; // 마지막 재생 위치

    @Column(nullable = false)
    private Long totalPlaybackTime; // 총 재생 시간

    private LocalDate playbackStartDate; // 스트리밍 시작 시간

    private LocalDateTime createdAt; // 로그 생성 일자

    public UserContentStreamingLog(Long userId,
                                   Long contentId,
                                   Long lastPlaybackPosition,
                                   Long totalPlaybackTime,
                                   LocalDate playbackStartDate) {
        this.userId = userId;
        this.contentId = contentId;
        this.lastPlaybackPosition = lastPlaybackPosition;
        this.totalPlaybackTime = totalPlaybackTime;
        this.playbackStartDate = playbackStartDate;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isTotalPlaybackTimeExceedsLastPlay(){
        return totalPlaybackTime > lastPlaybackPosition;
    }

    public boolean isPlaybackStartDateBeyondCurrent(){
        return playbackStartDate.isBefore(LocalDate.now());
    }

}

