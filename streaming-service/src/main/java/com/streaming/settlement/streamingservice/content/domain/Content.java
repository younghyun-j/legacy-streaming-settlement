package com.streaming.settlement.streamingservice.content.domain;

import com.streaming.settlement.streamingservice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @ColumnDefault("0")
    private Long views;

    @ColumnDefault("true")
    private boolean isActive;

    public Content(String title, String url, Long creatorId, boolean isActive) {
        this.title = title;
        this.url = url;
        this.creatorId = creatorId;
        this.isActive = isActive;
        this.views = 0L;
    }
}
