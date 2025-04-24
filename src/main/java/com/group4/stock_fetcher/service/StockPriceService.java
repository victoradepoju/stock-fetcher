package com.group4.stock_fetcher.service;

import com.group4.stock_fetcher.dto.StockPriceData;
import com.group4.stock_fetcher.model.LastFetchTime;
import com.group4.stock_fetcher.model.StockPriceEvent;
import com.group4.stock_fetcher.repository.LastFetchTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPriceService {

    private final MarketStackClient marketStackClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final LastFetchTimeRepository lastFetchTimeRepository;

    @Value("${app.kafka.topic}")
    private String topic;

    @Scheduled(initialDelayString = "${app.scheduler.initial-delay-ms}", fixedDelayString = "${app.scheduler.fixed-delay-ms}")
    public void fetchAndSendStockPrices() {
        log.info("Fetching stock prices from MarketStack API");

        marketStackClient.fetchStockPrices()
                .subscribe(response -> {
                    response.getData().forEach(this::processStockPriceData);
                    updateLastFetchTime();
                }, error -> {
                    log.error("Error fetching stock prices: {}", error.getMessage());
                });
    }

    private void processStockPriceData(StockPriceData data) {
        StockPriceEvent event = new StockPriceEvent();
        event.setSymbol(data.getSymbol());
        event.setDate(data.getDate());
        event.setOpen(data.getOpen());
        event.setHigh(data.getHigh());
        event.setLow(data.getLow());
        event.setClose(data.getClose());
        event.setVolume(data.getVolume());
        event.setExchange(data.getExchange());

        kafkaTemplate.send(topic, data.getSymbol(), event);
        log.info("Sent stock price event for {}: {}", data.getSymbol(), event);
    }

    private void updateLastFetchTime() {
        LastFetchTime lastFetchTime = lastFetchTimeRepository.findById("last_fetch")
                .orElse(new LastFetchTime());
        lastFetchTime.setLastFetchTime(LocalDateTime.now(ZoneOffset.UTC));
        lastFetchTimeRepository.save(lastFetchTime);
    }

    public Optional<LocalDateTime> getLastFetchTime() {
        return lastFetchTimeRepository.findById("last_fetch")
                .map(LastFetchTime::getLastFetchTime);
    }
}
