package com.pm.currencymonitor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pm.currencymonitor.shared.CurrencyPrice;

public interface CurrencyPriceServiceAsync {

	void getPrices(String[] symbols, AsyncCallback<CurrencyPrice[]> callback);



}
