package com.streaming.settlement.streamingservice.streaming.service;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;

import java.util.List;

public interface UserAdStreamingLogService {
    void create(List<UserAdvertisementStreamingLog> userAdvertisementStreamingLogs);
}
