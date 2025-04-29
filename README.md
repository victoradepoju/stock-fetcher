# Stock Fetcher Project

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.0-green)
![Kafka](https://img.shields.io/badge/Apache_Kafka-3.4.0-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.0-blue)

## Overview
The Stock Fetcher is a Spring Boot application that periodically fetches stock market data from the MarketStack API and publishes it to a Kafka topic. The application is designed to run on a schedule, fetch the latest stock prices, and send them as events to a Kafka broker for further processing by downstream services.

## Features
- Scheduled fetching of stock prices from MarketStack API
- Kafka integration for event streaming
- Persistent storage of last fetch time
- Configurable stock symbols and fetch intervals
- JSON serialization for Kafka messages

## Project Structure

```
📦 stock-fetcher
├── 📂 config
│   └── 📄 KafkaProducerConfig.java
├── 📂 dto
│   ├── 📄 MarketStackResponse.java
│   └── 📄 StockPriceData.java
├── 📂 model
│   ├── 📄 LastFetchTime.java
│   └── 📄 StockPriceEvent.java
├── 📂 repository
│   └── 📄 LastFetchTimeRepository.java
├── 📂 service
│   ├── 📄 MarketStackClient.java
│   └── 📄 StockPriceService.java
└── 📄 StockFetcherApplication.java
```


## Components

### 1. Kafka Producer Configuration (`KafkaProducerConfig`)
Configures the Kafka producer with:
- Bootstrap servers
- String serializer for keys
- JSON serializer for values
- Producer acknowledgments set to "all"
- 3 retries for failed sends

### 2. Data Transfer Objects
- `MarketStackResponse`: Wrapper for MarketStack API response with pagination and data
- `StockPriceData`: Represents individual stock price data points from MarketStack

### 3. Models
- `LastFetchTime`: Entity to track the last successful fetch time in the database
- `StockPriceEvent`: Simplified stock price event model for Kafka messages

### 4. Services
#### MarketStackClient
- WebClient-based HTTP client for MarketStack API
- Configurable symbols and limit parameters
- Returns reactive Mono<MarketStackResponse>

#### StockPriceService
- Scheduled service that:
  - Fetches stock prices from MarketStack
  - Processes and sends them to Kafka
  - Updates last fetch time in database
- Uses `@Scheduled` with configurable initial delay and fixed delay

## Prerequisites
- Java 17+
- Apache Kafka
- Zookeeper
- MarketStack API key
- PostgreSQL (or other JPA-compatible database)

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/victoradepoju/stock-fetcher.git
cd stock-fetcher
```

### 2. Build and run
```bash
./mvnw spring-boot:run
```

### 3. Verify
Check the application logs for messages like:
```
Fetching stock prices from MarketStack API
Sent stock price event for AAPL: StockPriceEvent(...)
```

## API Endpoints
While primarily a scheduled service, the application exposes the following endpoint:

- `GET /api/last-fetch` - Returns the timestamp of the last successful fetch

## Kafka Topics
The application produces messages to the configured topic (default: `stock-prices`). Each message contains:
- Key: Stock symbol (e.g., "AAPL")
- Value: `StockPriceEvent` object with price data

## Scheduling
The fetch operation runs on a fixed delay schedule configured by:
- `app.scheduler.initial-delay-ms`: Initial delay in milliseconds (default: 0)
- `app.scheduler.fixed-delay-ms`: Fixed delay between executions in milliseconds (default: 60000 = 1 minute)

## Error Handling
- Failed API requests are logged but don't stop subsequent executions
- Kafka send failures are handled by the Kafka producer (with 3 retries)

## Monitoring
The application logs:
- Start of each fetch operation
- Successfully sent stock price events
- Any errors during fetching or sending
