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
package com.maxxsoft.microServices.articleService.model;

import java.util.HashSet;
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
import com.maxxsoft.common.model.AuditModel;
import com.maxxsoft.microServices.articleService.model.request.PacketBarcodeRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "packet", schema = "public")
public class Packet extends AuditModel {

	public Packet(float gewicht, float hoehe, float breite, float tiefe, String number, Long articleId) {
		this.articleId = articleId;
		this.height = hoehe;
		this.lenght = breite;
		this.weight = gewicht;
		this.width = tiefe;
		this.number = number;
	}

	public Packet(double gewicht, double hoehe, double breite, double tiefe, String number, Long articleId) {
		this.articleId = articleId;
		this.height = hoehe;
		this.lenght = breite;
		this.weight = gewicht;
		this.width = tiefe;
		this.number = number;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long packetId;

	private double weight;

	private double height;

	private double lenght;

	private double width;

	private String number;

	private Long articleId;

	@JsonIgnore
	@OneToMany(mappedBy = "packet", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<PacketBarcode> PacketBarcodes = new HashSet();

	public void addBarcode(PacketBarcodeRequest packetBarcodeRequest) {
		PacketBarcode packetBarcode = new PacketBarcode(packetBarcodeRequest.getBarcode(), this);
		PacketBarcodes.add(packetBarcode);
	}

}
