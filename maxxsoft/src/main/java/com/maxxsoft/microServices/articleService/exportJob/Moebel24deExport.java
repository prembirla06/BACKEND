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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.Moebel24de;
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
public class Moebel24deExport {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	ErpArticleStueckRepository erpArticleStueckRepository;
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
	private MagentoProductRepository magentoProductRepository;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;

	FTPClient ftp = null;
	byte pic[] = null;
	List<String> pictureURLs = new ArrayList<>();
	
	double convesionRate = 0.35;
	int marketingPercentage = 12;
	int startingHour = 0;
	int endingHour = 24;

	private static ArrayList<Moebel24de> moebel24deList = new ArrayList<Moebel24de>();

	public void runMoebel24deExport() {
		moebel24deList = new ArrayList<Moebel24de>();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		log.info("---Start moebel24.de Export");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				int actualStock = article.getStock() - article.getPreOrder();
				BigDecimal sellPrice = priceService.getSellingOrSalePrice(article.getNumber(), actualStock, article.getBuyPrice(), sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(article.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(article.getNumber());
				pictureURLs = new ArrayList<>();
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(media.getFile());
					});
				}
				Moebel24de element = new Moebel24de();
				element.setName(article.getShortName());
				element.setSku_id(article.getNumber());
				element.setMaster_sku_id(""); // wird nicht benötigt?
				element.setPrice(sellPrice.toString());
				element.setAlternate_price(""); // wird nicht benötigt
				float tax = (sellPrice.floatValue() / 100) * 19;
				element.setTax(String.valueOf(tax));
				element.setDelivery_cost("0.00");
				int deliveryTime = article.getDeliveryTime();
				int deliveryTimeWorkdays = (BigDecimal.valueOf(deliveryTime).multiply(BigDecimal.valueOf(0.71))).intValue();
				element.setDelivery_time(deliveryTimeWorkdays + " Tage");
				element.setCategory_path(config.categoryPath);
				String desc_temp = article.getLongDescription().replace(",", " ");
				desc_temp = desc_temp.replace("|", " ");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				element.setDescription(desc_temp);
				element.setProduct_url(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=m24d");
				element.setClean_url(config.urlWebsite + "/" + config.urlProduct + ".html");
				element.setColor(config.farbe1);
				element.setMaterial("siehe Artikelbeschreibung");
				element.setLength(config.tiefe);
				element.setWidth(config.breite);
				element.setHeight(config.hoehe);
				element.setProduct_image_url_1(config.mediaURL + pictureURLs.get(0));
				if (pictureURLs.size() >= 2)
					element.setProduct_image_url_2(config.mediaURL + pictureURLs.get(1));
				if (pictureURLs.size() >= 3)
					element.setProduct_image_url_3(config.mediaURL + pictureURLs.get(2));
				if (pictureURLs.size() >= 4)
					element.setProduct_image_url_4(config.mediaURL + pictureURLs.get(3));
				if (pictureURLs.size() >= 5)
					element.setProduct_image_url_5(config.mediaURL + pictureURLs.get(4));
				element.setEan(article.getEan());
				element.setBrand("moebel-guenstig24.de");
				// element.setCpc(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,2000)));
				// element.setCpc(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,5000)));
				if (hour >= startingHour && hour <= endingHour) {
					if (actualStock > 0) {
						BigDecimal cpc = new BigDecimal(0);
						cpc = sellPrice.multiply(BigDecimal.valueOf(marketingPercentage)).divide(BigDecimal.valueOf(100));
						cpc = cpc.multiply(BigDecimal.valueOf(convesionRate)).divide(BigDecimal.valueOf(100));
						if (cpc.doubleValue() <= 0.50) {
							element.setCpc(String.valueOf(cpc.doubleValue()));
						}
						else element.setCpc("0.50");
					}
					else {
						// goods which are not on stock always 0,04 euro
						element.setCpc("0.02");
					}
					} else {
						element.setCpc("0.02");
					}
				
				
				
				element.setAvailability("auf Lager");
				if (actualStock > 0)
					moebel24deList.add(element);
			}
		});
		
		articleSetRepository.findAll().forEach(articleSet -> {
			int actualStock = articleSet.getStock();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellPrice = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
			Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
					.findBySku(articleSet.getNumber());
			if (magentoProductOptional.isPresent()) {
				config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
			}
			List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(articleSet.getNumber());
			pictureURLs = new ArrayList<>();
			if (magentoMediaList.size() > 0) {
				magentoMediaList.forEach(media -> {
					pictureURLs.add(media.getFile());
				});
			}
			Moebel24de element = new Moebel24de();
			element.setName(articleSet.getShortName());
			element.setSku_id(articleSet.getNumber());
			element.setMaster_sku_id(""); // wird nicht benötigt?
			element.setPrice(sellPrice.toString());
			element.setAlternate_price(""); // wird nicht benötigt
			float tax = (sellPrice.floatValue() / 100) * 19;
			element.setTax(String.valueOf(tax));
			element.setDelivery_cost("0.00");
			int deliveryTime = articleSet.getDeliveryTime();
			int deliveryTimeWorkdays = (BigDecimal.valueOf(deliveryTime).multiply(BigDecimal.valueOf(0.71))).intValue();
			element.setDelivery_time(deliveryTimeWorkdays + " Tage");
			element.setCategory_path(config.categoryPath);
			String desc_temp = articleSet.getLongDescription().replace(",", " ");
			desc_temp = desc_temp.replace("|", " ");
			desc_temp = desc_temp.replace("\n", " ");
			desc_temp = desc_temp.replace("\r", " ");
			element.setDescription(desc_temp);
			element.setProduct_url(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=m24d");
			element.setClean_url(config.urlWebsite + "/" + config.urlProduct + ".html");
			element.setColor(config.farbe1);
			element.setMaterial("siehe Artikelbeschreibung");
			element.setLength(config.tiefe);
			element.setWidth(config.breite);
			element.setHeight(config.hoehe);
			element.setProduct_image_url_1(config.mediaURL + pictureURLs.get(0));
			if (pictureURLs.size() >= 2)
				element.setProduct_image_url_2(config.mediaURL + pictureURLs.get(1));
			if (pictureURLs.size() >= 3)
				element.setProduct_image_url_3(config.mediaURL + pictureURLs.get(2));
			if (pictureURLs.size() >= 4)
				element.setProduct_image_url_4(config.mediaURL + pictureURLs.get(3));
			if (pictureURLs.size() >= 5)
				element.setProduct_image_url_5(config.mediaURL + pictureURLs.get(4));
			element.setEan(articleSet.getEan());
			element.setBrand("moebel-guenstig24.de");
			// element.setCpc(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,2000)));
			// element.setCpc(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,5000)));
			if (hour >= startingHour && hour <= endingHour) {
				if (actualStock > 0) {
					BigDecimal cpc = new BigDecimal(0);
					cpc = sellPrice.multiply(BigDecimal.valueOf(marketingPercentage)).divide(BigDecimal.valueOf(100));
					cpc = cpc.multiply(BigDecimal.valueOf(convesionRate)).divide(BigDecimal.valueOf(100));
					if (cpc.doubleValue() <= 0.50) {
						element.setCpc(String.valueOf(cpc.doubleValue()));
					}
					else element.setCpc("0.50");
				}
				else {
					// goods which are not on stock always 0,04 euro
					element.setCpc("0.02");
				}
				} else {
					element.setCpc("0.02");
				}
			
			element.setAvailability("auf Lager");
			if (actualStock > 0)
				moebel24deList.add(element);
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("moebel24deList", moebel24deList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("moebel24deTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "moebel24de.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			String localFile = config.outputDir + "moebel24de.csv";
			config.uploadSftpFromPath(localFile,"/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/moebel24de.csv");
			log.info("moebel24de Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS! moebel24.de Export");
			e.printStackTrace();
		}
	}
}
