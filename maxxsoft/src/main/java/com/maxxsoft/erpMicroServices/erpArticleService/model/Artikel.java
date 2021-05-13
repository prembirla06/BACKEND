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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
// @Builder
@Table(name = "artikel")
public class Artikel {

	// private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "REC_ID", unique = true, nullable = false)
	private Integer recId;

	@Column(name = "ALTTEIL_FLAG")
	private String altteilFlag;

	private String artikeltyp;

	private String artnum;

	@Column(name = "AUFW_KTO")
	private Integer aufwKto;

	private String ausschreibungstext;

	@Column(name = "AUTODEL_FLAG")
	private String autodelFlag;

	private String barcode;

	private String barcode2;

	private String barcode3;

	@Column(name = "BASISPR_FAKTOR")
	private BigDecimal basisprFaktor;

	@Column(name = "BASISPR_ME_ID")
	private Integer basisprMeId;

	private String breite;

	@Column(name = "DEFAULT_LIEF_ID")
	private Integer defaultLiefId;

	@Column(name = "DIM_A")
	private String dimA;

	@Column(name = "DIM_B")
	private String dimB;

	@Column(name = "DIM_C")
	private String dimC;

	private String dimension;

	@Column(name = "EK_LIEFERZEIT_ID")
	private Integer ekLieferzeitId;

	@Column(name = "EK_PREIS")
	private BigDecimal ekPreis;

	@Column(name = "EK_VPE")
	private BigDecimal ekVpe;

	@Column(name = "EKDS_PREIS")
	private BigDecimal ekdsPreis;

	@Column(name = "ERLOES_KTO")
	private Integer erloesKto;

	@Column(name = "ERSATZ_ARTIKEL_ID")
	private Integer ersatzArtikelId;

	@Column(name = "ERSATZ_ARTNUM")
	private String ersatzArtnum;

	@Column(name = "ERST_NAME")
	private String erstName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date erstellt;

	@Column(name = "ETIKETT_PRInteger")
	private Byte etikettPrInteger;

	@Column(name = "FSK18_FLAG")
	private String fsk18Flag;

	@Temporal(TemporalType.TIMESTAMP)
	private Date geaend;

	@Column(name = "GEAEND_NAME")
	private String geaendName;

	private BigDecimal gewicht;

	private String groesse;

	private String herkunftsland;

	@Column(name = "HERST_ARTNUM")
	private String herstArtnum;

	@Column(name = "HERSTELLER_ID")
	private Integer herstellerId;

	private String hoehe;

	@Lob
	private String info;

	@Column(name = "INVENTUR_WERT")
	private BigDecimal inventurWert;

	@Column(name = "KAS_NAME")
	private String kasName;

	@Column(name = "KENNZEICHNUNG_FLAG")
	private Byte kennzeichnungFlag;

	@Column(name = "KOMMISION_FLAG")
	private String kommisionFlag;

	private Byte konfig;

	private String kurzname;

	private String laenge;

	private String lagerort;

	@Lob
	private String langname;

	@Temporal(TemporalType.DATE)
	private Date liefertermin;

	private String liefstatus;

	@Column(name = "LIZENZ_FLAG")
	private String lizenzFlag;

	private String matchcode;

	private BigDecimal maxrabatt;

	@Column(name = "ME_ID")
	private Integer meId;

	@Column(name = "MENGE_AKT")
	private BigDecimal mengeAkt;

	@Column(name = "MENGE_BVOR")
	private BigDecimal mengeBvor;

	@Column(name = "MENGE_FLAG")
	private Byte mengeFlag;

	@Column(name = "MENGE_MIN")
	private BigDecimal mengeMin;

	@Column(name = "MENGE_START")
	private BigDecimal mengeStart;

	@Column(name = "MENGE_WARN")
	private BigDecimal mengeWarn;

	private BigDecimal mingewinn;

	@Column(name = "NE_GEWICHT")
	private BigDecimal neGewicht;

	@Column(name = "NE_TYP")
	private String neTyp;

	@Column(name = "NE_ZUSCHLAG_FLAG")
	private String neZuschlagFlag;

	@Column(name = "NO_BEZEDIT_FLAG")
	private String noBezeditFlag;

	@Column(name = "NO_EK_FLAG")
	private String noEkFlag;

