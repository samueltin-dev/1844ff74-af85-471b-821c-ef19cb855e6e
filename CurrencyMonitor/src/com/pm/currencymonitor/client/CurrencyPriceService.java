package com.pm.currencymonitor.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pm.currencymonitor.shared.CurrencyPrice;

@RemoteServiceRelativePath("currencyPrices")
public interface CurrencyPriceService extends RemoteService {
	
	CurrencyPrice[] getPrices(String[] symbols);

	

}
