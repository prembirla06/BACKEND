package com.maxxsoft.microServices.articleService.model.amazon_temp;


public class AmazonProductImage {

	public String sku = new String(); // Artikelnummer
	
	public double standardPrice; // Price item will be sold at
	public String imageType = new String(); // z.B. Main: Main image
											//Swatch: different colour variations (will be rescaled to 30�30 pixels)
											//Alternate (PT1-8): Secondary photos to be displayed alongside others
		
	public String imageLocation = new String(); // URL where image is located
	
	
	public AmazonProductImage() {
	}


	public AmazonProductImage(String sku, double standardPrice, String imageType, String mageLocation) {
		super();
		this.sku = sku;
		this.standardPrice = standardPrice;
		this.imageType = imageType;
		this.imageLocation = mageLocation;
	}


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public double getStandardPrice() {
		return standardPrice;
	}


	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}


	public String getImageType() {
		return imageType;
	}


	public void setImageType(String imageType) {
		this.imageType = imageType;
	}


	public String getImageLocation() {
		return imageLocation;
	}


	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	
	
}
