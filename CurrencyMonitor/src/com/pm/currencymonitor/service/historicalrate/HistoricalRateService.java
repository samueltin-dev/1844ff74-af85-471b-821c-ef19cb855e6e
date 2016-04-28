package com.pm.currencymonitor.service.historicalrate;

public interface HistoricalRateService {
	
	/**
	 * For demo purpose, there is only one month to retrieve last month currency rate
	 * 
	 */
	public double getLastMonthRate(String symbol);
	

}
