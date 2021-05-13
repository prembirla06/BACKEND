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
public class Check24 {

	String ean;
	String eindeutige_id;
	String lieferzeit;
	String preis;
	String versandkosten;
	String bestand;
	String hersteller;
	String kategorie;
	String link_produktseite;
	String produktname;
	String produktbeschreibung;

	String url_produktbild1;
	String url_produktbild2;
	String url_produktbild3;
	String url_produktbild4;
	String url_produktbild5;
	String url_produktbild6;
	String url_produktbild7;
	String url_produktbild8;

	String versandart;
}
