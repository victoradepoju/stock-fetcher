package com.group4.stock_fetcher.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class StockPriceEvent {
    private String symbol;
    private OffsetDateTime date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private String exchange;
}
