package com.streaming.settlement.streamingservice.streaming.service;

import com.streaming.settlement.streamingservice.streaming.domain.UserContentStreamingLog;
import com.streaming.settlement.streamingservice.streaming.repository.UserContentStreamingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserContentStreamingLogServiceImpl implements UserContentStreamingLogService {

    private final UserContentStreamingLogRepository userContentStreamingLogRepository;

    private static final Long INITIAL_PLAYBACK_POSITION = 0L;

    @Override
    public void create(UserContentStreamingLog userContentStreamingLog) {
        logValidation(userContentStreamingLog);
        userContentStreamingLogRepository.save(userContentStreamingLog);
    }

    @Override
    public Long getLastPlaybackPositionOfLatestStreaming(Long userId, Long contentId) {
        return userContentStreamingLogRepository.findFirstByUserIdAndContentIdOrderByPlaybackStartDateDesc(userId, contentId)
                .map(UserContentStreamingLog::getLastPlaybackPosition)
                .orElse(INITIAL_PLAYBACK_POSITION);
    }

    private static void logValidation(UserContentStreamingLog userContentStreamingLog) {
        if (!userContentStreamingLog.isPlaybackStartDateBeyondCurrent()) {
            // TODO : 서비스 예외 처리
            throw new RuntimeException("Invalid playback start date");
        }
        if (userContentStreamingLog.isTotalPlaybackTimeExceedsLastPlay()) {
            // TODO : 서비스 예외 처리
            throw new RuntimeException("Total playback time exceeds last playback time");
        }
    }

}
