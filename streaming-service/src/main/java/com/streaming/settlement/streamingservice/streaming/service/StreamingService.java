package com.streaming.settlement.streamingservice.streaming.service;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;
import com.streaming.settlement.streamingservice.streaming.domain.UserContentStreamingLog;
import com.streaming.settlement.streamingservice.streaming.dto.response.StreamingContentRes;

import java.util.List;

public interface StreamingService {
    StreamingContentRes getStreamingContent(Long userId, Long contentId);
    void endStreamingContent(UserContentStreamingLog streamingLog, List<UserAdvertisementStreamingLog> userAdvertisementStreamingLogs);
}
