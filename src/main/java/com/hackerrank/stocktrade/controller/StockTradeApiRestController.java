package com.hackerrank.stocktrade.controller;

import com.hackerrank.stocktrade.exceptions.NoSuchStockException;
import com.hackerrank.stocktrade.exceptions.NoTradesForDateException;
import com.hackerrank.stocktrade.exceptions.TradeIsAlreadyExistException;
import com.hackerrank.stocktrade.exceptions.UserDoesntExistException;
import com.hackerrank.stocktrade.model.*;
import com.hackerrank.stocktrade.repository.TradeRepository;
import com.hackerrank.stocktrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hackerrank.stocktrade.Constant.*;

@RestController
public class StockTradeApiRestController {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    UserRepository userRepository;

    @DeleteMapping("/erase")
    @ResponseStatus(HttpStatus.OK)
    void eraseAllTrades() {
        tradeRepository.deleteAll();
    }

    @PostMapping("/trades")
    @ResponseStatus(HttpStatus.CREATED)
    Trade newTrade(@RequestBody Trade trade) {
        if (tradeRepository.exists(trade.getId())) {
            throw new TradeIsAlreadyExistException();
        }
        if (!userRepository.exists(trade.getUser().getId())) {
            userRepository.save(trade.getUser());
        }

        return tradeRepository.save(trade);
    }

    @GetMapping("/trades")
    @ResponseStatus(HttpStatus.OK)
    List<Trade> getAllTrades() {
        return tradeRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("/trades/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    List<Trade> getTradesByUser(@PathVariable(value = "userID") long userId) {
        if (!userRepository.exists(userId)) {
            throw new UserDoesntExistException();
        }

        User user = userRepository.findOne(userId);

        return tradeRepository.findAllByUserIsOrderByIdAsc(user);
    }

    @GetMapping("/stocks/{stockSymbol}/price")
    @ResponseStatus(HttpStatus.OK)
    HiLoStock getHiLoPriceForStock(@PathVariable(value = "stockSymbol") String stockSymbol,
                                   //Didn't have a time to figure out why @DateTimeFormat doesnt work here
                                   @RequestParam(value = "start", required = false) String startDate,
                                   @RequestParam(value = "end", required = false) String endDate) {


        List<Trade> trades = tradeRepository.findAllByStockSymbolIs(stockSymbol);
        if (trades.size() == 0) throw new NoSuchStockException();

        Timestamp start = getTimestamp(startDate  + " 00:00:00");
        Timestamp end = getTimestamp(endDate  + " 23:59:59");

        trades = trades.stream().filter(trade ->
                trade.getTradeTimestamp().before(end)
                        && trade.getTradeTimestamp().after(start)
        ).collect(Collectors.toList());

        if (trades.size() == 0) {
            throw new NoTradesForDateException();
        }

        Float lowest = trades.stream().map(Trade::getStockPrice).max(Float::compareTo).get();
        Float highest = trades.stream().map(Trade::getStockPrice).min(Float::compareTo).get();

        return new HiLoStock(stockSymbol, highest, lowest);
    }

    @GetMapping("/stocks/stats")
    @ResponseStatus(HttpStatus.OK)
    List<Stock> getHiLoPriceForStock(//Didn't have a time to figure out why @DateTimeFormat doesnt work here
                                     @RequestParam(value = "start", required = false) String startDate,
                                     @RequestParam(value = "end", required = false) String endDate) {
        Timestamp start = getTimestamp(startDate + " 00:00:00");
        Timestamp end = getTimestamp(endDate + " 23:59:59");

        List<Trade> trades = tradeRepository.findAll();
        Map<String, List<Trade>> tradesByStock = trades.stream().collect(Collectors.groupingBy(Trade::getStockSymbol));

        List<Stock> stocks = new ArrayList<>();
        for (Map.Entry<String, List<Trade>> entry : tradesByStock.entrySet()) {
            String stock = entry.getKey();

            trades = entry.getValue().stream().filter(trade ->
                    trade.getTradeTimestamp().before(end)
                            && trade.getTradeTimestamp().after(start)
            ).sorted((Comparator.comparing(Trade::getTradeTimestamp))).collect(Collectors.toList());

            if (trades.size() == 0) {
                stocks.add(new StockError(stock, NO_TRADES_FOR_DATE_RANGE));
            } else {
                stocks.add(getStockFluctuation(trades, stock));
            }
        }

        return stocks.stream().sorted(Comparator.comparing(Stock::getStock)).collect(Collectors.toList());
    }

    private Stock getStockFluctuation(List<Trade> trades, String stock) {
        List<Float> prices = trades.stream().map(Trade::getStockPrice).collect(Collectors.toList());

        float maxRise = 0;
        float maxFall = 0;
        if (prices.size() >= 2) {
            for (int i = 0; i < prices.size() - 1; i++) {
                Float first = prices.get(i);
                Float second = prices.get(i + 1);
                float diff = Math.abs(first - second);
                if (first > second) {
                    maxFall = Math.max(maxFall, diff);
                } else if (first < second) {
                    maxRise = Math.max(maxRise, diff);
                }
            }
        }

        return new StockFluctuation(stock, getFluctuation(prices), maxRise, maxFall);
    }

    Integer getFluctuation(List<Float> prices) {
        if (prices.size() < 3) return 0;
        int fluctuation = 0;
        for (int i = 1; i < prices.size() - 1; i++) {
            if (prices.get(i - 1) < prices.get(i) &
                    prices.get(i) > prices.get(i + 1)) {
                fluctuation++;
            } else if (prices.get(i - 1) > prices.get(i) &
                    prices.get(i) < prices.get(i + 1)) {
                fluctuation++;
            }
        }
        return fluctuation;
    }

    private Timestamp getTimestamp(String startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TRADE_DATE_TIME_FORMAT);
        Date date;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Timestamp(date.getTime());
    }
}
