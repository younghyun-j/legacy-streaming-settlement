package com.streaming.settlement.batchserver.statistic.job;

import com.streaming.settlement.batchserver.statistic.chunk.CustomStatisticItemProcessor;
import com.streaming.settlement.batchserver.statistic.chunk.CustomStatisticItemReader;
import com.streaming.settlement.batchserver.statistic.domain.CumulativeContentStatistic;
import com.streaming.settlement.batchserver.statistic.domain.DailyContentStatistic;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeAndDailyStatisticDto;
import com.streaming.settlement.batchserver.statistic.dto.CumulativeStatisticDto;
import com.streaming.settlement.batchserver.global.util.CreateDateJobParameter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ContentStatisticJobConfig {

    @Value("${spring.batch.chunk.size}")
    private int CHUNK_SIZE;

    private final EntityManagerFactory entityManagerFactory;
    private final CustomStatisticItemReader customStatisticItemReader;
    private final CustomStatisticItemProcessor customStatisticItemProcessor;

    @Bean
    public Job statisticJob(JobRepository jobRepository, Step statisticStep) {
        return new JobBuilder("statisticJob", jobRepository)
                .start(statisticStep)
                .build();
    }

    @JobScope
    @Bean
    public CreateDateJobParameter JobParameter(@Value("#{jobParameters['settlementDate']}") String settlementDateStr) {
        return new CreateDateJobParameter(settlementDateStr);
    }

    @JobScope
    @Bean
    public Step statisticStep(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("statisticStep", jobRepository)
                .<CumulativeStatisticDto, CumulativeAndDailyStatisticDto>chunk(CHUNK_SIZE, tx)
                .reader(customStatisticItemReader)
                .processor(customStatisticItemProcessor)
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<CumulativeAndDailyStatisticDto> compositeItemWriter() {
        CompositeItemWriter<CumulativeAndDailyStatisticDto> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(
                dailyStatisticsWriter(),
                cumulativeStatisticsWriter()
        ));
        return writer;
    }

    @Bean
    @StepScope
    public ItemWriter<CumulativeAndDailyStatisticDto> dailyStatisticsWriter() {
        return items -> {
            JpaItemWriter<DailyContentStatistic> writer = new JpaItemWriter<>();
            writer.setEntityManagerFactory(entityManagerFactory);

            List<DailyContentStatistic> dailyStatistics = items.getItems().stream()
                    .map(CumulativeAndDailyStatisticDto::dailyContentStatistic)
                    .collect(Collectors.toList());

            writer.write(new Chunk<>(dailyStatistics));
        };
    }

    @Bean
    @StepScope
    public ItemWriter<CumulativeAndDailyStatisticDto> cumulativeStatisticsWriter() {
        return items -> {
            JpaItemWriter<CumulativeContentStatistic> writer = new JpaItemWriter<>();
            writer.setEntityManagerFactory(entityManagerFactory);

            List<CumulativeContentStatistic> cumulativeStatistics = items.getItems().stream()
                    .map(CumulativeAndDailyStatisticDto::cumulativeContentStatistic)
                    .collect(Collectors.toList());

            writer.write(new Chunk<>(cumulativeStatistics));
        };
    }
}
