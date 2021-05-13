package com.maxxsoft.microServices.articleService.model.amazon_temp;

import java.util.Date;

public class AmazonPrice {

	public String sku = new String(); // Artikelnummer
	
	public double standardPrice; // Price item will be sold at
	public String currencyPrice = new String(); // z.B. EUR
	
	
	// SALE (Optional)
	boolean sale = false;
	
	public Date startDate; // Date long format
	public Date endDate; // Date long format
	public double salePrice; // Sale price
	public String currencySalePrice = new String(); // z.B. EUR
	
	
	public AmazonPrice() {
	}
	
	public AmazonPrice(String sku, double standardPrice, String currencyPrice) {
		super();
		this.sku = sku;
		this.standardPrice = standardPrice;
		this.currencyPrice = currencyPrice;
	}
	
	public AmazonPrice(String sku, double standardPrice, String currencyPrice, boolean sale, Date startDate,
			Date endDate, double salePrice, String currencySalePrice) {
		super();
		this.sku = sku;
		this.standardPrice = standardPrice;
		this.currencyPrice = currencyPrice;
		this.sale = sale;
		this.startDate = startDate;
		this.endDate = endDate;
		this.salePrice = salePrice;
		this.currencySalePrice = currencySalePrice;
	}


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public String getStandardPrice() {
		return String.valueOf(standardPrice).replace(',', '.');
	}


	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}


	public String getCurrencyPrice() {
		return currencyPrice;
	}


	public void setCurrencyPrice(String currencyPrice) {
		this.currencyPrice = currencyPrice;
	}


	public boolean isSale() {
		return sale;
	}


	public void setSale(boolean sale) {
		this.sale = sale;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public double getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}


	public String getCurrencySalePrice() {
		return currencySalePrice;
	}


	public void setCurrencySalePrice(String currencySalePrice) {
		this.currencySalePrice = currencySalePrice;
	}
	
	
	
	
}
