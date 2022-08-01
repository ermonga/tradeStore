package com.tradestore.scheduler;

import java.util.Timer;
import java.util.TimerTask;

import com.tradestore.dao.TradeStoreDao;

public class TradeExpiryChecker {
	
	private TradeStoreDao tradeStoreDao = new TradeStoreDao();
	private Timer timer = new Timer();

	public TradeExpiryChecker() {
	}
	
	public void updateTradeExpirySchedule() {
		TimerTask scheduleTask = new TimerTask() {
			@Override
			public void run() {
				tradeStoreDao.updateTradeExpiryPostMaturityDate();
			}
		};
		// Every 24 hours
		// timer.schedule (scheduleTask, 0l, 1000*60*60*24);
		// Every second to test
		timer.schedule(scheduleTask, 0l, 1000);
	}
	
	public void closeTimer() {
		timer.cancel();
	}
}
