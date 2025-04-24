package com.group4.stock_fetcher.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarketStackResponse {
    private Pagination pagination;
    private List<StockPriceData> data;

    @Data
    public static class Pagination {
        private int limit;
        private int offset;
        private int count;
        private int total;
    }
}
