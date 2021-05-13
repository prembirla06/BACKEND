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

import com.maxxsoft.microServices.articleService.model.request.Verpackungsdaten;

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
public class wayfair {

	// Required fields:
	String herstellerArtikenummer;
	String lieferantArtikelnummer;
	String produktname;
	String grosshandelsPreis;
	String mindestBestellmenge;
	String vervielfachungsfaktor;
	String setgroesse;
	String produktgewicht;
	String produkthoehe;
	String produktweite;
	String produkttiefe;
	String option1;
	String option1Moeglichkeiten;
	String produktbeschreibung;
	String produktEigenschaft1;
	String produktEigenschaft2;
	String produktEigenschaft3;
	String herkunftsland;
	String versandvorlaufzeit;
	String versandvorlaufzeitErsatzteile;
	String anzahlVerpackungen;

	Verpackungsdaten verpackungsdaten;

	String bild1Name; // URL zum Bild

	// Optional fields:
	String eanNummer;
	String wirdPalettiertVerschickt; // J oder N;
	String bild2Name; // URL zum Bild
	String bild3Name; // URL zum Bild
	String bild4Name; // URL zum Bild
	String bild5Name; // URL zum Bild

}
