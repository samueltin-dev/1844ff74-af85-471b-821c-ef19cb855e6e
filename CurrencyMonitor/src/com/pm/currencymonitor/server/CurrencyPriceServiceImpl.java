package com.pm.currencymonitor.server;

import java.util.Random;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pm.currencymonitor.client.CurrencyPriceService;
import com.pm.currencymonitor.service.historicalrate.HistoricalRateService;
import com.pm.currencymonitor.service.historicalrate.HistoricalRateServiceImpl;
import com.pm.currencymonitor.shared.CurrencyPrice;

public class CurrencyPriceServiceImpl extends RemoteServiceServlet implements CurrencyPriceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1039576114951551492L;
	private static final double MAX_PRICE = 100.0; // $100.00
	private HistoricalRateService rateService = createHistoricalRateService();

	/**
	 * Generate random price and retrieve last month price
	 */
	@Override
	public CurrencyPrice[] getPrices(String[] symbols) {

		Random rnd = new Random();

		CurrencyPrice[] prices = new CurrencyPrice[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			double price = rnd.nextDouble() * MAX_PRICE;
			double lastMonthPrice = getLastMonthPrice(symbols[i]);

			prices[i] = new CurrencyPrice(symbols[i], price, lastMonthPrice);
		}

		return prices;
	}

	private double getLastMonthPrice(String symbol) {
		return rateService.getLastMonthRate(symbol);
	}

	// For demo purpose use factory method rather than IOC
	private HistoricalRateService createHistoricalRateService() {
		return new HistoricalRateServiceImpl();
	}

}
