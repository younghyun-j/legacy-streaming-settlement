package com.streaming.settlement.batchserver.statistic.repository;


import com.streaming.settlement.batchserver.statistic.entity.UserAdvertisementStreamingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface UserAdStreamingLogRepository extends JpaRepository<UserAdvertisementStreamingLog, Long> {
    @Query("SELECT COUNT(log.id) " +
            "FROM UserAdvertisementStreamingLog log " +
            "WHERE log.contentId = :contentId " +
            "AND log.playbackStartDate = :playbackStartDate")
    Long countAdStreamingByContentIdAndDate(Long contentId, LocalDate playbackStartDate);
}
