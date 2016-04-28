package com.pm.currencymonitor.client;

import com.google.gwt.http.client.RequestException;
import com.pm.currencymonitor.shared.CurrencyPrice;

public interface CurrencyMonitorController {
	
	public void addCurrency(String symbol);
	
	public void refreshWatchList();
	
	public void getCurrencyCodes() throws RequestException;
	
	public void updateUIwithNewPrice(CurrencyPrice[] prices) ;

}
