package com.streaming.settlement.streamingservice.streaming.repository;

import com.streaming.settlement.streamingservice.streaming.domain.UserAdvertisementStreamingLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserAdStreamingLogCustomRepositoryImpl implements UserAdStreamingLogCustomRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<UserAdvertisementStreamingLog> logs) {
        String sql = """
                    INSERT INTO user_advertisement_streaming_log (user_id, content_id, advertisement_id) 
                    VALUES (:userId, :contentId, :advertisementId)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getUserAdStreamingLogToSqlParameterSources(logs));
    }

    private MapSqlParameterSource[] getUserAdStreamingLogToSqlParameterSources(List<UserAdvertisementStreamingLog> logs) {
        return logs.stream()
                .map(this::getUserAdStreamingLogToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getUserAdStreamingLogToSqlParameterSource(UserAdvertisementStreamingLog log) {
        return new MapSqlParameterSource()
                .addValue("userId", log.getUserId())
                .addValue("contentId", log.getContentId())
                .addValue("advertisementId", log.getAdvertisementId());
    }
}
