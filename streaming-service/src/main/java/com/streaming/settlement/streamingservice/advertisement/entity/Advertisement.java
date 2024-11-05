package com.streaming.settlement.streamingservice.advertisement.entity;

import com.streaming.settlement.streamingservice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertisement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    private Long views;

    private boolean isActive;

    public Advertisement(String title, String url, boolean isActive) {
        this.title = title;
        this.url = url;
        this.isActive = isActive;
        this.views = 0L;
    }
}
