package com.group4.stock_fetcher.service;


import com.group4.stock_fetcher.dto.MarketStackResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MarketStackClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String symbols;
    private final int limit;

    public MarketStackClient(
            @Value("${app.marketstack.api-url}") String apiUrl,
            @Value("${app.marketstack.api-key}") String apiKey,
            @Value("${app.marketstack.symbols}") String symbols,
            @Value("${app.marketstack.limit}") int limit) {
        this.webClient = WebClient.builder().baseUrl(apiUrl).build();
        this.apiKey = apiKey;
        this.symbols = symbols;
        this.limit = limit;
    }

    public Mono<MarketStackResponse> fetchStockPrices() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_key", apiKey)
                        .queryParam("symbols", symbols)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(MarketStackResponse.class);
    }
}
