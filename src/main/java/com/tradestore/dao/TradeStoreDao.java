package com.tradestore.dao;

import com.tradestore.exception.TradeAlreadyExistsException;
import com.tradestore.model.Trade;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class TradeStoreDao {
    private static final Map<String, Trade> TRADE_MAP = new ConcurrentHashMap();

    public Trade getTradeById(String tradeId) {
        return TRADE_MAP.get(tradeId);
    }

    public Trade addTrade(Trade trade) {
        if (nonNull(trade.getTradeId())) {
            Trade existingTrade = TRADE_MAP.get(trade.getTradeId());
            if (isNull(existingTrade)) {
                TRADE_MAP.put(trade.getTradeId(), trade);
            } else {
                if (trade.getVersion() <= existingTrade.getVersion()) {
                    throw new TradeAlreadyExistsException("Trade already exists for tradeId: " + trade.getTradeId());
                } else {
                    TRADE_MAP.replace(trade.getTradeId(), trade);
                }
            }
            return trade;
        }

        return null;
    }
    
    public void updateTradeExpiryPostMaturityDate() {
		TRADE_MAP.entrySet().stream().filter(x -> x.getValue().getMaturityDate().compareTo(new Date()) < 0)
				.forEach(y -> y.getValue().setExpired('Y'));
	}
}
