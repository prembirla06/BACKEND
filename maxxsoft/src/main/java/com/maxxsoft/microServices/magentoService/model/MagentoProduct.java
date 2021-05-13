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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maxxsoft.common.model.AuditModel;

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
@Table(name = "magentoProduct", schema = "public")
public class MagentoProduct extends AuditModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long magentoProductId;

	private String sku;

	@OneToMany(mappedBy = "magentoProduct", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<MagentoCategory> MagentoCategories = new HashSet();

	@JsonManagedReference
	@JsonIgnore
	@OneToMany(mappedBy = "magentoProduct", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<MagentoCustomAttributeValue> magentoCustomAttributes = new HashSet();

	@Override
	public int hashCode() {
		return Objects.hash(sku);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
