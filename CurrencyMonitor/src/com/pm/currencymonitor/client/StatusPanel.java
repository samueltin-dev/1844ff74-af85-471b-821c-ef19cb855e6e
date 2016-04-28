package com.pm.currencymonitor.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class StatusPanel extends HorizontalPanel {
	private Label lastUpdatedLabel = new Label();
	
	public StatusPanel() {
		this.add(lastUpdatedLabel);
	}
	
	public void setStatus(String status) {
		lastUpdatedLabel.setText(status);
	}

}
