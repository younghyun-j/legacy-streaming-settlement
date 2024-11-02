package com.streaming.settlement.batchserver.global.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateDateJobParameter {

    private LocalDate createDate;

    public CreateDateJobParameter(String createDateStr) {
        this.createDate = LocalDate.parse(createDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
