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
public class Moebelde {

	String artNr;
	String artName;
	String artBeschreibung;
	String artMenge;
	String artUrl;
	String artImgUrl;
	String artPreis;
	String artWaehrung;
	String artLieferkosten;
	String artLieferzeit;
	String artLieferzeitWert;
	String artVersandAt;
	String artVerfuegbarkeit;
	String artMarke;
	String artKategorie;
	String artImgUrl2;
	String artImgUrl3;
	String artImgUrl4;

	String artEan;
	String artFarbe;
	String artHauptfarbe;
	String artStil;
	String artExtras;
	String artBreite;
	String artBreiteEinheit;
	String artTiefe;
	String artTiefeEinheit;
	String artHoehe;
	String artHoeheEinheit;

	String artStreichpreis;

	public String getArtStreichpreis() {
		return artStreichpreis;
	}

	public void setArtStreichpreis(String artStreichpreis) {
		this.artStreichpreis = artStreichpreis;
	}

	public String getArtEan() {
		return artEan;
	}

	public void setArtEan(String artEan) {
		this.artEan = artEan;
	}

	public String getArtFarbe() {
		return artFarbe;
	}

	public void setArtFarbe(String artFarbe) {
		this.artFarbe = artFarbe;
	}

	public String getArtHauptfarbe() {
		return artHauptfarbe;
	}

	public void setArtHauptfarbe(String artHauptfarbe) {
		this.artHauptfarbe = artHauptfarbe;
	}

	public String getArtStil() {
		return artStil;
	}

	public void setArtStil(String artStil) {
		this.artStil = artStil;
	}

	public String getArtExtras() {
		return artExtras;
	}

	public void setArtExtras(String artExtras) {
		this.artExtras = artExtras;
	}

	public String getArtBreite() {
		return artBreite;
	}

	public void setArtBreite(String artBreite) {
		this.artBreite = artBreite;
	}

	public String getArtBreiteEinheit() {
		return artBreiteEinheit;
	}

	public void setArtBreiteEinheit(String artBreiteEinheit) {
		this.artBreiteEinheit = artBreiteEinheit;
	}

	public String getArtTiefe() {
		return artTiefe;
	}

	public void setArtTiefe(String artTiefe) {
		this.artTiefe = artTiefe;
	}

	public String getArtTiefeEinheit() {
		return artTiefeEinheit;
	}

	public void setArtTiefeEinheit(String artTiefeEinheit) {
		this.artTiefeEinheit = artTiefeEinheit;
	}

	public String getArtHoehe() {
		return artHoehe;
	}

	public void setArtHoehe(String artHoehe) {
		this.artHoehe = artHoehe;
	}

	public String getArtHoeheEinheit() {
		return artHoeheEinheit;
	}

	public void setArtHoeheEinheit(String artHoeheEinheit) {
		this.artHoeheEinheit = artHoeheEinheit;
	}

	public String getArtNr() {
		return artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public String getArtName() {
		return artName;
	}

	public void setArtName(String artName) {
		this.artName = artName;
	}

	public String getArtBeschreibung() {
		return artBeschreibung;
	}

	public void setArtBeschreibung(String artBeschreibung) {
		this.artBeschreibung = artBeschreibung;
	}

	public String getArtMenge() {
		return artMenge;
	}

	public void setArtMenge(String artMenge) {
		this.artMenge = artMenge;
	}

	public String getArtUrl() {
		return artUrl;
	}

	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}

	public String getArtImgUrl() {
		return artImgUrl;
	}

	public void setArtImgUrl(String artImgUrl) {
		this.artImgUrl = artImgUrl;
	}

	public String getArtPreis() {
		return artPreis;
	}

	public void setArtPreis(String artPreis) {
		this.artPreis = artPreis;
	}

	public String getArtWaehrung() {
		return artWaehrung;
	}

	public void setArtWaehrung(String artWaehrung) {
		this.artWaehrung = artWaehrung;
	}

	public String getArtLieferkosten() {
		return artLieferkosten;
	}

	public void setArtLieferkosten(String artLieferkosten) {
		this.artLieferkosten = artLieferkosten;
	}

	public String getArtLieferzeit() {
		return artLieferzeit;
	}

	public void setArtLieferzeit(String artLieferzeit) {
		this.artLieferzeit = artLieferzeit;
	}

	public String getArtLieferzeitWert() {
		return artLieferzeitWert;
	}

	public void setArtLieferzeitWert(String artLieferzeitWert) {
		this.artLieferzeitWert = artLieferzeitWert;
	}

	public String getArtVersandAt() {
		return artVersandAt;
	}

	public void setArtVersandAt(String artVersandAt) {
		this.artVersandAt = artVersandAt;
	}

	public String getArtVerfuegbarkeit() {
		return artVerfuegbarkeit;
	}

	public void setArtVerfuegbarkeit(String artVerfuegbarkeit) {
		this.artVerfuegbarkeit = artVerfuegbarkeit;
	}

	public String getArtMarke() {
		return artMarke;
	}

	public void setArtMarke(String artMarke) {
		this.artMarke = artMarke;
	}

	public String getArtKategorie() {
		return artKategorie;
	}

	public void setArtKategorie(String artKategorie) {
		this.artKategorie = artKategorie;
	}

	public String getArtImgUrl2() {
		return artImgUrl2;
	}

	public void setArtImgUrl2(String artImgUrl2) {
		this.artImgUrl2 = artImgUrl2;
	}

	public String getArtImgUrl3() {
		return artImgUrl3;
	}

	public void setArtImgUrl3(String artImgUrl3) {
		this.artImgUrl3 = artImgUrl3;
	}

	public String getArtImgUrl4() {
		return artImgUrl4;
	}

	public void setArtImgUrl4(String artImgUrl4) {
		this.artImgUrl4 = artImgUrl4;
	}

}
