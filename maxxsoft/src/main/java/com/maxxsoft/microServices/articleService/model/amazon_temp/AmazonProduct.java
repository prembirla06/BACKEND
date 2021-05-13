package com.maxxsoft.microServices.articleService.model.amazon_temp;

import java.util.Date;

public class AmazonProduct {

	public String sku; // Artikelnummer
	
	public String standardProductID;
	public Date launchDate;
	public String condition;
	
	// DescriptionData:
	public String title;
	public String brand;
	public String description;
	public String bulletPoint1;
	public String bulletPoint2;
	public String bulletPoint3;
	public String bulletPoint4;
	public String bulletPoint5;
	public String manufacturer;
	public String recommendedBrowseNode1;
	public String recommendedBrowseNode2;
	public String recommendedBrowseNode3;
	
	
	public AmazonProduct() {
	}
	

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getStandardProductID() {
		return standardProductID;
	}

	public void setStandardProductID(String standardProductID) {
		this.standardProductID = standardProductID;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBulletPoint1() {
		return bulletPoint1;
	}

	public void setBulletPoint1(String bulletPoint1) {
		this.bulletPoint1 = bulletPoint1;
	}

	public String getBulletPoint2() {
		return bulletPoint2;
	}

	public void setBulletPoint2(String bulletPoint2) {
		this.bulletPoint2 = bulletPoint2;
	}

	public String getBulletPoint3() {
		return bulletPoint3;
	}

	public void setBulletPoint3(String bulletPoint3) {
		this.bulletPoint3 = bulletPoint3;
	}

	public String getBulletPoint4() {
		return bulletPoint4;
	}

	public void setBulletPoint4(String bulletPoint4) {
		this.bulletPoint4 = bulletPoint4;
	}

	public String getBulletPoint5() {
		return bulletPoint5;
	}

	public void setBulletPoint5(String bulletPoint5) {
		this.bulletPoint5 = bulletPoint5;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getRecommendedBrowseNode1() {
		return recommendedBrowseNode1;
	}

	public void setRecommendedBrowseNode1(String recommendedBrowseNode1) {
		this.recommendedBrowseNode1 = recommendedBrowseNode1;
	}

	public String getRecommendedBrowseNode2() {
		return recommendedBrowseNode2;
	}

	public void setRecommendedBrowseNode2(String recommendedBrowseNode2) {
		this.recommendedBrowseNode2 = recommendedBrowseNode2;
	}


	public String getRecommendedBrowseNode3() {
		return recommendedBrowseNode3;
	}


	public void setRecommendedBrowseNode3(String recommendedBrowseNode3) {
		this.recommendedBrowseNode3 = recommendedBrowseNode3;
	}
	
	
	
	
	
}
