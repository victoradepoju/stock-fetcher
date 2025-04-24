package com.group4.stock_fetcher.repository;

import com.group4.stock_fetcher.model.LastFetchTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastFetchTimeRepository extends JpaRepository<LastFetchTime, String> {
}
