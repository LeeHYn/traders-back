package com.traders.tradersback.dto;

import java.util.List;
import java.util.Map;

public class PriceTrendDTO {
    private Map<String, Double> averagePrices; // 월-년과 평균 가격 매핑
    private List<Double> recentPrices; // 최근 상품 가격 목록

    // 생성자, 게터, 세터 생략

    public Map<String, Double> getAveragePrices() {
        return averagePrices;
    }

    public void setAveragePrices(Map<String, Double> averagePrices) {
        this.averagePrices = averagePrices;
    }

    public List<Double> getRecentPrices() {
        return recentPrices;
    }

    public void setRecentPrices(List<Double> recentPrices) {
        this.recentPrices = recentPrices;
    }
}
