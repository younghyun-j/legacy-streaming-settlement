package com.streaming.settlement.streamingservice.content.service;

import com.streaming.settlement.streamingservice.content.entity.Content;

public interface ContentService {
    Content getContent(Long contentId);
    void isExistContent(Long contentId);
}
