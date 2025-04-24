package com.group4.stock_fetcher.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class LastFetchTime {
    @Id
    private String id = "last_fetch";
    private LocalDateTime lastFetchTime;
}
