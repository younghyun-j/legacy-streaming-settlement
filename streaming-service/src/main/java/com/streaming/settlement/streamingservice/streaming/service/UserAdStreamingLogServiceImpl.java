package com.streaming.settlement.streamingservice.streaming.service;

import com.streaming.settlement.streamingservice.streaming.entity.UserAdvertisementStreamingLog;
import com.streaming.settlement.streamingservice.streaming.repository.UserAdStreamingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdStreamingLogServiceImpl implements UserAdStreamingLogService{

    private final UserAdStreamingLogRepository userAdStreamingLogRepository;

    @Override
    public void create(List<UserAdvertisementStreamingLog> userAdvertisementStreamingLogs) {
        userAdStreamingLogRepository.saveAll(userAdvertisementStreamingLogs);
    }
}
