package com.maxxsoft.microServices.articleService.model.amazon_temp;

public class AmazonInventory {

	public String sku; // Artikelnummer
	
	public int quantity; // Menge
	
	public int fulfillmentLatency; // Bearbeitungszeit

	
	public AmazonInventory() {
	}

	public AmazonInventory(String sku, int quantity, int fulfillmentLatency) {
		super();
		this.sku = sku;
		this.quantity = quantity;
		this.fulfillmentLatency = fulfillmentLatency;
	}

	

	public String getSku() {
		return sku;
	}



	public void setSku(String sku) {
		this.sku = sku;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getFulfillmentLatency() {
		return fulfillmentLatency;
	}

	public void setFulfillmentLatency(int fulfillmentLatency) {
		this.fulfillmentLatency = fulfillmentLatency;
	}
	
	
	
	
	
}
