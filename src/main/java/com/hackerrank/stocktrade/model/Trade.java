package com.hackerrank.stocktrade.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;

import static com.hackerrank.stocktrade.Constant.TRADE_DATE_TIME_FORMAT;

@Entity
public class Trade {
    private @Id Long id;
    private String type;
    @ManyToOne
    private User user;
    @JsonProperty("symbol")
    private String stockSymbol;
    private Integer shares;
    @JsonProperty("price")
    private Float stockPrice;
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= TRADE_DATE_TIME_FORMAT)
    private Timestamp tradeTimestamp;

    public Trade() {
    }

    public Trade(Long id, String type, User user, String stockSymbol, Integer shares, Float stockPrice, Timestamp tradeTimestamp) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.stockSymbol = stockSymbol;
        this.shares = shares;
        this.stockPrice = stockPrice;
        this.tradeTimestamp = tradeTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer stockQuantity) {
        this.shares = stockQuantity;
    }

    public Float getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Float stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Timestamp getTradeTimestamp() {
        return tradeTimestamp;
    }

    public void setTradeTimestamp(Timestamp tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }
}
