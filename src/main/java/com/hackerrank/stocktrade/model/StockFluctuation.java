package com.hackerrank.stocktrade.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockFluctuation extends Stock {
    private Integer fluctuation;
    @JsonProperty("max_rise")
    private Float maxRise;
    @JsonProperty("max_fall")
    private Float maxFall;

    public StockFluctuation() {
    }

    public StockFluctuation(String stock, Integer fluctuation, Float maxRise, Float maxFall) {
        super(stock);
        this.fluctuation = fluctuation;
        this.maxRise = maxRise;
        this.maxFall = maxFall;
    }

    public Integer getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(Integer fluctuation) {
        this.fluctuation = fluctuation;
    }

    public Float getMaxRise() {
        return maxRise;
    }

    public void setMaxRise(Float maxRise) {
        this.maxRise = maxRise;
    }

    public Float getMaxFall() {
        return maxFall;
    }

    public void setMaxFall(Float maxFall) {
        this.maxFall = maxFall;
    }
}
