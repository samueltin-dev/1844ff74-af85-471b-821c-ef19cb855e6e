package com.pm.currencymonitor.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ErrorPanel extends HorizontalPanel {
	
	private Label errorMsgLabel = new Label();
	
	public ErrorPanel() {
		this.add(errorMsgLabel);
		errorMsgLabel.setStyleName("errorMessage");
	}

	public void displayError(String error) {
		 errorMsgLabel.setText("Error: " + error);
		 errorMsgLabel.setVisible(true);
	}
	
	public void hideError() {
		errorMsgLabel.setText("");
		errorMsgLabel.setVisible(false);
	}
	
}
