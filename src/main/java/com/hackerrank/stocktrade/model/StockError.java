package com.hackerrank.stocktrade.model;

public class StockError extends Stock{
    private String message;

    public StockError() {
    }

    public StockError(String stock, String message) {
        super(stock);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
