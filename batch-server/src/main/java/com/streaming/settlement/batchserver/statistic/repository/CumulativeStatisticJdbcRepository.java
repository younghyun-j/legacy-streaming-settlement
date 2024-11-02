package com.streaming.settlement.batchserver.statistic.repository;


import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CumulativeStatisticJdbcRepository implements CumulativeStatisticRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void bulkUpdateCumulativeStatistics(List<CumulativeStatisticDto> cumulativeStatistics) {
        for (CumulativeStatisticDto cumulativeStatistic : cumulativeStatistics) {
            log.info("cumulativeStatistic.contentId {}", cumulativeStatistic.contentId());
        }
        String sql = """
                UPDATE cumulative_content_statistic 
                SET total_content_views = COALESCE(total_content_views, 0) + :totalContentViews,
                    total_content_revenue = COALESCE(total_content_revenue, 0) + :totalContentRevenue,
                    total_advertisement_views = COALESCE(total_advertisement_views, 0) + :totalAdvertisementViews,
                    total_advertisement_revenue = COALESCE(total_advertisement_revenue, 0) + :totalAdvertisementRevenue,
                    total_playback_time = COALESCE(total_playback_time, 0) + :totalPlaybackTime,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = :id
                """;
        namedParameterJdbcTemplate.batchUpdate(sql, getUpdateCumulativeContentStatisticToSqlParameterSources(cumulativeStatistics));
    }

    private MapSqlParameterSource[] getUpdateCumulativeContentStatisticToSqlParameterSources(List<CumulativeStatisticDto> cumulativeStatistics) {
        return cumulativeStatistics.stream()
                .map(this::getUpdateCumulativeContentStatisticSqlParameterSource)
                .toArray(MapSqlParameterSource[]::new);
    }

    private MapSqlParameterSource getUpdateCumulativeContentStatisticSqlParameterSource(CumulativeStatisticDto cumulativeStatistics) {
        return new MapSqlParameterSource()
                .addValue("id", cumulativeStatistics.id())
                .addValue("totalContentViews", cumulativeStatistics.totalContentViews())
                .addValue("totalContentRevenue", cumulativeStatistics.totalContentRevenue())
                .addValue("totalAdvertisementViews", cumulativeStatistics.totalAdvertisementViews())
                .addValue("totalAdvertisementRevenue", cumulativeStatistics.totalAdvertisementRevenue())
                .addValue("totalPlaybackTime", cumulativeStatistics.totalPlaybackTime());
    }

}
