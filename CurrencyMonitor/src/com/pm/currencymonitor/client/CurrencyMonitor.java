package com.pm.currencymonitor.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pm.currencymonitor.shared.CurrencyPrice;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CurrencyMonitor implements EntryPoint, CurrencyMonitorController {

	private static final String FAIL_TO_GET_CURRENCY_PRICE = "Fail to get Currency Price! Please try again later.";
	private static final String GET_CURRENCY_CODE_ERROR="Fail to get supported currency code from server! Please try again later.";
	private static final String GET_JSON_ERROR= "Encouter error while getting JSON";
	private static final String GET_CURRENCY_PRICE_ERROR = "Encouter error while getting currency price!";
	private static final int REFRESH_INTERVAL = 5000; // ms
	private static final String CCY_DATA_URL = GWT.getModuleBaseURL() + "currencyCode";
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private CurrencyTable ccyTable = createCurrencyTable();
	private ErrorPanel errorPanel = createErrorPanel();
	private ControlPanel controlPanel = createControlPanel();
	private StatusPanel statusPanel = createStatusPanel();

	private boolean isGetCurrencyCodeFailed = false;
	
	//private ArrayList<String> currencies = new ArrayList<String>();	
	private JsArray<CurrencyData> myCurrencyArray=null;

	private CurrencyPriceServiceAsync currencyPriceSvc = GWT.create(CurrencyPriceService.class);


	private Logger logger = Logger.getLogger(CurrencyMonitor.class.getName());

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
	    // Assemble Main panel.
	    mainPanel.add(errorPanel);
	    mainPanel.add(ccyTable);
	    mainPanel.add(controlPanel);
	    mainPanel.add(statusPanel);
	    

	    // Associate the Main panel with the HTML host page.
	    RootPanel.get("currencylist").add(mainPanel);
	    
        // Move cursor focus to the input box.
	    controlPanel.setFocusToSymbolTextBox(true);

		 // Setup timer to refresh list automatically.
	      Timer refreshTimer = new Timer() {
	        @Override
	        public void run() {
	          refreshWatchList();
	        }
	      };
	      refreshTimer.scheduleRepeating(REFRESH_INTERVAL);   

	}
	
	public void addCurrency(final String symbol) {
		
		controlPanel.setFocusToSymbolTextBox(true);

		// Currency Code must be 3 characters
		if (!symbol.matches("[A-Z][A-Z][A-Z]")) {
			Window.alert("'" + symbol + "' is not a valid symbol.");
			controlPanel.selectAllToSymbolTextBox();
			return;
		}
		
		// Don't add the currency if it's already in the table.
		if (ccyTable.containsSymbol(symbol)) {
			return;
		}


		//Retrieve list of supported currency code if it haven't been initialized
		//For demo purpose not all currency code is supported
		if (myCurrencyArray == null) {
			try {
				getCurrencyCodes();
			} catch (RequestException e) {
				displayCurrencyCodesRetrievalError();
				logger.log(Level.SEVERE, GET_JSON_ERROR, e);
			}
		}

		//A timer to wait until asynchronized call completed, then check if currency supported
		Timer pendingTimer = new Timer() {
			@Override
			public void run() {
				if (myCurrencyArray != null) {
					if (isCurrencySupported(myCurrencyArray, symbol)) {
						ccyTable.addCurrencyToUI(symbol);
						controlPanel.setTextFromSymbolTextBox("");
					} else {
						Window.alert("'" + symbol + "' is not supported!.");
						controlPanel.selectAllToSymbolTextBox();
					}
					this.cancel();
				}
			}
		};
		
		if (!isGetCurrencyCodeFailed) {
			pendingTimer.scheduleRepeating(500);
		}

	}


	public void refreshWatchList() {
		
	    if (currencyPriceSvc == null) {
	    	currencyPriceSvc = GWT.create(CurrencyPriceService.class);
	    }

	     // Set up the callback object.
	    AsyncCallback<CurrencyPrice[]> callback = new AsyncCallback<CurrencyPrice[]>() {
	      public void onFailure(Throwable e) {
	    	  errorPanel.displayError(FAIL_TO_GET_CURRENCY_PRICE);
	    	  logger.log(Level.SEVERE, GET_CURRENCY_PRICE_ERROR, e);
	      }

	      public void onSuccess(CurrencyPrice[] result) {
	    	errorPanel.hideError();
	        updateUIwithNewPrice(result);
	      }
	    };

	     // Make the call to the stock price service.
	    currencyPriceSvc.getPrices(ccyTable.getAddedCurrencyArray(), callback);

  }
	
	private void displayCurrencyCodesRetrievalError() {
		isGetCurrencyCodeFailed=true;
		errorPanel.displayError(GET_CURRENCY_CODE_ERROR);
	}
		

	public void updateUIwithNewPrice(CurrencyPrice[] prices) {
		ccyTable.updateTable(prices);

		// Display timestamp showing last refresh.
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
		statusPanel.setStatus("Last update : " + dateFormat.format(new Date()));
	}
    

    
	public void getCurrencyCodes() throws RequestException {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, CCY_DATA_URL);
		Request request =  builder.sendRequest(null, new RequestCallback() {
			public void onError(Request request, Throwable e) {
				displayCurrencyCodesRetrievalError();
				logger.log(Level.SEVERE, GET_JSON_ERROR, e);
				
			}

			public void onResponseReceived(Request request, Response response) {

				if (200 == response.getStatusCode()) {
					myCurrencyArray = JsonUtils.<JsArray<CurrencyData>> safeEval(response.getText());
					errorPanel.hideError();
				} else {
					displayCurrencyCodesRetrievalError();
					logger.log(Level.SEVERE, GET_JSON_ERROR+" response code="+response.getStatusCode());
				}
			}
		});

	}
	
	private boolean isCurrencySupported(JsArray<CurrencyData> currencyArray, String symbol) {
		boolean supported= false;
		for (int i = 0; i < currencyArray.length(); i++) {
			CurrencyData data = currencyArray.get(i);
			if (symbol != null && symbol.equals(data.getCurrencyCode())) {
				supported = true;
			}

		}
		return supported;
	}

	public ErrorPanel createErrorPanel() {
		return new ErrorPanel();
	}
	
	public StatusPanel createStatusPanel() {
		return new StatusPanel();
	}

	//Create Control Panel
	public ControlPanel createControlPanel() {
		return new ControlPanel(this);
	}


	public CurrencyTable createCurrencyTable() {
		return new CurrencyTable(this);
	}
	
}
