package com.hackerrank.stocktrade.repository;

import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    public List<Trade> findAllByOrderByIdAsc();

    public List<Trade> findAllByUserIsOrderByIdAsc(User user);
    public List<Trade> findAllByTradeTimestampBetween(Timestamp startDate, Timestamp endDate);
}
