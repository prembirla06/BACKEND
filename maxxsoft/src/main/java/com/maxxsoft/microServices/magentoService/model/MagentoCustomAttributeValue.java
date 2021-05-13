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
package com.maxxsoft.microServices.magentoService.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "magentoCustomAttributeValue", schema = "public")
public class MagentoCustomAttributeValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long magentoCustomAttributeValueId;

	private String attributeName;

	private String attributeValueName;

	@Column(length = 1000000)
	@Lob
	private String attributeValue;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "magentoProductId", nullable = false)
	private MagentoProduct magentoProduct;

	public MagentoCustomAttributeValue(String attributeName, String attributeValue, String attributeValueName,
			MagentoProduct magentoProduct) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.attributeValueName = attributeValueName;
		this.magentoProduct = magentoProduct;
	}

}
