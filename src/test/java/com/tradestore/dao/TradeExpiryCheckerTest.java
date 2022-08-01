package com.tradestore.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.tradestore.model.Trade;
import com.tradestore.scheduler.TradeExpiryChecker;

public class TradeExpiryCheckerTest {

	private TradeStoreDao tradeStoreDao;
	private TradeExpiryChecker tradeExpiryChecker;

	@Before
	public void init() {
		tradeStoreDao = new TradeStoreDao();
		tradeExpiryChecker = new TradeExpiryChecker();
	}
	
	@Test
	public void updateTradeExpirySchedule() {
		// date before todays date by one day
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, -1);
		dt = c.getTime();

		Trade trade = new Trade();
		trade.setTradeId("T6");
		trade.setCounterPartyId("CP-2");
		trade.setBookId("BK-2");
		trade.setExpired('N');
		trade.setCreatedDate(new Date());
		trade.setMaturityDate(dt);
		trade.setVersion(1);

		tradeStoreDao.addTrade(trade);

		// Call Scheduler
		tradeExpiryChecker.updateTradeExpirySchedule();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Trade tradeVal = tradeStoreDao.getTradeById("T6");
		char flag = tradeVal.getExpired();

		assertEquals('Y', flag);

		tradeExpiryChecker.closeTimer();
	}
}
