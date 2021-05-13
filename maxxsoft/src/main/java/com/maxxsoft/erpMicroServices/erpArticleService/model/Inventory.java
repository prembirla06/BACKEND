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
package com.maxxsoft.erpMicroServices.erpArticleService.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Maxxsoft_Inventory")
public class Inventory {

	@Id
	@Column(name = "REC_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recId;

	@Column(name = "MENGE_AKT")
	private BigDecimal mengeAkt;

	private String location;

	public Integer getRecId() {
		return recId;
	}

	public Inventory() {
	}

	public Inventory(BigDecimal mengeAkt, String location) {
		this.mengeAkt = mengeAkt;
		this.location = location;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public BigDecimal getMengeAkt() {
		return mengeAkt;
	}

	public void setMengeAkt(BigDecimal mengeAkt) {
		this.mengeAkt = mengeAkt;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
