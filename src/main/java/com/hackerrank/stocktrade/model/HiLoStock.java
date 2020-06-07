package com.hackerrank.stocktrade.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HiLoStock {
    private String symbol;
    @JsonProperty("highest")
    private Float highestPrice;
    @JsonProperty("lowest")
    private Float lowestPrice;

    public HiLoStock() {
    }

    public HiLoStock(String symbol, Float highestPrice, Float lowestPrice) {
        this.symbol = symbol;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public Float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }
}
