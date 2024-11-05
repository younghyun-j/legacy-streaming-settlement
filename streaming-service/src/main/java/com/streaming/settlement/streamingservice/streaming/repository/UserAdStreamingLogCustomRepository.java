package com.streaming.settlement.streamingservice.streaming.repository;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;

import java.util.List;

public interface UserAdStreamingLogCustomRepository {
    void saveAll(List<UserAdvertisementStreamingLog> logs);
}
