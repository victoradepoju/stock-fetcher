package com.group4.stock_fetcher;

import com.group4.stock_fetcher.service.StockPriceService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class StockFetcherApplication {

	private final StockPriceService stockPriceService;

	public static void main(String[] args) {
		SpringApplication.run(StockFetcherApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Fetch immediately on startup if not fetched today
		stockPriceService.getLastFetchTime().ifPresentOrElse(
				lastFetch -> {
					if (lastFetch.toLocalDate().isBefore(java.time.LocalDate.now())) {
						stockPriceService.fetchAndSendStockPrices();
					}
				},
				() -> stockPriceService.fetchAndSendStockPrices() // First time run
		);
	}
}
