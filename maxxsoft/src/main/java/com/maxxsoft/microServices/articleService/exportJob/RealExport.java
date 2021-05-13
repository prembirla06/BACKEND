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
package com.maxxsoft.microServices.articleService.exportJob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.RealAngebotdatei;
import com.maxxsoft.microServices.articleService.model.export.RealProduktdatei;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.model.MagentoProduct;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;
import com.maxxsoft.microServices.magentoService.repository.MagentoProductRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class RealExport {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	PriceService priceService;
	@Autowired
	CommonExportConfig config;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;

	@Autowired
	private MagentoProductRepository magentoProductRepository;

	FTPClient ftp = null;
	byte pic[] = null;

	private static ArrayList<RealProduktdatei> realProduktdateiList;
	private static ArrayList<RealAngebotdatei> realAngebotdateiList;

	public void runRealExport() {
		realProduktdateiList = new ArrayList<RealProduktdatei>();
		realAngebotdateiList = new ArrayList<RealAngebotdatei>();
		log.info("---Start runRealExport");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone() /*&& article.getNumber().equals("BR_PLUTO120-200_WEIß_SCHWARZ_1754")*/) {
				//if (article.getNumber().equals("BR_PLUTO120-200_WEIß_SCHWARZ_1754") || article.getNumber().equals("8044849")) {
				//	System.out.println("debug");
				//}
				//System.out.println(article.getNumber());
				RealProduktdatei element = new RealProduktdatei();
				//BigDecimal sellPriceMagento = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(),
				//		sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
				BigDecimal sellPriceReal = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(),
						sellingPlatformRepository.findByName("Real").get().getSellingPlatformId());
				BigDecimal MinSellPriceReal = priceService.getMinimumSellingPrice(article.getNumber(),
						article.getBuyPrice(),
						sellingPlatformRepository.findByName("Real").get().getSellingPlatformId());
				Artikel erpArticle = erpArticleService.getArtikelByNumber(article.getNumber()).get();
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(article.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				List<String> pictureURLs = new ArrayList<>();
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(article.getNumber());
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(media.getFile());
					});
				}
				int actualStock = article.getStock() - article.getPreOrder();
				element.setEan(article.getEan());

				element.setCategory(config.lastCategory);

				element.setTitle(article.getLongName());

				// Description without ";" and "|" and "\n" and "\r"
				String desc_temp = article.getLongDescription().replace(";", ",");
				desc_temp = desc_temp.replace("|", ",");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				
				element.setDescription(desc_temp);

				if (pictureURLs.size() < 1) {
					// TODO: 
					log.error("RealExport.java: No Image there for Article "+ article.getNumber() +" -> Error Message");
				}
				
				if (pictureURLs.size() >= 1)
				element.setPicture1(config.mediaURL + pictureURLs.get(0));
				else
					element.setPicture1("");
				if (pictureURLs.size() >= 2)
					element.setPicture2(config.mediaURL + pictureURLs.get(1));
				else
					element.setPicture2("");
				if (pictureURLs.size() >= 3)
					element.setPicture3(config.mediaURL + pictureURLs.get(2));
				else
					element.setPicture3("");
				if (pictureURLs.size() >= 4)
					element.setPicture4(config.mediaURL + pictureURLs.get(3));
				else
					element.setPicture4("");
				element.setManufacturer("moebel-guenstig24");

				String short_desc_temp = article.getShortDescription().replace(";", ",");
				short_desc_temp = short_desc_temp.replace("|", ",");
				short_desc_temp = short_desc_temp.replace("\n", " ");
				short_desc_temp = short_desc_temp.replace("\r", " ");
				
				element.setShort_description(short_desc_temp);
				if (config.breite.length() > 0)
					element.setWidth((Float.valueOf(config.breite) / 100 + "m").replace(".", ","));
				else
					element.setWidth("");

				element.setDecor(config.trendfarbe);
				String energyEfficiencyClass_temp = new String("");
				if (config.besonderheit.contains("mit Beleuchtung")) {
					energyEfficiencyClass_temp = "A+++ bis A";
				}
				element.setEnergy_efficiency_class(energyEfficiencyClass_temp);
				element.setEnergylabel(""); // keine ahnung was hier hin kommen
											// soll
											// - link zum Labelbild?
				element.setColour(config.farbe1);
				element.setMpn(article.getNumber());
				if (config.hoehe.length() > 0)
					element.setHeight((Float.valueOf(config.hoehe) / 100 + "m").replace(".", ","));
				else
					element.setHeight("");
				
				if (config.tiefe.length() > 0)
					element.setDepth((Float.valueOf(config.tiefe) / 100 + " m").replace(".", ","));
				else
					element.setDepth("");
				element.setAdditional_categories(""); // element.setAdditional_categories(categoryPath);
														// Fehler: additional
														// categories are not
														// used
														// yet.
				element.setMaterial_composition("Keine Angaben erforderlich");

				if (article.getEan() != null && article.getEan().length() >= 2)
					realProduktdateiList.add(element);

				RealAngebotdatei elementAng = new RealAngebotdatei();

				elementAng.setEan(article.getEan());
				elementAng.setCondition("100");
				elementAng.setPrice("");
				elementAng.setComment("Neu");
				elementAng.setOfferId(article.getNumber());
				elementAng.setLocation("");
				elementAng.setWarehouse("");
				elementAng.setCount(String.valueOf(actualStock));
				elementAng.setCount("10");

				int deliveryTime = article.getDeliveryTime();
				elementAng.setDeliveryTimeMin(String.valueOf(deliveryTime));
				elementAng.setDeliveryTimeMax(String.valueOf(deliveryTime));
				
				/*
				double dt = deliveryTime;
				double weeks = dt / 7;
				double dtweekdays = weeks * 5;

				
				if (dtweekdays >= 0 && dtweekdays <= 3) {
					elementAng.setDeliveryTime("b");
				} else if (dtweekdays >= 4 && dtweekdays <= 6) {
					elementAng.setDeliveryTime("c");
				} else if (dtweekdays >= 7 && dtweekdays <= 10) {
					elementAng.setDeliveryTime("d");
				} else if (dtweekdays >= 11 && dtweekdays <= 14) {
					elementAng.setDeliveryTime("e");
				} else if (dtweekdays >= 15 && dtweekdays <= 28) {
					elementAng.setDeliveryTime("f");
				} else if (dtweekdays >= 29 && dtweekdays <= 49) {
					elementAng.setDeliveryTime("g");
				} else if (dtweekdays >= 50 && dtweekdays <= 70) {
					elementAng.setDeliveryTime("g"); // war mal "i" auf "g"
					// geändert!
				} else
					elementAng.setDeliveryTime("g"); // war mal "i" auf "g"
														// geändert!
				*/
				elementAng.setMinimumPrice("");
				elementAng.setPriceCs(sellPriceReal.toString());
				// Konkurenzpreis unterbieten über vergleichsdatei aus Real:
				try {
					java.io.BufferedReader FileReader = new java.io.BufferedReader(new java.io.FileReader(
							new java.io.File(config.outputDir + "report_competitorscomparer_real.csv")));

					String zeile = "";

					while (null != (zeile = FileReader.readLine())) {
						String[] split = zeile.split("\t");

						if (split[1].replaceAll("\"", "").equals(article.getNumber())) {
							if (Float.valueOf(split[3].replaceAll(",", ".")) < sellPriceReal.floatValue())
								if (Float.valueOf(split[3].replaceAll(",", ".")) >= MinSellPriceReal.floatValue()) {
									DecimalFormat df = new DecimalFormat("0.00");
									elementAng.setPriceCs(String
											.valueOf(df.format(Float.valueOf(split[3].replaceAll(",", ".")) - 0.10))
											.replace(",", "."));
								} else {
									log.info(article.getNumber() + ":" + article.getShortName() +
											" Realpreis ist unter Mindestpreis - bitte prüfen das Konkurrenzangebot bei Real");
								}
							break;
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				elementAng.setMinimumPriceCs("");
				if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("spedition"))
					elementAng.setShippingGroup("spedition");
				else
					elementAng.setShippingGroup("");

				if (article.getEan() != null && article.getEan().length() >= 2)
					realAngebotdateiList.add(elementAng);
			}
			});
		
		
		articleSetRepository.findAll().forEach(articleSet -> {
			//if (articleSet.getNumber().equals("TMM0702024417122")) {
				//System.out.println(articleSet.getNumber());
				RealProduktdatei element = new RealProduktdatei();
				int actualStock = articleSet.getStock();
				BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
				BigDecimal sellPriceReal = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Real").get().getSellingPlatformId());
				BigDecimal MinSellPriceReal = priceService.getMinimumSellingPrice(articleSet.getNumber(),
						setBuyPrice,
						sellingPlatformRepository.findByName("Real").get().getSellingPlatformId());
				
				
				Artikel erpArticle = erpArticleService.getArtikelByNumber(articleSet.getNumber()).get();
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(articleSet.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				List<String> pictureURLs = new ArrayList<>();
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(articleSet.getNumber());
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(media.getFile());
					});
				}
				element.setEan(articleSet.getEan());

				element.setCategory(config.lastCategory);

				element.setTitle(articleSet.getShortName());

				// Description without ";" and "|" and "\n" and "\r"
				String desc_temp = articleSet.getLongDescription().replace(";", ",");
				desc_temp = desc_temp.replace("|", ",");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				
				element.setDescription(desc_temp);

				if (pictureURLs.size() < 1) {
					// TODO: 
					log.error("RealExport.java: No Image there for Article "+ articleSet.getNumber() +" -> Error Message");
				}
				
				if (pictureURLs.size() >= 1)
				element.setPicture1(config.mediaURL + pictureURLs.get(0));
				else
					element.setPicture1("");
				if (pictureURLs.size() >= 2)
					element.setPicture2(config.mediaURL + pictureURLs.get(1));
				else
					element.setPicture2("");
				if (pictureURLs.size() >= 3)
					element.setPicture3(config.mediaURL + pictureURLs.get(2));
				else
					element.setPicture3("");
				if (pictureURLs.size() >= 4)
					element.setPicture4(config.mediaURL + pictureURLs.get(3));
				else
					element.setPicture4("");
				element.setManufacturer("moebel-guenstig24");

				String short_desc_temp = articleSet.getShortDescription().replace(";", ",");
				short_desc_temp = short_desc_temp.replace("|", ",");
				short_desc_temp = short_desc_temp.replace("\n", " ");
				short_desc_temp = short_desc_temp.replace("\r", " ");
				
				element.setShort_description(short_desc_temp);
				if (Objects.nonNull(config.breite) && config.breite.length() > 0)
					element.setWidth((Float.valueOf(config.breite) / 100 + "m").replace(".", ","));
				else
					element.setWidth("");

				element.setDecor(config.trendfarbe);
				String energyEfficiencyClass_temp = new String("");
				if (config.besonderheit.contains("mit Beleuchtung")) {
					energyEfficiencyClass_temp = "A+++ bis A";
				}
				element.setEnergy_efficiency_class(energyEfficiencyClass_temp);
				element.setEnergylabel(""); // keine ahnung was hier hin kommen
											// soll
											// - link zum Labelbild?
				element.setColour(config.farbe1);
				element.setMpn(articleSet.getNumber());
				if (Objects.nonNull(config.hoehe) && config.hoehe.length() > 0)
					element.setHeight((Float.valueOf(config.hoehe) / 100 + "m").replace(".", ","));
				else
					element.setHeight("");
				
				if (Objects.nonNull(config.tiefe) && config.tiefe.length() > 0)
					element.setDepth((Float.valueOf(config.tiefe) / 100 + " m").replace(".", ","));
				else
					element.setDepth("");
				element.setAdditional_categories(""); // element.setAdditional_categories(categoryPath);
														// Fehler: additional
														// categories are not
														// used
														// yet.
				element.setMaterial_composition("Keine Angaben erforderlich");

				if (articleSet.getEan() != null && articleSet.getEan().length() >= 2)
					realProduktdateiList.add(element);

				RealAngebotdatei elementAng = new RealAngebotdatei();

				elementAng.setEan(articleSet.getEan());
				elementAng.setCondition("100");
				elementAng.setPrice("");
				elementAng.setComment("Neu");
				elementAng.setOfferId(articleSet.getNumber());
				elementAng.setLocation("");
				elementAng.setWarehouse("");
				elementAng.setCount(String.valueOf(actualStock));
				elementAng.setCount("10");

				// Workingdays
				int deliveryTime = articleSet.getDeliveryTime();
				elementAng.setDeliveryTimeMin(String.valueOf(deliveryTime));
				elementAng.setDeliveryTimeMax(String.valueOf(deliveryTime));
				
				/*double dt = deliveryTime;
				double weeks = dt / 7;
				double dtweekdays = weeks * 5;
						
				
				if (dtweekdays >= 0 && dtweekdays <= 3) {
					elementAng.setDeliveryTime("b");
				} else if (dtweekdays >= 4 && dtweekdays <= 6) {
					elementAng.setDeliveryTime("c");
				} else if (dtweekdays >= 7 && dtweekdays <= 10) {
					elementAng.setDeliveryTime("d");
				} else if (dtweekdays >= 11 && dtweekdays <= 14) {
					elementAng.setDeliveryTime("e");
				} else if (dtweekdays >= 15 && dtweekdays <= 28) {
					elementAng.setDeliveryTime("f");
				} else if (dtweekdays >= 29 && dtweekdays <= 49) {
					elementAng.setDeliveryTime("g");
				} else if (dtweekdays >= 50 && dtweekdays <= 70) {
					elementAng.setDeliveryTime("g"); // war mal "i" auf "g"
					// geändert!
				} else
					elementAng.setDeliveryTime("g"); // war mal "i" auf "g"
				*/										// geändert!
				elementAng.setMinimumPrice("");
				elementAng.setPriceCs(sellPriceReal.toString());
				// Konkurenzpreis unterbieten über vergleichsdatei aus Real:
				try {
					java.io.BufferedReader FileReader = new java.io.BufferedReader(new java.io.FileReader(
							new java.io.File(config.outputDir + "report_competitorscomparer_real.csv")));

					String zeile = "";

					while (null != (zeile = FileReader.readLine())) {
						String[] split = zeile.split("\t");

						if (split[1].replaceAll("\"", "").equals(articleSet.getNumber())) {
							if (Float.valueOf(split[3].replaceAll(",", ".")) < sellPriceReal.floatValue())
								if (Float.valueOf(split[3].replaceAll(",", ".")) >= MinSellPriceReal.floatValue()) {
									DecimalFormat df = new DecimalFormat("0.00");
									elementAng.setPriceCs(String
											.valueOf(df.format(Float.valueOf(split[3].replaceAll(",", ".")) - 0.10))
											.replace(",", "."));
								} else {
									log.info(articleSet.getNumber() + ":" + articleSet.getShortName() +
											" Realpreis ist unter Mindestpreis - bitte prüfen das Konkurrenzangebot bei Real");
								}
							break;
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				elementAng.setMinimumPriceCs("");
				if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("spedition"))
					elementAng.setShippingGroup("spedition");
				else
					elementAng.setShippingGroup("");

				if (articleSet.getEan() != null && articleSet.getEan().length() >= 2)
					realAngebotdateiList.add(elementAng);
			//}
			});

		try {

			// Real Produktdatei

			Map<String, Object> root = new HashMap<>();
			root.put("realProduktdateiList", realProduktdateiList);
			Configuration cfg = config.getConfiguration();

			Template realProduktdateiTemplate = cfg.getTemplate("realProduktdateiTemplate.csv");

			FileWriter writerRealProduktdatei = new FileWriter(config.outputDir + "realProduktdatei.csv", false);
			realProduktdateiTemplate.process(root, writerRealProduktdatei);
			writerRealProduktdatei.close();

			// FTP Upload:
			String localFile = config.outputDir + "realProduktdatei.csv";
			config.uploadSftpFromPath(localFile, "/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/realProduktdatei.csv");

			log.info("RealExport Produkte done!");

			
			// Real Angebotsdatei
			root.clear();
			root.put("realAngebotdateiList", realAngebotdateiList);

			Template realAngebotdateiTemplate = cfg.getTemplate("realAngebotdateiTemplate.csv");

			FileWriter writerRealAngebotdatei = new FileWriter(config.outputDir + "realAngebotdatei.csv", false);
			realAngebotdateiTemplate.process(root, writerRealAngebotdatei);
			writerRealAngebotdatei.close();

			// FTP Upload:
			localFile = config.outputDir + "realAngebotdatei.csv";
			config.uploadSftpFromPath(localFile, "/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/realAngebotdatei.csv");

			log.info("RealExport Angebote done!");
			
			/*
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			input = new FileInputStream(new File(config.outputDir + "RealAngebotdatei.csv"));
			config.ftpClient.storeFile("RealAngebotdatei.csv", input);
			config.logout();
			System.out.println("RealExport Angebot done!");
			*/

		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS! Real Export");
			e.printStackTrace();
		}
	log.info("---Finish runRealExport");
	}
}
