package com.pm.currencymonitor.shared;

import java.io.Serializable;

public class CurrencyPrice implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -5612392908751128243L;
	private String symbol;
	private double price;
	private double lastMonthPrice;

	public CurrencyPrice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CurrencyPrice(String symbol, double price, double lastMonthPrice) {
		super();
		this.symbol = symbol;
		this.price = price;
		this.lastMonthPrice = lastMonthPrice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getLastMonthPrice() {
		return lastMonthPrice;
	}

	public void setLastMonthPrice(double lastMonthPrice) {
		this.lastMonthPrice = lastMonthPrice;
	}

	public double getChangePercent() {
		return 100.0 * (this.price - this.lastMonthPrice) / this.lastMonthPrice;
	}

}
