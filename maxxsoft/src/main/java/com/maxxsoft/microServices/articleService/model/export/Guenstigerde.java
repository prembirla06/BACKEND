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
public class Guenstigerde {

	String bestellnummer;
	String herstellerArtNr;
	String hersteller;
	String produktBezeichnung;
	String produktBeschreibung;
	String preis;
	String lieferzeit;
	String produktLink;
	String fotoLink;
	String eanCode;
	String kategorie;
	// String gewicht;
	// String versandVorkasse;
	String versandPayPal;
	String versandKreditkarte;
	String versandLastschrift;
	String versandRechnung;
	// String versandNachnahme;
	// String grundpreis_komplett;
	String energieeffizienzklasse;
	// String keyword;
	String farbe;
	// String groesse;
	// String geschlecht;
	// String erwachsene_Kind;
	// String pzn;
	// String reifentyp;
	// String reifensaison;
	// String reifenmass;

}
