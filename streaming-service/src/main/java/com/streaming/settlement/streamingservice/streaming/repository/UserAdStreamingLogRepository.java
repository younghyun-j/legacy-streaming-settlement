package com.streaming.settlement.streamingservice.streaming.repository;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdStreamingLogRepository extends JpaRepository<UserAdvertisementStreamingLog, Long>, UserAdStreamingLogCustomRepository {

}
