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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "artikel_stuecklist")
public class ArtikelStuecklist {

	@EmbeddedId
	private ArtikelStueckListID id;

	private String position;

	private double menge;

	private Date erstellt;

	@Column(name = "ERST_NAME")
	private String erstName;

	private Date geaend;

	@Column(name = "GEAEND_NAME")
	private String geaendName;

	public ArtikelStueckListID getArtikelStueckListID() {
		return id;
	}

	public void setArtikelStueckListID(ArtikelStueckListID artikelStueckListID) {
		this.id = artikelStueckListID;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getMenge() {
		return menge;
	}

	public void setMenge(double menge) {
		this.menge = menge;
	}

	public Date getErstellt() {
		return erstellt;
	}

	public void setErstellt(Date erstellt) {
		this.erstellt = erstellt;
	}

	public String getErstName() {
		return erstName;
	}

	public void setErstName(String erstName) {
		this.erstName = erstName;
	}

	public Date getGeaend() {
		return geaend;
	}

	public void setGeaend(Date geaend) {
		this.geaend = geaend;
	}

	public String getGeaendName() {
		return geaendName;
	}

	public void setGeaendName(String geaendName) {
		this.geaendName = geaendName;
	}

}
