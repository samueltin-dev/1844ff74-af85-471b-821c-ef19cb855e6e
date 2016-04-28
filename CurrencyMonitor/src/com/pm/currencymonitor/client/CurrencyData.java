package com.pm.currencymonitor.client;

import com.google.gwt.core.client.JavaScriptObject;

class CurrencyData extends JavaScriptObject {
	
	protected CurrencyData() {}
	
	public final native String getCurrencyCode()/*-{ return this.CurrencyCode; }-*/;
	public final native String getCurrencyName()/*-{ return this.CurrencyName; }-*/;
	

}
