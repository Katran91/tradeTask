package com.hackerrank.stocktrade.handler;

import com.hackerrank.stocktrade.Constant;
import com.hackerrank.stocktrade.exceptions.NoTradesForDateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class StockTradesErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoTradesForDateException.class})
    public ResponseEntity<Object> handleNoTradeException(NoTradesForDateException ex, WebRequest request){
        ErrorMessage message = new ErrorMessage(Constant.NO_TRADES_FOR_DATE_RANGE);
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.OK, request);
    }
}