	@Column(name = "NO_PROVISION_FLAG")
	private String noProvisionFlag;

	@Column(name = "NO_RABATT_FLAG")
	private String noRabattFlag;

	@Column(name = "NO_STOCK_FLAG")
	private String noStockFlag;

	@Column(name = "NO_VK_FLAG")
	private String noVkFlag;

	@Column(name = "PFAND1_ARTIKEL_ID")
	private Integer pfand1ArtikelId;

	@Column(name = "PFAND1_MENGE")
	private BigDecimal pfand1Menge;

	@Column(name = "PFAND2_ARTIKEL_ID")
	private Integer pfand2ArtikelId;

	@Column(name = "PFAND2_MENGE")
	private BigDecimal pfand2Menge;

	@Column(name = "PR_EINHEIT")
	private BigDecimal prEinheit;

	@Column(name = "PROD_DAUER")
	private Integer prodDauer;

	@Column(name = "PRODUKTION_FLAG")
	private String produktionFlag;

	@Column(name = "PROJEKT_ARTIKEL")
	private Byte projektArtikel;

	@Column(name = "PROVIS_PROZ")
	private BigDecimal provisProz;

	@Column(name = "RABGRP_ID")
	private String rabgrpId;

	@Column(name = "SHOP_ARTIKEL_ID")
	private Integer shopArtikelId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SHOP_CHANGE_DATE")
	private Date shopChangeDate;

	@Column(name = "SHOP_CHANGE_FLAG")
	private Byte shopChangeFlag;

	@Column(name = "SHOP_CHANGE_LIEF")
	private Integer shopChangeLief;

	@Column(name = "SHOP_CLICK_COUNT")
	private Integer shopClickCount;

	@Temporal(TemporalType.DATE)
	@Column(name = "SHOP_DATE_NEU")
	private Date shopDateNeu;

	@Column(name = "SHOP_DATENBLATT")
	private String shopDatenblatt;

	@Column(name = "SHOP_DEL_FLAG")
	private String shopDelFlag;

	@Temporal(TemporalType.DATE)
	@Column(name = "SHOP_FAELLT_WEG_AB")
	private Date shopFaelltWegAb;

	@Column(name = "SHOP_HANDBUCH")
	private String shopHandbuch;

	@Column(name = "SHOP_ID")
	private Byte shopId;

	@Column(name = "SHOP_IMAGE")
	private String shopImage;

	@Column(name = "SHOP_IMAGE_LARGE")
	private String shopImageLarge;

	@Column(name = "SHOP_IMAGE_MED")
	private String shopImageMed;

	@Column(name = "SHOP_KATALOG")
	private String shopKatalog;

	@Lob
	@Column(name = "SHOP_KURZTEXT")
	private String shopKurztext;

	@Lob
	@Column(name = "SHOP_LANGTEXT")
	private String shopLangtext;

	@Column(name = "SHOP_LIEFSTATUS")
	private Integer shopLiefstatus;

	@Column(name = "SHOP_LIEFTEXT")
	private String shopLieftext;

	@Lob
	@Column(name = "SHOP_META_BESCHR")
	private String shopMetaBeschr;

	@Column(name = "SHOP_META_KEY")
	private String shopMetaKey;

	@Column(name = "SHOP_META_TITEL")
	private String shopMetaTitel;

	@Column(name = "SHOP_PREIS_LISTE")
	private BigDecimal shopPreisListe;

	@Column(name = "SHOP_SORT")
	private Integer shopSort;

	@Column(name = "SHOP_SYNC")
	private Byte shopSync;

	@Column(name = "SHOP_VISIBLE")
	private Integer shopVisible;

	@Column(name = "SHOP_ZEICHNUNG")
	private String shopZeichnung;

	@Column(name = "SHOP_ZUB")
	private Byte shopZub;

	@Column(name = "SN_FLAG")
	private String snFlag;

	@Column(name = "STEUER_CODE")
	private Byte steuerCode;

	@Column(name = "USERFELD_01")
	private String userfeld01;

	@Column(name = "USERFELD_02")
	private String userfeld02;

	@Column(name = "USERFELD_03")
	private String userfeld03;

	@Column(name = "USERFELD_04")
	private String userfeld04;

	@Column(name = "USERFELD_05")
	private String userfeld05;

