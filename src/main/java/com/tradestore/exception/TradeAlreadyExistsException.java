package com.tradestore.exception;

public class TradeAlreadyExistsException extends RuntimeException {
    public TradeAlreadyExistsException(String message) {
        super(message);
    }
}
