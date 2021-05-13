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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "verpackungsdaten")
@XmlAccessorType(XmlAccessType.FIELD)
public class Verpackungsdaten {

	@XmlElement(name = "packstueck")
	private List<Packstueck> packstuecke = new ArrayList<Packstueck>();

	public List<Packstueck> getPackstuecke() {
		return packstuecke;
	}

	public void setPackstuecke(List<Packstueck> packstuecke) {
		this.packstuecke = packstuecke;
	}

}
