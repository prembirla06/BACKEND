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

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "maxxsoft_packaging_barcode")
public class PackagingBarcode {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "value")
	private String value;

	@Column(name = "artikel_packaging_id")
	private Integer packagingInformation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;

	}

	public Integer getPackagingInformation() {
		return packagingInformation;
	}

	public void setPackagingInformation(Integer packagingInformation) {
		this.packagingInformation = packagingInformation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PackagingBarcode that = (PackagingBarcode) o;
		return Objects.equals(id, that.id) && Objects.equals(value, that.value)
				&& Objects.equals(packagingInformation, that.packagingInformation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, value, packagingInformation);
	}
}
