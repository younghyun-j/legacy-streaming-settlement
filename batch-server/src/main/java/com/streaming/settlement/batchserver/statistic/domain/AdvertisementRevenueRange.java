package com.streaming.settlement.batchserver.statistic.domain;

public enum AdvertisementRevenueRange {

    // 광고 단가
    AD_UNDER_100K(10, 0L, 99_999L),
    AD_UNDER_500K(12, 100_000L, 500_000L),
    AD_UNDER_1M(15, 500_000L, 1_000_000L),
    AD_OVER_1M(20, 1_000_000L, Long.MAX_VALUE);

    private final double pricePerView;
    private final long minViews;
    private final long maxViews;

    AdvertisementRevenueRange(double pricePerView, long minViews, long maxViews) {
        this.pricePerView = pricePerView;
        this.minViews = minViews;
        this.maxViews = maxViews;
    }

    public static Long calculateRevenueByViews(long totalViews) {
        long remainingViews = totalViews;
        long totalRevenue = 0;

        for (AdvertisementRevenueRange bracket : values()) {
            if (remainingViews <= 0) {
                break;
            }

            long bracketRange = bracket.maxViews - bracket.minViews;
            long views = Math.min(bracketRange, remainingViews);

            if (views > 0) {
                totalRevenue += (long) (views * bracket.pricePerView);
                remainingViews -= views;
            }
        }
        return totalRevenue;
    }
}
