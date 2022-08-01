package com.tradestore.dao;

import com.tradestore.dao.TradeStoreDao;
import com.tradestore.exception.TradeAlreadyExistsException;
import com.tradestore.model.Trade;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TradeStoreDaoTest {

	private TradeStoreDao tradeStoreDao;

	@Before
	public void init() {
		tradeStoreDao = new TradeStoreDao();
	}

	@Test
	public void tradeStoreDaoConstructor() {
		assertNotNull(new TradeStoreDao());
	}

	@Test
	public void addTrade() {
		assertNull(new TradeStoreDao().addTrade(new Trade()));
	}

	@Test
	public void addTradeWhenTradeNotAdded() {
		assertNull(new TradeStoreDao().addTrade(new Trade()));
	}

	@Test
	public void addTradeWhenTradeAddedToStore() {
		Trade trade = new Trade();
		trade.setTradeId("T1");
		assertNotNull(tradeStoreDao.addTrade(trade));
	}

	@Test
	public void getTradeByIdWhenTradeNotExists() {
		assertNull(tradeStoreDao.getTradeById("T1"));
	}

	@Test
	public void getTradeByIdWhenTradeExistsForProvidedTradeId() {
		Trade trade = new Trade();
		trade.setTradeId("T2");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("BK-1");
		trade.setExpired('N');
		trade.setCreatedDate(new Date());
		trade.setMaturityDate(new Date());
		trade.setVersion(1);
		tradeStoreDao.addTrade(trade);

		Trade t2 = tradeStoreDao.getTradeById("T2");
		assertEquals("T2", t2.getTradeId());
		assertEquals(1, t2.getVersion());
		assertEquals("BK-1", t2.getBookId());
		assertEquals("CP-1", t2.getCounterPartyId());
		assertNotNull(t2.getCreatedDate());
		assertNotNull(t2.getMaturityDate());
		assertEquals('N', t2.getExpired());
	}

	@Test
	public void addTradeWhenTradeIsAvailableInStoreWithOldVersion() {
		Trade trade = new Trade();
		trade.setTradeId("T3");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("BK-1");
		trade.setExpired('N');
		trade.setCreatedDate(new Date());
		trade.setMaturityDate(new Date());
		trade.setVersion(1);

		Trade t2 = tradeStoreDao.addTrade(trade);

		assertEquals("T3", t2.getTradeId());
		assertEquals(1, t2.getVersion());
		assertEquals("BK-1", t2.getBookId());
		assertEquals("CP-1", t2.getCounterPartyId());
		assertNotNull(t2.getCreatedDate());
		assertNotNull(t2.getMaturityDate());
		assertEquals('N', t2.getExpired());
	}

	@Test(expected = TradeAlreadyExistsException.class)
	public void addTradeShouldThrowExceptionWhenTradeIsAvailableInStoreWithSameOrOlderVersion() {
		Trade trade = new Trade();
		trade.setTradeId("T4");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("BK-1");
		trade.setExpired('N');
		trade.setCreatedDate(new Date());
		trade.setMaturityDate(new Date());
		trade.setVersion(1);

		tradeStoreDao.addTrade(trade);

		Trade trade2 = new Trade();
		trade2.setTradeId("T4");
		trade2.setCounterPartyId("CP-1");
		trade2.setBookId("BK-1");
		trade2.setExpired('N');
		trade2.setCreatedDate(new Date());
		trade2.setMaturityDate(new Date());
		trade2.setVersion(1);

		tradeStoreDao.addTrade(trade2);
	}

	@Test
	public void addTradeShouldNotThrowExceptionWhenTradeIsAvailableInStoreWithOlderVersion() {
		Trade trade = new Trade();
		trade.setTradeId("T5");
		trade.setCounterPartyId("CP-1");
		trade.setBookId("BK-1");
		trade.setExpired('N');
		trade.setCreatedDate(new Date());
		trade.setMaturityDate(new Date());
		trade.setVersion(1);

		tradeStoreDao.addTrade(trade);

		Trade trade2 = new Trade();
		trade2.setTradeId("T5");
		trade2.setCounterPartyId("CP-1");
		trade2.setBookId("BK-1");
		trade2.setExpired('N');
		trade2.setCreatedDate(new Date());
		trade2.setMaturityDate(new Date());
		trade2.setVersion(2);

		Trade trade1 = tradeStoreDao.addTrade(trade2);

		assertNotNull(trade1);
		assertEquals("T5", trade1.getTradeId());
		assertEquals(2, trade1.getVersion());
		assertEquals("BK-1", trade1.getBookId());
		assertEquals("CP-1", trade1.getCounterPartyId());
		assertNotNull(trade1.getCreatedDate());
		assertNotNull(trade1.getMaturityDate());
		assertEquals('N', trade1.getExpired());
	}

	@Test
	public void updateTradeExpiryPostMaturityDate() {

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
		tradeStoreDao.updateTradeExpiryPostMaturityDate();

		Trade tradeVal = tradeStoreDao.getTradeById("T6");

		assertEquals('Y', tradeVal.getExpired());
	}

}
