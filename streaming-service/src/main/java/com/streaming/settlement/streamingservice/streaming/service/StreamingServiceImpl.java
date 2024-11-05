package com.streaming.settlement.streamingservice.streaming.service;


import com.streaming.settlement.streamingservice.content.domain.Content;
import com.streaming.settlement.streamingservice.content.service.ContentService;
import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;
import com.streaming.settlement.streamingservice.streaming.domain.UserContentStreamingLog;
import com.streaming.settlement.streamingservice.streaming.dto.response.StreamingContentRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreamingServiceImpl implements StreamingService {

    private final ContentService contentService;
    private final UserContentStreamingLogService userContentStreamingLogService;
    private final UserAdStreamingLogService userAdStreamingLogService;

    @Override
    public StreamingContentRes getStreamingContent(Long userId, Long contentId) {
        Content getContent = contentService.getContent(contentId);
        Long lastPlaybackPosition = userContentStreamingLogService.getLastPlaybackPositionOfLatestStreaming(userId, getContent.getId());
        return StreamingContentRes.of(getContent, lastPlaybackPosition);
    }

    @Transactional
    @Override
    public void endStreamingContent(UserContentStreamingLog streamingLog, List<UserAdvertisementStreamingLog> userAdvertisementStreamingLogs) {
        userContentStreamingLogService.create(streamingLog);
        userAdStreamingLogService.create(userAdvertisementStreamingLogs);
    }
}
