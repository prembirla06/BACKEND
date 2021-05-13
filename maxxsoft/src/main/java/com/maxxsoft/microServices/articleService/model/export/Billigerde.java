/*******************************************************
* Copyright (C) 2020, TecMaXX GmbH
* All Rights Reserved.
* 
* NOTICE: All information contained herein is, and remains
* the property of TecMaXX GmbH and its suppliers,
* if any. The intellectual and technical concepts contained
* herein are proprietary to TecMaXX GmbH
* and its suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from TecMaXX GmbH.
* 
* TecMaXX GmbH
* Auf der Suend 18, DE-91757 Treuchtlingen
*******************************************************/
package com.maxxsoft.microServices.articleService.model.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billigerde {

	String aid;
	String name;
	String price;
	String link;
	String shop_cat;
	String brand;
	String mpn;
	String gtin; // EAN
	String image;
	String dlv_time;
	String dlv_cost;
	String dlv_cost_at;
	String desc; // Beschreibung
	String old_price;
	String eek;
	String size;
	String color;
	String material;
	String billigerde_class;
	String features;
	String style;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getShop_cat() {
		return shop_cat;
	}

	public void setShop_cat(String shop_cat) {
		this.shop_cat = shop_cat;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDlv_time() {
		return dlv_time;
	}

	public void setDlv_time(String dlv_time) {
		this.dlv_time = dlv_time;
	}

	public String getDlv_cost() {
		return dlv_cost;
	}

	public void setDlv_cost(String dlv_cost) {
		this.dlv_cost = dlv_cost;
	}

	public String getDlv_cost_at() {
		return dlv_cost_at;
	}

	public void setDlv_cost_at(String dlv_cost_at) {
		this.dlv_cost_at = dlv_cost_at;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOld_price() {
		return old_price;
	}

	public void setOld_price(String old_price) {
		this.old_price = old_price;
	}

	public String getEek() {
		return eek;
	}

	public void setEek(String eek) {
		this.eek = eek;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getBilligerde_class() {
		return billigerde_class;
	}

	public void setBilligerde_class(String billigerde_class) {
		this.billigerde_class = billigerde_class;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
