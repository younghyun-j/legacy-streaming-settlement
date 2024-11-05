package com.streaming.settlement.scheduler.content.repository;

import java.util.Map;

public interface ContentRepository {
    void bulkUpdateViewCounts(Map<Long, Long> updateViewCounts);
}
