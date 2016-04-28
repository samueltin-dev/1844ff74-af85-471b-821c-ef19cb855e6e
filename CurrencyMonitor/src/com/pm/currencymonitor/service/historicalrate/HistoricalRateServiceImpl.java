package com.pm.currencymonitor.service.historicalrate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.ibm.icu.util.Calendar;

public class HistoricalRateServiceImpl implements HistoricalRateService {

	/**
	 * rateTable
	 */
	private Hashtable<String, Double> rateTable = new Hashtable<String, Double>();
	/**
	 * completed loading rate table or not 
	 */
	private volatile boolean completed = false;
	private Logger logger = Logger.getLogger(HistoricalRateServiceImpl.class.getName());

	@Override
	public double getLastMonthRate(String symbol) {
		initRateTable();
		Double price = rateTable.get(symbol);

		if (price == null) {
			price = 0.0;
		}
		return price;
	}

	/**
	 * initial rate table
	 */
	private void initRateTable() {
		synchronized (rateTable) {
			if (!completed) {
				retrieveLastMonthRate();
			}
		}
	}

	/**
	 * retrieve last month currency rate from file and load entries into rateTable (Hashtable)
	 */
	private void retrieveLastMonthRate() {

		String filename = getHistoricalRateFileName();

		synchronized (rateTable) {
			if (completed) {
				return;
			}

			try {
				InputStream fis = new FileInputStream(filename);
				JsonReader jsonReader = Json.createReader(fis);
				JsonArray jsonArray = jsonReader.readArray();

				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObj = jsonArray.getJsonObject(i);
					String symbol = jsonObj.getString("currencyCode");
					Double price = parseDouble(jsonObj);
					if (price != null) {
						rateTable.put(symbol, price);
					}

				}

			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "File to read historical rate file" + filename, e);
			}
			completed = true;
		}
	}

	/**
	 * Get last month currency rate file name
	 * @return
	 */
	private String getHistoricalRateFileName() {
		return "./historicalrate_" + getlastDayofLastMonth() + ".json";
	}

	
	/**
	 * 
	 * @return last day of last month in yyyyMMdd format
	 */
	private String getlastDayofLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		return sdf.format(calendar.getTime());
	}

	/**
	 * Convert jsonObj hkdPerUnit into Double
	 * @param jsonObj
	 * @return double value of hkdPerUnit
	 */
	private Double parseDouble(JsonObject jsonObj) {
		Double price = null;

		try {
			price = new Double(jsonObj.getString("hkdPerUnit"));
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Fail to convert to double value", e);
		}

		return price;
	}

}
