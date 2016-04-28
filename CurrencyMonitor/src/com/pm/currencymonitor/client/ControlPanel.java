package com.pm.currencymonitor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ControlPanel extends HorizontalPanel {
	
	private TextBox newSymbolTextBox = new TextBox();
	private Button addCcyButton = new Button("Add");
	private Button refreshButton = new Button("Refresh");
	private CurrencyMonitorController controller = null;
	public ControlPanel(final CurrencyMonitorController controller) {
		
		this.controller = controller;
		newSymbolTextBox.setMaxLength(3);
		
	    this.add(newSymbolTextBox);
	    this.add(addCcyButton);
	    this.add(refreshButton);
	    this.addStyleName("addPanel");
	    
	      // Listen for mouse events on the Add button.
	      addCcyButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
	          controller.addCurrency(symbol);
	        }
	      });

	      // Listen for keyboard events in the input box.
	      newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
	        public void onKeyDown(KeyDownEvent event) {
	          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	        	  final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
	        	  controller.addCurrency(symbol);
	          }
	        }
	      });
	      
	      refreshButton.addClickHandler(new ClickHandler() {
		        public void onClick(ClickEvent event) {
		        	controller.refreshWatchList();
		        }
		   });
	}
	
	public void setFocusToSymbolTextBox(boolean focus) {
		newSymbolTextBox.setFocus(true);
	}
	
	public String getTextFromSymbolTextBox() {
		return newSymbolTextBox.getText();
	}
	
	public void selectAllToSymbolTextBox() {
		newSymbolTextBox.selectAll();
	}
	
	public void setTextFromSymbolTextBox(String text) {
		newSymbolTextBox.setText(text);
	}
	
	
	

}