	@Column(name = "USERFELD_06")
	private String userfeld06;

	@Column(name = "USERFELD_07")
	private String userfeld07;

	@Column(name = "USERFELD_08")
	private String userfeld08;

	@Column(name = "USERFELD_09")
	private String userfeld09;

	@Column(name = "USERFELD_10")
	private String userfeld10;

	@Column(name = "VAR_ID")
	private Integer varId;

	private String varartnum;

	private String varname;

	private String vartext;

	@Column(name = "VK_LIEFERZEIT_ID")
	private Integer vkLieferzeitId;

	private BigDecimal vk1;

	private BigDecimal vk1b;

	private BigDecimal vk2;

	private BigDecimal vk2b;

	private BigDecimal vk3;

	private BigDecimal vk3b;

	private BigDecimal vk4;

	private BigDecimal vk4b;

	private BigDecimal vk5;

	private BigDecimal vk5b;

	private BigDecimal vpe;

	@Column(name = "VPE_EK")
	private BigDecimal vpeEk;

	private Integer warengruppe;

	private String zollnummer;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ARTIKEL_ID")
	private List<Inventory> inventory;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ARTIKEL_ID")
	private List<PackagingInformation> packagingInformationList;

	public Artikel() {
		inventory = new ArrayList<>();
		packagingInformationList = new ArrayList<>();
	}

