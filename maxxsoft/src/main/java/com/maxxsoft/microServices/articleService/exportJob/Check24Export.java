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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.Check24;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.model.MagentoProduct;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;
import com.maxxsoft.microServices.magentoService.repository.MagentoProductRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class Check24Export {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	MagentoService magentoService;

	@Autowired
	PriceService priceService;
	@Autowired
	CommonExportConfig config;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	MagentoProductRepository magentoProductRepository;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;


	List<String> pictureURLs;
	private static ArrayList<Check24> check24List;

	public void runCheck24Export() {
		check24List = new ArrayList<Check24>();
		pictureURLs = new ArrayList<>();
		log.info("Start Check24 export!");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
			Artikel erpArticle = erpArticleService.getArtikelByNumber(article.getNumber()).get();
			BigDecimal sellPriceCheck24 = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(),
					sellingPlatformRepository.findByName("Check24").get().getSellingPlatformId());
			String userfield08 = erpArticle.getUserfeld08();

			if (userfield08 != null) {

				if (userfield08.contains("Kinderzimmer") || userfield08.contains("Babyzimmer")) {
					pictureURLs = new ArrayList<>();
					List<MagentoMedia> magentoMediaList = magentoMediaRepository
							.findBySkuOrderByPositionAsc(article.getNumber());
					if (magentoMediaList.size() > 0) {
						magentoMediaList.forEach(media -> {
							pictureURLs.add(media.getFile());
						});
					}

					Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
							.findBySku(article.getNumber());
					if (magentoProductOptional.isPresent()) {
						config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
					}

					Check24 element = new Check24();

					element.setEan(article.getEan());
					element.setEindeutige_id(article.getNumber());

					int deliveryTime = article.getDeliveryTime();

					if (deliveryTime < 3)
						element.setLieferzeit("\"1-3 Tage\"");
					else if (deliveryTime < 5)
						element.setLieferzeit("\"3-5 Tage\"");
					else if (deliveryTime < 7)
						element.setLieferzeit("\"5-7 Tage\"");
					else if (deliveryTime < 10)
						element.setLieferzeit("\"7-10 Tage\"");
					else if (deliveryTime < 15)
						element.setLieferzeit("\"10-15 Tage\"");
					else if (deliveryTime < 20)
						element.setLieferzeit("\"15-20 Tage\"");
					else if (deliveryTime < 30)
						element.setLieferzeit("\"20-30 Tage\"");
					else if (deliveryTime < 40)
						element.setLieferzeit("\"30-40 Tage\"");
					else if (deliveryTime < 50)
						element.setLieferzeit("\"40-50 Tage\"");
					else if (deliveryTime < 60)
						element.setLieferzeit("\"50-60 Tage\"");
					else if (deliveryTime < 70)
						element.setLieferzeit("\"60-70 Tage\"");
					else if (deliveryTime < 80)
						element.setLieferzeit("\"70-80 Tage\"");
					else if (deliveryTime < 90)
						element.setLieferzeit("\"80-90 Tage\"");
					else
						element.setLieferzeit("\"mehr als 90 Tage\"");

					element.setPreis(sellPriceCheck24.toString());
					element.setVersandkosten("0.00");
					element.setBestand("10");
					element.setHersteller(null);
					element.setKategorie("\"" + config.categoryPath + "\"");
					element.setLink_produktseite(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=check24");
					element.setProduktname("\"" + article.getShortName().replaceAll("\"", "") + "\"");
					
					String desc_temp = article.getLongDescription().replaceAll("\"", "");
					desc_temp = desc_temp.replace("|", ",");
					desc_temp = desc_temp.replace("\n", " ");
					desc_temp = desc_temp.replace("\r", " ");
					
					element.setProduktbeschreibung("\"" + desc_temp + "\"");

					element.setUrl_produktbild1(config.mediaURL + pictureURLs.get(0));
					if (pictureURLs.size() >= 2)
						element.setUrl_produktbild2(config.mediaURL + pictureURLs.get(1));
					if (pictureURLs.size() >= 3)
						element.setUrl_produktbild3(config.mediaURL + pictureURLs.get(2));
					if (pictureURLs.size() >= 4)
						element.setUrl_produktbild4(config.mediaURL + pictureURLs.get(3));
					if (pictureURLs.size() >= 5)
						element.setUrl_produktbild5(config.mediaURL + pictureURLs.get(4));
					if (pictureURLs.size() >= 6)
						element.setUrl_produktbild6(config.mediaURL + pictureURLs.get(5));
					if (pictureURLs.size() >= 7)
						element.setUrl_produktbild7(config.mediaURL + pictureURLs.get(6));
					if (pictureURLs.size() >= 8)
						element.setUrl_produktbild8(config.mediaURL + pictureURLs.get(7));

					if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("spedition")) {
						element.setVersandart("Spedition");
					} else if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("paketdienst")) {
						element.setVersandart("Paketdienst");
					} else {
						element.setVersandart("Spedition");
					}

					check24List.add(element);
				}
			}
			}
		});

		articleSetRepository.findAll().forEach(articleSet -> {
			// if (articleSet.getArticleId() < 15) {
			Artikel erpArticle = erpArticleService.getArtikelByNumber(articleSet.getNumber()).get();
			int actualStock = articleSet.getStock();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellPriceCheck24 = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Check24").get().getSellingPlatformId());
			String userfield08 = erpArticle.getUserfeld08();

			if (userfield08 != null) {

				if (userfield08.contains("Kinderzimmer") || userfield08.contains("Babyzimmer")) {
					List<MagentoMedia> magentoMediaList = magentoMediaRepository
							.findBySkuOrderByPositionAsc(articleSet.getNumber());
					pictureURLs = new ArrayList<>();
					if (magentoMediaList.size() > 0) {
						magentoMediaList.forEach(media -> {
							pictureURLs.add(media.getFile());
						});
					}

					Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
							.findBySku(articleSet.getNumber());
					if (magentoProductOptional.isPresent()) {
						config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
					}

					Check24 element = new Check24();

					element.setEan(articleSet.getEan());
					element.setEindeutige_id(articleSet.getNumber());

					int deliveryTime = articleSet.getDeliveryTime();

					if (deliveryTime < 3)
						element.setLieferzeit("\"1-3 Tage\"");
					else if (deliveryTime < 5)
						element.setLieferzeit("\"3-5 Tage\"");
					else if (deliveryTime < 7)
						element.setLieferzeit("\"5-7 Tage\"");
					else if (deliveryTime < 10)
						element.setLieferzeit("\"7-10 Tage\"");
					else if (deliveryTime < 15)
						element.setLieferzeit("\"10-15 Tage\"");
					else if (deliveryTime < 20)
						element.setLieferzeit("\"15-20 Tage\"");
					else if (deliveryTime < 30)
						element.setLieferzeit("\"20-30 Tage\"");
					else if (deliveryTime < 40)
						element.setLieferzeit("\"30-40 Tage\"");
					else if (deliveryTime < 50)
						element.setLieferzeit("\"40-50 Tage\"");
					else if (deliveryTime < 60)
						element.setLieferzeit("\"50-60 Tage\"");
					else if (deliveryTime < 70)
						element.setLieferzeit("\"60-70 Tage\"");
					else if (deliveryTime < 80)
						element.setLieferzeit("\"70-80 Tage\"");
					else if (deliveryTime < 90)
						element.setLieferzeit("\"80-90 Tage\"");
					else
						element.setLieferzeit("\"mehr als 90 Tage\"");

					element.setPreis(sellPriceCheck24.toString());
					element.setVersandkosten("0.00");
					element.setBestand("10");
					element.setHersteller(null);
					element.setKategorie("\"" + config.categoryPath + "\"");
					element.setLink_produktseite(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=check24");					
					element.setProduktname("\"" + articleSet.getShortName().replaceAll("\"", "") + "\"");
					
					String desc_temp = articleSet.getLongDescription().replaceAll("\"", "");
					desc_temp = desc_temp.replace("|", ",");
					desc_temp = desc_temp.replace("\n", " ");
					desc_temp = desc_temp.replace("\r", " ");
					
					element.setProduktbeschreibung("\"" + desc_temp + "\"");

										
					element.setUrl_produktbild1(config.mediaURL + pictureURLs.get(0));
					if (pictureURLs.size() >= 2)
						element.setUrl_produktbild2(config.mediaURL + pictureURLs.get(1));
					if (pictureURLs.size() >= 3)
						element.setUrl_produktbild3(config.mediaURL + pictureURLs.get(2));
					if (pictureURLs.size() >= 4)
						element.setUrl_produktbild4(config.mediaURL + pictureURLs.get(3));
					if (pictureURLs.size() >= 5)
						element.setUrl_produktbild5(config.mediaURL + pictureURLs.get(4));
					if (pictureURLs.size() >= 6)
						element.setUrl_produktbild6(config.mediaURL + pictureURLs.get(5));
					if (pictureURLs.size() >= 7)
						element.setUrl_produktbild7(config.mediaURL + pictureURLs.get(6));
					if (pictureURLs.size() >= 8)
						element.setUrl_produktbild8(config.mediaURL + pictureURLs.get(7));

					if (erpArticle.getInfo().contains("spedition")) {
						element.setVersandart("Spedition");
					} else if (erpArticle.getInfo().contains("paketdienst")) {
						element.setVersandart("Paketdienst");
					} else {
						element.setVersandart("Spedition");
					}

					check24List.add(element);
				}
			}
			// }
		});

		try {

			// Real Produktdatei

			Map<String, Object> root = new HashMap<>();
			root.put("check24List", check24List);
			Configuration cfg = config.getConfiguration();

			Template check24Template = cfg.getTemplate("check24Template.csv");

			FileWriter writerCheck24 = new FileWriter(config.outputDir + "check24.csv", false);
			check24Template.process(root, writerCheck24);
			writerCheck24.close();

			// FTP Upload:
			String localFile = config.outputDir + "check24.csv";
			config.uploadSftpFromPath(localFile,
					"/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/check24.csv");

			log.info("Check24 export done!");

		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS! Check Export");
			e.printStackTrace();
		}
	}
}
