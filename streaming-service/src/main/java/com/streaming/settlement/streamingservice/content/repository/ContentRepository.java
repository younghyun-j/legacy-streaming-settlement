package com.streaming.settlement.streamingservice.content.repository;

import com.streaming.settlement.streamingservice.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long>{

}
