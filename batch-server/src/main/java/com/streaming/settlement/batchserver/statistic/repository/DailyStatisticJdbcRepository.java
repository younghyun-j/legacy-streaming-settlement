package com.streaming.settlement.batchserver.statistic.repository;

import com.streaming.settlement.batchserver.statistic.dto.DailyStatisticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DailyStatisticJdbcRepository implements DailyStatisticRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void bulkInsertDailyStatistic(List<DailyStatisticDto> dailyStatistics) {

        String sql = """
                INSERT daily_content_statistic(cumulative_content_statistic_id, total_views, total_content_revenue, total_advertisement_views, total_advertisement_revenue, total_playback_time)
                VALUES (:cumulativeStatisticId, :totalContentViews, :totalContentRevenue, :totalAdvertisementViews, :totalAdvertisementRevenue, :totalPlaybackTime)
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getInsertDailyContentStatisticToSqlParameterSources(dailyStatistics));
    }

    private MapSqlParameterSource[] getInsertDailyContentStatisticToSqlParameterSources(List<DailyStatisticDto> dailyStatistics) {
        return dailyStatistics.stream()
                .map(this::getInsertDailyContentStatisticToSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getInsertDailyContentStatisticToSqlParameterSource(DailyStatisticDto dailyStatisticDto) {
        return new MapSqlParameterSource()
                .addValue("cumulativeStatisticId", dailyStatisticDto.cumulativeStatisticId())
                .addValue("totalContentViews", dailyStatisticDto.totalViews())
                .addValue("totalContentRevenue", dailyStatisticDto.totalContentRevenue())
                .addValue("totalAdvertisementViews", dailyStatisticDto.totalAdvertisementViews())
                .addValue("totalAdvertisementRevenue", dailyStatisticDto.totalAdvertisementRevenue())
                .addValue("totalPlaybackTime", dailyStatisticDto.totalPlaybackTime());
    }
}
