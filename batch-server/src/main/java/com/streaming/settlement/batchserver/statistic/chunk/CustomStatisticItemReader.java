package com.streaming.settlement.batchserver.statistic.chunk;


import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.statistic.repository.CumulativeStatisticQueryDSLRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class CustomStatisticItemReader implements ItemStreamReader<CumulativeStatisticDto> {
    private final CumulativeStatisticQueryDSLRepository cumulativeStatisticQueryDSLRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Queue<CumulativeStatisticDto> statistics = new LinkedList<>();
    private static final String DAILY_VIEWED_CONTENT_KEY = "viewed:content:date:";

    @Value("${spring.batch.chunk.size}")
    private int CHUNK_SIZE;
    private List<Long> contentIds;
    private List<Long> currentChunk;
    private int currentIndex = 0;
    private int maxIndex = 0;

    @PostConstruct
    public void init() {
        String cacheKey = generateDailyViewedContentKey();
        contentIds = Objects.requireNonNull(redisTemplate.opsForZSet()
                        .range(cacheKey, 0, -1))
                        .stream()
                        .map(Long::parseLong)
                        .toList();
        for (Long id : contentIds) {
            log.info("id = {}", id);
        }
    }

    @Override
    public CumulativeStatisticDto read() throws Exception {
        if (isCurrentChunkEmpty()) {
            currentChunk = fetchNextChunk();
            List<CumulativeStatisticDto> getStatistics = cumulativeStatisticQueryDSLRepository.findCumulativeContentStatisticsByContentId(contentIds);
            for (CumulativeStatisticDto getStatistic : getStatistics) {
                log.info("getStatistic.getContentId() = {}", getStatistic.contentId());
            }
            getStatistics.forEach(statistics::offer);
        }

        return statistics.poll();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey("currentIndex")) {
            currentIndex = executionContext.getInt("currentIndex");
            return;
        }
        executionContext.put("currentIndex", currentIndex);
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.put("currentIndex", currentIndex);
    }

    private List<Long> fetchNextChunk() {
        if(contentIds.isEmpty()) return Collections.emptyList();
        maxIndex = Math.min(contentIds.size(), currentIndex + CHUNK_SIZE);
        currentChunk = contentIds.subList(currentIndex, maxIndex);
        currentIndex = currentIndex + CHUNK_SIZE;
        if (currentChunk.isEmpty()) {
            return Collections.emptyList();
        }
        return currentChunk;
    }

    private boolean isCurrentChunkEmpty() {
        return currentChunk == null || currentChunk.isEmpty();
    }

    private static String generateDailyViewedContentKey() {
        return DAILY_VIEWED_CONTENT_KEY + LocalDate.now().minusDays(1L);
    }

}
