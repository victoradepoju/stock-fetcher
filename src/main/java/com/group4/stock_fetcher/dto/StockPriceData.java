package com.group4.stock_fetcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class StockPriceData {
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    @JsonProperty("adj_high")
    private Double adjHigh;

    @JsonProperty("adj_low")
    private Double adjLow;

    @JsonProperty("adj_close")
    private Double adjClose;

    @JsonProperty("adj_open")
    private Double adjOpen;

    @JsonProperty("adj_volume")
    private Long adjVolume;

    @JsonProperty("split_factor")
    private Double splitFactor;

    private Double dividend;
    private String symbol;
    private String exchange;
    private OffsetDateTime date;
}
