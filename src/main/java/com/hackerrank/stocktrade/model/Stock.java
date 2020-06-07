package com.hackerrank.stocktrade.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock {
    @JsonProperty("symbol")
    private String stock;

    public Stock() {
    }

    public Stock(String stock) {
        this.stock = stock;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
