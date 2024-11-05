package com.streaming.settlement.streamingservice.streaming.dto.request;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;
import com.streaming.settlement.streamingservice.streaming.domain.UserContentStreamingLog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserStreamingReportReq(
        @NotBlank(message = "마지막 재생 시점은 필수 값입니다.")
        Long lastPlaybackPosition,
        @NotBlank(message = "총 재생 시간은 필수 값입니다.")
        Long totalPlaybackTime,
        @NotBlank(message = "재생 시작 날짜는 필수 값입니다.")
        LocalDate playbackStartDate,
        List<Long> advertiseIds

) {
    public UserContentStreamingLog toUserContentStreamingLog(Long userId, Long contentId) {
        return new UserContentStreamingLog(userId, contentId, lastPlaybackPosition, totalPlaybackTime, playbackStartDate);
    }

    public List<UserAdvertisementStreamingLog> toUserAdStreamingLogs(Long userId, Long contentId) {
        return advertiseIds.stream()
                .map(id -> new UserAdvertisementStreamingLog(userId, contentId, id, playbackStartDate))
                .toList();
    }
}
