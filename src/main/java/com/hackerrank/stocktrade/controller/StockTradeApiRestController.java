package com.hackerrank.stocktrade.controller;

import com.hackerrank.stocktrade.exceptions.NoSuchStockException;
import com.hackerrank.stocktrade.exceptions.NoTradesForDateException;
import com.hackerrank.stocktrade.exceptions.TradeIsAlreadyExistException;
import com.hackerrank.stocktrade.exceptions.UserDoesntExistException;
import com.hackerrank.stocktrade.model.HiLoStock;
import com.hackerrank.stocktrade.model.Trade;
import com.hackerrank.stocktrade.model.User;
import com.hackerrank.stocktrade.repository.TradeRepository;
import com.hackerrank.stocktrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hackerrank.stocktrade.Constant.STOCK_DATE_FORMAT;
import static com.hackerrank.stocktrade.Constant.TRADE_DATE_TIME_FORMAT;

@RestController
public class StockTradeApiRestController {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    UserRepository userRepository;

    @DeleteMapping("/erase")
    @ResponseStatus(HttpStatus.OK)
    void eraseAllTrades(){
        tradeRepository.deleteAll();
    }

    @PostMapping("/trades")
    @ResponseStatus(HttpStatus.CREATED)
    Trade newTrade(@RequestBody Trade trade){
        if(tradeRepository.exists(trade.getId())){
            throw new TradeIsAlreadyExistException();
        }
        if(!userRepository.exists(trade.getUser().getId())) {
            userRepository.save(trade.getUser());
        }

        return tradeRepository.save(trade);
    }

    @GetMapping("/trades")
    @ResponseStatus(HttpStatus.OK)
    List<Trade> getAllTrades(){
        return tradeRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("/trades/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    List<Trade> getTradesByUser(@PathVariable(value = "userID") long userId){
        if(!userRepository.exists(userId)){
            throw new UserDoesntExistException();
        }

        User user = userRepository.findOne(userId);

        return tradeRepository.findAllByUserIsOrderByIdAsc(user);
    }

    @GetMapping("/stocks/{stockSymbol}/price")
    @ResponseStatus(HttpStatus.OK)
    HiLoStock getHiLoPriceForStock(@PathVariable(value = "stockSymbol") String stockSymbol,
                                   //Didn't have a time to figure out why @DateTimeFormat doesnt work here
                                     @RequestParam(value = "start", required = false)  String startDate,
                                     @RequestParam(value = "end", required = false)  String endDate){


        List<Trade> trades = tradeRepository.findAllByStockSymbolIs(stockSymbol);
        if(trades.size() == 0) throw new NoSuchStockException();

        Timestamp start = getTimestamp(startDate);
        Timestamp end = getTimestamp(endDate);

        trades = trades.stream().filter(trade ->
                trade.getTradeTimestamp().before(end)
                && trade.getTradeTimestamp().after(start)
        ).collect(Collectors.toList());

        if(trades.size() == 0) {
            throw new NoTradesForDateException();
        }

        Float lowest = trades.stream().map(Trade::getStockPrice).max(Float::compareTo).get();
        Float highest = trades.stream().map(Trade::getStockPrice).min(Float::compareTo).get();

        return new HiLoStock(stockSymbol, highest, lowest);
    }

    private Timestamp getTimestamp(String startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(STOCK_DATE_FORMAT);
        Date date;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Timestamp(date.getTime());
    }
}
