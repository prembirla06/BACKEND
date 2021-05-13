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
public class Schottenland {

	String aan;
	String produktname;
	String produktpreis;
	String deeplink;
	String deeplink_mobile;
	String ean;
	String hersteller;
	String lieferzeit;

	String versandkosten_vorkasse;
	String versandkosten_kreditkarte;
	String versandkosten_lastschrift;
	String versandkosten_rechnung;
	String versandkosten_paypal;

	String energieeffizienzklasse;

	String produktbild;
	String han; // Herstellerartikelnummer
	String artikelbeschreibung;
	String warenkorb_link;
	String warenkorb_link_mobile;
	String kategorie;
}