	public Integer getRecId() {
		return this.recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public String getAltteilFlag() {
		return this.altteilFlag;
	}

	public void setAltteilFlag(String altteilFlag) {
		this.altteilFlag = altteilFlag;
	}

	public String getArtikeltyp() {
		return this.artikeltyp;
	}

	public void setArtikeltyp(String artikeltyp) {
		this.artikeltyp = artikeltyp;
	}

	public String getArtnum() {
		return this.artnum;
	}

	public void setArtnum(String artnum) {
		this.artnum = artnum;
	}

	public Integer getAufwKto() {
		return this.aufwKto;
	}

	public void setAufwKto(Integer aufwKto) {
		this.aufwKto = aufwKto;
	}

	public String getAusschreibungstext() {
		return this.ausschreibungstext;
	}

	public void setAusschreibungstext(String ausschreibungstext) {
		this.ausschreibungstext = ausschreibungstext;
	}

	public String getAutodelFlag() {
		return this.autodelFlag;
	}

	public void setAutodelFlag(String autodelFlag) {
		this.autodelFlag = autodelFlag;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBarcode2() {
		return this.barcode2;
	}

	public void setBarcode2(String barcode2) {
		this.barcode2 = barcode2;
	}

	public String getBarcode3() {
		return this.barcode3;
	}

	public void setBarcode3(String barcode3) {
		this.barcode3 = barcode3;
	}

	public BigDecimal getBasisprFaktor() {
		return this.basisprFaktor;
	}

	public void setBasisprFaktor(BigDecimal basisprFaktor) {
		this.basisprFaktor = basisprFaktor;
	}

	public Integer getBasisprMeId() {
		return this.basisprMeId;
	}

	public void setBasisprMeId(Integer basisprMeId) {
		this.basisprMeId = basisprMeId;
	}

	public String getBreite() {
		return this.breite;
	}

	public void setBreite(String breite) {
		this.breite = breite;
	}

	public Integer getDefaultLiefId() {
		return this.defaultLiefId;
	}

	public void setDefaultLiefId(Integer defaultLiefId) {
		this.defaultLiefId = defaultLiefId;
	}

	public String getDimA() {
		return this.dimA;
	}

	public void setDimA(String dimA) {
		this.dimA = dimA;
	}

	public String getDimB() {
		return this.dimB;
	}

	public void setDimB(String dimB) {
		this.dimB = dimB;
	}

	public String getDimC() {
		return this.dimC;
	}

	public void setDimC(String dimC) {
		this.dimC = dimC;
	}

	public String getDimension() {
		return this.dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public Integer getEkLieferzeitId() {
		return this.ekLieferzeitId;
	}

	public void setEkLieferzeitId(Integer ekLieferzeitId) {
		this.ekLieferzeitId = ekLieferzeitId;
	}

	public BigDecimal getEkPreis() {
		return this.ekPreis;
	}

	public void setEkPreis(BigDecimal ekPreis) {
		this.ekPreis = ekPreis;
	}

	public BigDecimal getEkVpe() {
		return this.ekVpe;
	}

	public void setEkVpe(BigDecimal ekVpe) {
		this.ekVpe = ekVpe;
	}

	public BigDecimal getEkdsPreis() {
		return this.ekdsPreis;
	}

	public void setEkdsPreis(BigDecimal ekdsPreis) {
		this.ekdsPreis = ekdsPreis;
	}

	public Integer getErloesKto() {
		return this.erloesKto;
	}

	public void setErloesKto(Integer erloesKto) {
		this.erloesKto = erloesKto;
	}

	public Integer getErsatzArtikelId() {
		return this.ersatzArtikelId;
	}

	public void setErsatzArtikelId(Integer ersatzArtikelId) {
		this.ersatzArtikelId = ersatzArtikelId;
	}

	public String getErsatzArtnum() {
		return this.ersatzArtnum;
	}

	public void setErsatzArtnum(String ersatzArtnum) {
		this.ersatzArtnum = ersatzArtnum;
	}

	public String getErstName() {
		return this.erstName;
	}

	public void setErstName(String erstName) {
		this.erstName = erstName;
	}

	public Date getErstellt() {
		return this.erstellt;
	}

	public void setErstellt(Date erstellt) {
		this.erstellt = erstellt;
	}

	public Byte getEtikettPrInteger() {
		return this.etikettPrInteger;
	}

	public void setEtikettPrInteger(Byte etikettPrInteger) {
		this.etikettPrInteger = etikettPrInteger;
	}

	public String getFsk18Flag() {
		return this.fsk18Flag;
	}

	public void setFsk18Flag(String fsk18Flag) {
		this.fsk18Flag = fsk18Flag;
	}

	public Date getGeaend() {
		return this.geaend;
	}

	public void setGeaend(Date geaend) {
		this.geaend = geaend;
	}

	public String getGeaendName() {
		return this.geaendName;
	}

	public void setGeaendName(String geaendName) {
		this.geaendName = geaendName;
	}

	public BigDecimal getGewicht() {
		return this.gewicht;
	}

	public void setGewicht(BigDecimal gewicht) {
		this.gewicht = gewicht;
	}

	public String getGroesse() {
		return this.groesse;
	}

	public void setGroesse(String groesse) {
		this.groesse = groesse;
	}

	public String getHerkunftsland() {
		return this.herkunftsland;
	}

	public void setHerkunftsland(String herkunftsland) {
		this.herkunftsland = herkunftsland;
	}

	public String getHerstArtnum() {
		return this.herstArtnum;
	}

	public void setHerstArtnum(String herstArtnum) {
		this.herstArtnum = herstArtnum;
	}

	public Integer getHerstellerId() {
		return this.herstellerId;
	}

	public void setHerstellerId(Integer herstellerId) {
		this.herstellerId = herstellerId;
	}

	public String getHoehe() {
		return this.hoehe;
	}

	public void setHoehe(String hoehe) {
		this.hoehe = hoehe;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public BigDecimal getInventurWert() {
		return this.inventurWert;
	}

	public void setInventurWert(BigDecimal inventurWert) {
		this.inventurWert = inventurWert;
	}

	public String getKasName() {
		return this.kasName;
	}

	public void setKasName(String kasName) {
		this.kasName = kasName;
	}

	public Byte getKennzeichnungFlag() {
		return this.kennzeichnungFlag;
	}

	public void setKennzeichnungFlag(Byte kennzeichnungFlag) {
		this.kennzeichnungFlag = kennzeichnungFlag;
	}

	public String getKommisionFlag() {
		return this.kommisionFlag;
	}

	public void setKommisionFlag(String kommisionFlag) {
		this.kommisionFlag = kommisionFlag;
	}

	public Byte getKonfig() {
		return this.konfig;
	}

	public void setKonfig(Byte konfig) {
		this.konfig = konfig;
	}

	public String getKurzname() {
		return this.kurzname;
	}

	public void setKurzname(String kurzname) {
		this.kurzname = kurzname;
	}

	public String getLaenge() {
		return this.laenge;
	}

	public void setLaenge(String laenge) {
		this.laenge = laenge;
	}

	public String getLagerort() {
		return this.lagerort;
	}

	public void setLagerort(String lagerort) {
		this.lagerort = lagerort;
	}

	public String getLangname() {
		return this.langname;
	}

	public void setLangname(String langname) {
		this.langname = langname;
	}

	public Date getLiefertermin() {
		return this.liefertermin;
	}

	public void setLiefertermin(Date liefertermin) {
		this.liefertermin = liefertermin;
	}

	public String getLiefstatus() {
		return this.liefstatus;
	}

	public void setLiefstatus(String liefstatus) {
		this.liefstatus = liefstatus;
	}

	public String getLizenzFlag() {
		return this.lizenzFlag;
	}

	public void setLizenzFlag(String lizenzFlag) {
		this.lizenzFlag = lizenzFlag;
	}

	public String getMatchcode() {
		return this.matchcode;
	}

	public void setMatchcode(String matchcode) {
		this.matchcode = matchcode;
	}

	public BigDecimal getMaxrabatt() {
		return this.maxrabatt;
	}

	public void setMaxrabatt(BigDecimal maxrabatt) {
		this.maxrabatt = maxrabatt;
	}

	public Integer getMeId() {
		return this.meId;
	}

	public void setMeId(Integer meId) {
		this.meId = meId;
	}

	public BigDecimal getMengeAkt() {
		return this.mengeAkt;
	}

	public void setMengeAkt(BigDecimal mengeAkt) {
		this.mengeAkt = mengeAkt;
	}

	public BigDecimal getMengeBvor() {
		return this.mengeBvor;
	}

	public void setMengeBvor(BigDecimal mengeBvor) {
		this.mengeBvor = mengeBvor;
	}

	public Byte getMengeFlag() {
		return this.mengeFlag;
	}

	public void setMengeFlag(Byte mengeFlag) {
		this.mengeFlag = mengeFlag;
	}

	public BigDecimal getMengeMin() {
		return this.mengeMin;
	}

	public void setMengeMin(BigDecimal mengeMin) {
		this.mengeMin = mengeMin;
	}

	public BigDecimal getMengeStart() {
		return this.mengeStart;
	}

	public void setMengeStart(BigDecimal mengeStart) {
		this.mengeStart = mengeStart;
	}

	public BigDecimal getMengeWarn() {
		return this.mengeWarn;
	}

	public void setMengeWarn(BigDecimal mengeWarn) {
		this.mengeWarn = mengeWarn;
	}

	public BigDecimal getMingewinn() {
		return this.mingewinn;
	}

	public void setMingewinn(BigDecimal mingewinn) {
		this.mingewinn = mingewinn;
	}

	public BigDecimal getNeGewicht() {
		return this.neGewicht;
	}

	public void setNeGewicht(BigDecimal neGewicht) {
		this.neGewicht = neGewicht;
	}

	public String getNeTyp() {
		return this.neTyp;
	}

	public void setNeTyp(String neTyp) {
		this.neTyp = neTyp;
	}

	public String getNeZuschlagFlag() {
		return this.neZuschlagFlag;
	}

	public void setNeZuschlagFlag(String neZuschlagFlag) {
		this.neZuschlagFlag = neZuschlagFlag;
	}

	public String getNoBezeditFlag() {
		return this.noBezeditFlag;
	}

	public void setNoBezeditFlag(String noBezeditFlag) {
		this.noBezeditFlag = noBezeditFlag;
	}

	public String getNoEkFlag() {
		return this.noEkFlag;
	}

	public void setNoEkFlag(String noEkFlag) {
		this.noEkFlag = noEkFlag;
	}

	public String getNoProvisionFlag() {
		return this.noProvisionFlag;
	}

	public void setNoProvisionFlag(String noProvisionFlag) {
		this.noProvisionFlag = noProvisionFlag;
	}

	public String getNoRabattFlag() {
		return this.noRabattFlag;
	}

	public void setNoRabattFlag(String noRabattFlag) {
		this.noRabattFlag = noRabattFlag;
	}

	public String getNoStockFlag() {
		return this.noStockFlag;
	}

	public void setNoStockFlag(String noStockFlag) {
		this.noStockFlag = noStockFlag;
	}

	public String getNoVkFlag() {
		return this.noVkFlag;
	}

	public void setNoVkFlag(String noVkFlag) {
		this.noVkFlag = noVkFlag;
	}

	public Integer getPfand1ArtikelId() {
		return this.pfand1ArtikelId;
	}

	public void setPfand1ArtikelId(Integer pfand1ArtikelId) {
		this.pfand1ArtikelId = pfand1ArtikelId;
	}

	public BigDecimal getPfand1Menge() {
		return this.pfand1Menge;
	}

	public void setPfand1Menge(BigDecimal pfand1Menge) {
		this.pfand1Menge = pfand1Menge;
	}

	public Integer getPfand2ArtikelId() {
		return this.pfand2ArtikelId;
	}

	public void setPfand2ArtikelId(Integer pfand2ArtikelId) {
		this.pfand2ArtikelId = pfand2ArtikelId;
	}

	public BigDecimal getPfand2Menge() {
		return this.pfand2Menge;
	}

	public void setPfand2Menge(BigDecimal pfand2Menge) {
		this.pfand2Menge = pfand2Menge;
	}

	public BigDecimal getPrEinheit() {
		return this.prEinheit;
	}

	public void setPrEinheit(BigDecimal prEinheit) {
		this.prEinheit = prEinheit;
	}

	public Integer getProdDauer() {
		return this.prodDauer;
	}

	public void setProdDauer(Integer prodDauer) {
		this.prodDauer = prodDauer;
	}

	public String getProduktionFlag() {
		return this.produktionFlag;
	}

	public void setProduktionFlag(String produktionFlag) {
		this.produktionFlag = produktionFlag;
	}

	public Byte getProjektArtikel() {
		return this.projektArtikel;
	}

	public void setProjektArtikel(Byte projektArtikel) {
		this.projektArtikel = projektArtikel;
	}

	public BigDecimal getProvisProz() {
		return this.provisProz;
	}

	public void setProvisProz(BigDecimal provisProz) {
		this.provisProz = provisProz;
	}

	public String getRabgrpId() {
		return this.rabgrpId;
	}

	public void setRabgrpId(String rabgrpId) {
		this.rabgrpId = rabgrpId;
	}

	public Integer getShopArtikelId() {
		return this.shopArtikelId;
	}

	public void setShopArtikelId(Integer shopArtikelId) {
		this.shopArtikelId = shopArtikelId;
	}

	public Date getShopChangeDate() {
		return this.shopChangeDate;
	}

	public void setShopChangeDate(Date shopChangeDate) {
		this.shopChangeDate = shopChangeDate;
	}

	public Byte getShopChangeFlag() {
		return this.shopChangeFlag;
	}

	public void setShopChangeFlag(Byte shopChangeFlag) {
		this.shopChangeFlag = shopChangeFlag;
	}

	public Integer getShopChangeLief() {
		return this.shopChangeLief;
	}

	public void setShopChangeLief(Integer shopChangeLief) {
		this.shopChangeLief = shopChangeLief;
	}

	public Integer getShopClickCount() {
		return this.shopClickCount;
	}

	public void setShopClickCount(Integer shopClickCount) {
		this.shopClickCount = shopClickCount;
	}

	public Date getShopDateNeu() {
		return this.shopDateNeu;
	}

	public void setShopDateNeu(Date shopDateNeu) {
		this.shopDateNeu = shopDateNeu;
	}

	public String getShopDatenblatt() {
		return this.shopDatenblatt;
	}

	public void setShopDatenblatt(String shopDatenblatt) {
		this.shopDatenblatt = shopDatenblatt;
	}

	public String getShopDelFlag() {
		return this.shopDelFlag;
	}

	public void setShopDelFlag(String shopDelFlag) {
		this.shopDelFlag = shopDelFlag;
	}

	public Date getShopFaelltWegAb() {
		return this.shopFaelltWegAb;
	}

	public void setShopFaelltWegAb(Date shopFaelltWegAb) {
		this.shopFaelltWegAb = shopFaelltWegAb;
	}

	public String getShopHandbuch() {
		return this.shopHandbuch;
	}

	public void setShopHandbuch(String shopHandbuch) {
		this.shopHandbuch = shopHandbuch;
	}

	public Byte getShopId() {
		return this.shopId;
	}

	public void setShopId(Byte shopId) {
		this.shopId = shopId;
	}

	public String getShopImage() {
		return this.shopImage;
	}

	public void setShopImage(String shopImage) {
		this.shopImage = shopImage;
	}

	public String getShopImageLarge() {
		return this.shopImageLarge;
	}

	public void setShopImageLarge(String shopImageLarge) {
		this.shopImageLarge = shopImageLarge;
	}

	public String getShopImageMed() {
		return this.shopImageMed;
	}

	public void setShopImageMed(String shopImageMed) {
		this.shopImageMed = shopImageMed;
	}

	public String getShopKatalog() {
		return this.shopKatalog;
	}

	public void setShopKatalog(String shopKatalog) {
		this.shopKatalog = shopKatalog;
	}

	public String getShopKurztext() {
		return this.shopKurztext;
	}

	public void setShopKurztext(String shopKurztext) {
		this.shopKurztext = shopKurztext;
	}

	public String getShopLangtext() {
		return this.shopLangtext;
	}

	public void setShopLangtext(String shopLangtext) {
		this.shopLangtext = shopLangtext;
	}

	public Integer getShopLiefstatus() {
		return this.shopLiefstatus;
	}

	public void setShopLiefstatus(Integer shopLiefstatus) {
		this.shopLiefstatus = shopLiefstatus;
	}

	public String getShopLieftext() {
		return this.shopLieftext;
	}

	public void setShopLieftext(String shopLieftext) {
		this.shopLieftext = shopLieftext;
	}

	public String getShopMetaBeschr() {
		return this.shopMetaBeschr;
	}

	public void setShopMetaBeschr(String shopMetaBeschr) {
		this.shopMetaBeschr = shopMetaBeschr;
	}

	public String getShopMetaKey() {
		return this.shopMetaKey;
	}

	public void setShopMetaKey(String shopMetaKey) {
		this.shopMetaKey = shopMetaKey;
	}

	public String getShopMetaTitel() {
		return this.shopMetaTitel;
	}

	public void setShopMetaTitel(String shopMetaTitel) {
		this.shopMetaTitel = shopMetaTitel;
	}

	public BigDecimal getShopPreisListe() {
		return this.shopPreisListe;
	}

	public void setShopPreisListe(BigDecimal shopPreisListe) {
		this.shopPreisListe = shopPreisListe;
	}

	public Integer getShopSort() {
		return this.shopSort;
	}

	public void setShopSort(Integer shopSort) {
		this.shopSort = shopSort;
	}

	public Byte getShopSync() {
		return this.shopSync;
	}

	public void setShopSync(Byte shopSync) {
		this.shopSync = shopSync;
	}

	public Integer getShopVisible() {
		return this.shopVisible;
	}

	public void setShopVisible(Integer shopVisible) {
		this.shopVisible = shopVisible;
	}

	public String getShopZeichnung() {
		return this.shopZeichnung;
	}

	public void setShopZeichnung(String shopZeichnung) {
		this.shopZeichnung = shopZeichnung;
	}

	public Byte getShopZub() {
		return this.shopZub;
	}

	public void setShopZub(Byte shopZub) {
		this.shopZub = shopZub;
	}

	public String getSnFlag() {
		return this.snFlag;
	}

	public void setSnFlag(String snFlag) {
		this.snFlag = snFlag;
	}

	public Byte getSteuerCode() {
		return this.steuerCode;
	}

	public void setSteuerCode(Byte steuerCode) {
		this.steuerCode = steuerCode;
	}

	public String getUserfeld01() {
		return this.userfeld01;
	}

	public void setUserfeld01(String userfeld01) {
		this.userfeld01 = userfeld01;
	}

	public String getUserfeld02() {
		return this.userfeld02;
	}

	public void setUserfeld02(String userfeld02) {
		this.userfeld02 = userfeld02;
	}

	public String getUserfeld03() {
		return this.userfeld03;
	}

	public void setUserfeld03(String userfeld03) {
		this.userfeld03 = userfeld03;
	}

	public String getUserfeld04() {
		return this.userfeld04;
	}

	public void setUserfeld04(String userfeld04) {
		this.userfeld04 = userfeld04;
	}

	public String getUserfeld05() {
		return this.userfeld05;
	}

	public void setUserfeld05(String userfeld05) {
		this.userfeld05 = userfeld05;
	}

	public String getUserfeld06() {
		return this.userfeld06;
	}

	public void setUserfeld06(String userfeld06) {
		this.userfeld06 = userfeld06;
	}

	public String getUserfeld07() {
		return this.userfeld07;
	}

	public void setUserfeld07(String userfeld07) {
		this.userfeld07 = userfeld07;
	}

	public String getUserfeld08() {
		return this.userfeld08;
	}

	public void setUserfeld08(String userfeld08) {
		this.userfeld08 = userfeld08;
	}

	public String getUserfeld09() {
		return this.userfeld09;
	}

	public void setUserfeld09(String userfeld09) {
		this.userfeld09 = userfeld09;
	}

	public String getUserfeld10() {
		return this.userfeld10;
	}

	public void setUserfeld10(String userfeld10) {
		this.userfeld10 = userfeld10;
	}

	public Integer getVarId() {
		return this.varId;
	}

	public void setVarId(Integer varId) {
		this.varId = varId;
	}

	public String getVarartnum() {
		return this.varartnum;
	}

	public void setVarartnum(String varartnum) {
		this.varartnum = varartnum;
	}

	public String getVarname() {
		return this.varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getVartext() {
		return this.vartext;
	}

	public void setVartext(String vartext) {
		this.vartext = vartext;
	}

	public Integer getVkLieferzeitId() {
		return this.vkLieferzeitId;
	}

	public void setVkLieferzeitId(Integer vkLieferzeitId) {
		this.vkLieferzeitId = vkLieferzeitId;
	}

	public BigDecimal getVk1() {
		return this.vk1;
	}

	public void setVk1(BigDecimal vk1) {
		this.vk1 = vk1;
	}

	public BigDecimal getVk1b() {
		return this.vk1b;
	}

	public void setVk1b(BigDecimal vk1b) {
		this.vk1b = vk1b;
	}

	public BigDecimal getVk2() {
		return this.vk2;
	}

	public void setVk2(BigDecimal vk2) {
		this.vk2 = vk2;
	}

	public BigDecimal getVk2b() {
		return this.vk2b;
	}

	public void setVk2b(BigDecimal vk2b) {
		this.vk2b = vk2b;
	}

	public BigDecimal getVk3() {
		return this.vk3;
	}

	public void setVk3(BigDecimal vk3) {
		this.vk3 = vk3;
	}

	public BigDecimal getVk3b() {
		return this.vk3b;
	}

	public void setVk3b(BigDecimal vk3b) {
		this.vk3b = vk3b;
	}

	public BigDecimal getVk4() {
		return this.vk4;
	}

	public void setVk4(BigDecimal vk4) {
		this.vk4 = vk4;
	}

	public BigDecimal getVk4b() {
		return this.vk4b;
	}

	public void setVk4b(BigDecimal vk4b) {
		this.vk4b = vk4b;
	}

	public BigDecimal getVk5() {
		return this.vk5;
	}

	public void setVk5(BigDecimal vk5) {
		this.vk5 = vk5;
	}

	public BigDecimal getVk5b() {
		return this.vk5b;
	}

	public void setVk5b(BigDecimal vk5b) {
		this.vk5b = vk5b;
	}

	public BigDecimal getVpe() {
		return this.vpe;
	}

	public void setVpe(BigDecimal vpe) {
		this.vpe = vpe;
	}

	public BigDecimal getVpeEk() {
		return this.vpeEk;
	}

	public void setVpeEk(BigDecimal vpeEk) {
		this.vpeEk = vpeEk;
	}

	public Integer getWarengruppe() {
		return this.warengruppe;
	}

	public void setWarengruppe(Integer warengruppe) {
		this.warengruppe = warengruppe;
	}

	public String getZollnummer() {
		return this.zollnummer;
	}

	public void setZollnummer(String zollnummer) {
		this.zollnummer = zollnummer;
	}

	public List<Inventory> getInventory() {
		return inventory;
	}

	public void setInventory(List<Inventory> inventory) {
		this.inventory = inventory;
	}

	public List<PackagingInformation> getPackagingInformationList() {
		return packagingInformationList;
	}

	public void setPackagingInformationList(List<PackagingInformation> packagingInformationList) {
		this.packagingInformationList = packagingInformationList;
	}
}
