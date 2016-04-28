package com.pm.currencymonitor.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.pm.currencymonitor.shared.CurrencyPrice;

public class CurrencyTable extends FlexTable {

	private CurrencyMonitorController controller = null;
	private ArrayList<String> currencies = new ArrayList<String>();

	public CurrencyTable(CurrencyMonitorController controller) {
		this.controller = controller;

		// Create table for currency data.
		this.setText(0, 0, "Symbol");
		this.setText(0, 1, "Price in HKD");
		this.setText(0, 2, "% Change");
		this.setText(0, 3, "Remove");

		// Add styles to elements in the currency list table.
		this.getRowFormatter().addStyleName(0, "watchListHeader");
		this.addStyleName("watchList");
		this.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
		this.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
		this.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
	}
	
	public boolean containsSymbol(String symbol) {
		return currencies.contains(symbol);
	}
	
	public void addCurrencyToUI(final String symbol) {
		// Add the stock to the table.
	      int row = this.getRowCount();
	      currencies.add(symbol);
	      this.setText(row, 0, symbol);
	      
	      this.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
	      this.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
	      this.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
	      
	   // Add a button to remove this stock from the table.
	      Button removeStockButton = new Button("x");
	      removeStockButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          int removedIndex = currencies.indexOf(symbol);
	          currencies.remove(removedIndex);
	          CurrencyTable.this.removeRow(removedIndex + 1);
	        }
	      });
	      this.setWidget(row, 3, removeStockButton);

	      controller.refreshWatchList();
	}
	
	public String[] getAddedCurrencyArray() {
		return currencies.toArray(new String[0]);
	}
	
	public void updateTable(CurrencyPrice price) {
		// Make sure the stock is still in the stock table.
		if (!currencies.contains(price.getSymbol())) {
			return;
		}

		int row = currencies.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
		String changePercentText = changeFormat.format(price.getChangePercent());

		// Populate the Price and Change fields with new data.
		this.setText(row, 1, priceText);
		this.setText(row, 2, changePercentText + "%");
	}
	
	public void updateTable(CurrencyPrice[] prices) {
		for (int i = 0; i < prices.length; i++) {
			this.updateTable(prices[i]);
		}

	}
    
	

}
