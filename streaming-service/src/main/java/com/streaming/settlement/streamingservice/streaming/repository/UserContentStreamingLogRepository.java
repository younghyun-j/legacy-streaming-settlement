package com.streaming.settlement.streamingservice.streaming.repository;

import com.streaming.settlement.streamingservice.streaming.entity.UserContentStreamingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserContentStreamingLogRepository extends JpaRepository<UserContentStreamingLog, Long> {
    Optional<UserContentStreamingLog> findFirstByUserIdAndContentIdOrderByPlaybackStartDateDesc(Long userId, Long contentId);
}
