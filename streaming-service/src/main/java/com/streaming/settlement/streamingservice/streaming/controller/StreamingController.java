package com.streaming.settlement.streamingservice.streaming.controller;


import com.streaming.settlement.streamingservice.content.service.ContentService;
import com.streaming.settlement.streamingservice.global.redis.dto.AbusingKey;
import com.streaming.settlement.streamingservice.global.redis.service.DailyViewedContentCacheService;
import com.streaming.settlement.streamingservice.global.redis.service.ViewAbusingCacheService;
import com.streaming.settlement.streamingservice.global.redis.service.ViewCountCacheService;
import com.streaming.settlement.streamingservice.global.util.IpUtil;
import com.streaming.settlement.streamingservice.streaming.entity.UserAdvertisementStreamingLog;
import com.streaming.settlement.streamingservice.streaming.entity.UserContentStreamingLog;
import com.streaming.settlement.streamingservice.streaming.dto.request.UserStreamingReportReq;
import com.streaming.settlement.streamingservice.streaming.dto.response.StreamingContentRes;
import com.streaming.settlement.streamingservice.streaming.service.StreamingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final StreamingService streamingService;
    private final ContentService contentService;
    private final ViewAbusingCacheService viewAbusingCacheService;
    private final DailyViewedContentCacheService dailyViewedContentCacheService;
    private final ViewCountCacheService viewCountCacheService;

    // TODO : 인증 구현시 userId param 제거
    @GetMapping("/contents/{contentId}")
    public ResponseEntity<StreamingContentRes> startStreaming(HttpServletRequest request,
                                                              @RequestParam Long userId,
                                                              @PathVariable Long contentId) {
        StreamingContentRes response = streamingService.getStreamingContent(userId, contentId);
        AbusingKey abusingKey = new AbusingKey(userId,
                response.contentId(),
                response.creatorId(),
                IpUtil.getClientIp(request));
        boolean isAbusing = viewAbusingCacheService.isAbusing(abusingKey);

        if (!isAbusing) {
            dailyViewedContentCacheService.setContentId(contentId); // contentId 저장
            viewCountCacheService.incrementViewCount(contentId); // 조회수 증가
            viewAbusingCacheService.setAbusing(abusingKey); // 어뷰징 등록
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/contents/{contentId}")
    public ResponseEntity<Void> endStreaming(@RequestParam Long userId,
                                             @PathVariable Long contentId,
                                             @RequestBody UserStreamingReportReq request) {

        contentService.isExistContent(contentId);

        UserContentStreamingLog userContentStreamingLog = request.toUserContentStreamingLog(userId, contentId);
        List<UserAdvertisementStreamingLog> userAdvertisementStreamingLogs = request.toUserAdStreamingLogs(userId, contentId);
        streamingService.endStreamingContent(userContentStreamingLog, userAdvertisementStreamingLogs);

        return ResponseEntity.noContent().build();
    }

}
