package com.streaming.settlement.streamingservice.streaming.dto.response;

import com.streaming.settlement.streamingservice.content.domain.Content;

import java.time.LocalDate;

public record StreamingContentRes(
    Long contentId,
    Long creatorId,
    String contentUrl,
    Long startPlaybackPosition,
    LocalDate playbackStartDate
) {
    public static StreamingContentRes of(Content content, Long playBackPosition) {
        return new StreamingContentRes(content.getId(), content.getCreatorId(), content.getUrl(), playBackPosition, LocalDate.now());
    }
}
