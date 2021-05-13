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
package com.maxxsoft.microServices.articleService.model.request;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "packstueck")
@XmlType(propOrder = { "breite", "hoehe", "tiefe", "gewicht" })
public class Packstueck {

	private float breite; // length

	private float hoehe; // height

	private float tiefe; // width

	private float gewicht; // weight

	public Packstueck() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Packstueck(float breite, float hoehe, float tiefe, float gewicht) {
		super();
		this.breite = breite;
		this.hoehe = hoehe;
		this.tiefe = tiefe;
		this.gewicht = gewicht;
	}

	@XmlAttribute
	public float getBreite() {
		return breite;
	}

	public void setBreite(float breite) {
		this.breite = breite;
	}

	@XmlAttribute
	public float getHoehe() {
		return hoehe;
	}

	public void setHoehe(float hoehe) {
		this.hoehe = hoehe;
	}

	@XmlAttribute
	public float getTiefe() {
		return tiefe;
	}

	public void setTiefe(float tiefe) {
		this.tiefe = tiefe;
	}

	@XmlAttribute
	public float getGewicht() {
		return gewicht;
	}

	public void setGewicht(float gewicht) {
		this.gewicht = gewicht;
	}

}
