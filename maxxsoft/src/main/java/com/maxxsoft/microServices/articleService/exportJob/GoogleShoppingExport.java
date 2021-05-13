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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.GoogleShopping;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.articleService.utility.MapArticleImagesWithArticleFromFtp;
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
public class GoogleShoppingExport {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	ErpArticleStueckRepository erpArticleStueckRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	MapArticleImagesWithArticleFromFtp mapArticleImagesWithArticleFromFtp;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	ImageService imageService;

	@Autowired
	PriceService priceService;

	@Autowired
	CommonExportConfig config;

	@Autowired
	private MagentoProductRepository magentoProductRepository;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;
	
	@Value("${spring.profiles.active}")
	private String activeProfile;

	String imageLink;

	private static ArrayList<GoogleShopping> googleShoppingList;
	private static ArrayList<GoogleShopping> googleShoppingList_noIdentifier;

	public void runExportDataGoogleShopping() {
		googleShoppingList = new ArrayList<GoogleShopping>();
		googleShoppingList_noIdentifier = new ArrayList<GoogleShopping>();
		// Run for Article
		log.info("---Start runExportDataGoogleShopping");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				GoogleShopping element = new GoogleShopping();
				int actualStock = article.getStock() - article.getPreOrder();
				BigDecimal sellPrice = priceService.getSellingOrSalePrice(article.getNumber(), actualStock, article.getBuyPrice(), sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
				Artikel erpArticle = erpArticleService.getArtikelByNumber(article.getNumber()).get();
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(article.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				element.setId(article.getNumber());
				element.setTitle(article.getShortName());

				// Description without ";" and "|" and "\n" and "\r"
				String desc_temp = article.getLongDescription().replace(";", ",");
				desc_temp = desc_temp.replace("|", ",");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");

				element.setDescription(desc_temp);
				element.setLink(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=gsde");
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(article.getNumber());
				if (magentoMediaList.size() > 0) {
					MagentoMedia mediaWithMinPosition = magentoMediaList.stream()
							.min(Comparator.comparing(MagentoMedia::getPosition))
							.orElseThrow(NoSuchElementException::new);
					imageLink = mediaWithMinPosition.getFile();
				}
				element.setImageLink(config.mediaURL + imageLink);

				if (actualStock > 0) {
					element.setAvailability("in stock");
				} else {
					element.setAvailability("preorder");
				}

				element.setPrice(sellPrice.toString());
				element.setGoogleProductCategory("Möbel");
				element.setBrand("moebel-guenstig24");
				element.setMpn("");
				element.setGtin("");
				element.setCondition("new");
				element.setAdult("no");

				element.setShipping("DE:::0,00");

				element.setIdentifierExists("Nein");

				element.setSalePrice("");

				int deliveryTime = article.getDeliveryTime();
				/*if (deliveryTime >= 0 && deliveryTime <= 10) {
					element.setCustomLabel1("0 bis 10 Tage Lieferzeit");
				} else if (deliveryTime >= 11 && deliveryTime <= 22) {
					element.setCustomLabel1("11 bis 22 Tage Lieferzeit");
				} else
					element.setCustomLabel1("22 und mehr Tage Lieferzeit");*/
				if (deliveryTime >= 0 && deliveryTime <= 15) {
					element.setCustomLabel1("0 bis 15 Tage Lieferzeit");
				} else if (deliveryTime >= 16 && deliveryTime <= 30) {
					element.setCustomLabel1("16 bis 30 Tage Lieferzeit");
				} else
					element.setCustomLabel1("31 und mehr Tage Lieferzeit");

				int sellPriceInt = sellPrice.intValue();
				if (config.isBetween(sellPriceInt, 0, 120)) {
					element.setCustomLabel2("0 und 120 Euro Artikelpreis");
				} else if (config.isBetween(sellPriceInt, 121, 250)) {
					element.setCustomLabel2("121 und 250 Euro Artikelpreis");
				} else if (config.isBetween(sellPriceInt, 251, 500)) {
					element.setCustomLabel2("251 und 500 Euro Artikelpreis");
				} else if (config.isBetween(sellPriceInt, 501, 800)) {
					element.setCustomLabel2("501 und 800 Euro Artikelpreis");
				} else
					element.setCustomLabel2("801 Euro und mehr Artikelpreis");

				int ordersLast60Days = erpArticleService.getOrderLastxDays(erpArticle.getRecId(), 60);
				if (ordersLast60Days == 0) {
					element.setCustomLabel3("0 Bestellungen in den letzen 60 Tagen");
				} else if (config.isBetween(ordersLast60Days, 1, 3)) {
					element.setCustomLabel3("1 bis 3 Bestellungen in den letzen 60 Tagen");
				} else if (config.isBetween(ordersLast60Days, 4, 8)) {
					element.setCustomLabel3("4 bis 8 Bestellungen in den letzen 60 Tagen");
				} else if (config.isBetween(ordersLast60Days, 9, 15)) {
					element.setCustomLabel3("9 bis 15 Bestellungen in den letzen 60 Tagen");
				} else if (config.isBetween(ordersLast60Days, 16, 30)) {
					element.setCustomLabel3("16 bis 30 Bestellungen in den letzen 60 Tagen");
				} else
					element.setCustomLabel3("31 und mehr Bestellungen in den letzen 60 Tagen");

				// CustomLabel4 noch unbelegt
				element.setCustomLabel4("");

				// CustomLabel0 Produktart
				// need a new field.
				// For time being, fixed it with existing way.
				// Need a flag for Low ROAS
				if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
					element.setCustomLabel0("lowROAS_Produkte");
				else
					element.setCustomLabel0("einzigartige_Produkte");
				googleShoppingList_noIdentifier.add((GoogleShopping) element.clone());

				if (article.getEan() != null && article.getEan().length() >= 2) {
					element.setIdentifierExists("Ja");
					element.setMpn(article.getNumber());
					element.setGtin(article.getEan());
					element.setId(article.getNumber() + "_" + article.getEan());
					// CustomLabel0 Produktart
					if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
						element.setCustomLabel0("lowROAS_Produkte");
					else
						element.setCustomLabel0("normale_Produkte");
					googleShoppingList.add((GoogleShopping) element.clone());

				}

				// add new field GoogleGtins
				// For time being, fixed it with existing way.
				// weitere Artikel hinzfügen falls weiter GTINs vorhanden sind:
				if (StringUtils.isNotEmpty(config.gtin)) {
					StringTokenizer tokenizer_gtin = new StringTokenizer(config.gtin, ";");
					while (tokenizer_gtin.hasMoreElements()) {
						String gtin_fremdartikel = tokenizer_gtin.nextToken();
						if (!gtin_fremdartikel.equals(article.getEan())) {
							element = (GoogleShopping) element.clone();
							element.setGtin(gtin_fremdartikel);
							element.setId(article.getNumber() + "_" + gtin_fremdartikel);
							// CustomLabel0 Produktart
							if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
								element.setCustomLabel0("lowROAS_Produkte");
							else
								element.setCustomLabel0("normale_Produkte");
							googleShoppingList.add(element);
						}
					}
				}
			}

		});

		// Run for ArticleSets
		articleSetRepository.findAll().forEach(articleSet -> {
			GoogleShopping element = new GoogleShopping();
			int actualStock = articleSet.getStock();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellPrice = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
			// Saving actual stock in stock. This value is equal to the mimimum
			// actual stock among all the partlist
			Artikel erpArticle = erpArticleService.getArtikelByNumber(articleSet.getNumber()).get();
			Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
					.findBySku(articleSet.getNumber());
			if (magentoProductOptional.isPresent()) {
				config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
			}
			element.setId(articleSet.getNumber());
			element.setTitle(articleSet.getShortName());

			// Description without ";" and "|" and "\n" and "\r"
			String desc_temp = articleSet.getLongDescription().replace(";", ",");
			desc_temp = desc_temp.replace("|", ",");
			desc_temp = desc_temp.replace("\n", " ");
			desc_temp = desc_temp.replace("\r", " ");

			element.setDescription(desc_temp);
			element.setLink(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=gsde");
			List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(articleSet.getNumber());
			if (magentoMediaList.size() > 0) {
				MagentoMedia mediaWithMinPosition = magentoMediaList.stream()
						.min(Comparator.comparing(MagentoMedia::getPosition)).orElseThrow(NoSuchElementException::new);
				imageLink = mediaWithMinPosition.getFile();
			}
			element.setImageLink(config.mediaURL + imageLink);

			if (actualStock > 0) {
				element.setAvailability("in stock");
			} else {
				element.setAvailability("preorder");
			}

			element.setPrice(sellPrice.toString());
			element.setGoogleProductCategory("Möbel");
			element.setBrand("moebel-guenstig24");
			element.setMpn("");
			element.setGtin("");
			element.setCondition("new");
			element.setAdult("no");

			element.setShipping("DE:::0,00");

			element.setIdentifierExists("Nein");

			element.setSalePrice("");

			int deliveryTime = articleSet.getDeliveryTime();
			/*if (deliveryTime >= 0 && deliveryTime <= 10) {
				element.setCustomLabel1("0 bis 10 Tage Lieferzeit");
			} else if (deliveryTime >= 11 && deliveryTime <= 22) {
				element.setCustomLabel1("11 bis 22 Tage Lieferzeit");
			} else
				element.setCustomLabel1("22 und mehr Tage Lieferzeit");
			element.setCustomLabel1("22 und mehr Tage Lieferzeit");*/
			if (deliveryTime >= 0 && deliveryTime <= 15) {
				element.setCustomLabel1("0 bis 15 Tage Lieferzeit");
			} else if (deliveryTime >= 16 && deliveryTime <= 30) {
				element.setCustomLabel1("16 bis 30 Tage Lieferzeit");
			} else
				element.setCustomLabel1("31 und mehr Tage Lieferzeit");

			int sellPriceInt = sellPrice.intValue();
			if (config.isBetween(sellPriceInt, 0, 120)) {
				element.setCustomLabel2("0 und 120 Euro Artikelpreis");
			} else if (config.isBetween(sellPriceInt, 121, 250)) {
				element.setCustomLabel2("121 und 250 Euro Artikelpreis");
			} else if (config.isBetween(sellPriceInt, 251, 500)) {
				element.setCustomLabel2("251 und 500 Euro Artikelpreis");
			} else if (config.isBetween(sellPriceInt, 501, 800)) {
				element.setCustomLabel2("501 und 800 Euro Artikelpreis");
			} else
				element.setCustomLabel2("801 Euro und mehr Artikelpreis");

			int ordersLast60Days = erpArticleService.getOrderLastxDays(erpArticle.getRecId(), 60);
			if (ordersLast60Days == 0) {
				element.setCustomLabel3("0 Bestellungen in den letzen 60 Tagen");
			} else if (config.isBetween(ordersLast60Days, 1, 3)) {
				element.setCustomLabel3("1 bis 3 Bestellungen in den letzen 60 Tagen");
			} else if (config.isBetween(ordersLast60Days, 4, 8)) {
				element.setCustomLabel3("4 bis 8 Bestellungen in den letzen 60 Tagen");
			} else if (config.isBetween(ordersLast60Days, 9, 15)) {
				element.setCustomLabel3("9 bis 15 Bestellungen in den letzen 60 Tagen");
			} else if (config.isBetween(ordersLast60Days, 16, 30)) {
				element.setCustomLabel3("16 bis 30 Bestellungen in den letzen 60 Tagen");
			} else
				element.setCustomLabel3("31 und mehr Bestellungen in den letzen 60 Tagen");

			// CustomLabel4 noch unbelegt
			element.setCustomLabel4("");

			// CustomLabel0 Produktart
			// need a new field.
			// For time being, fixed it with existing way.
			// Need a flag for Low ROAS
			if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
				element.setCustomLabel0("lowROAS_Produkte");
			else
				element.setCustomLabel0("einzigartige_Produkte");
			googleShoppingList_noIdentifier.add((GoogleShopping) element.clone());

			if (articleSet.getEan() != null && articleSet.getEan().length() >= 2) {
				element.setIdentifierExists("Ja");
				element.setMpn(articleSet.getNumber());
				element.setGtin(articleSet.getEan());
				element.setId(articleSet.getNumber() + "_" + articleSet.getEan());
				// CustomLabel0 Produktart
				if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
					element.setCustomLabel0("lowROAS_Produkte");
				else
					element.setCustomLabel0("normale_Produkte");
				googleShoppingList.add((GoogleShopping) element.clone());

			}

			// add new field GoogleGtins
			// For time being, fixed it with existing way.
			// weitere Artikel hinzfügen falls weiter GTINs vorhanden sind:
			if (StringUtils.isNotEmpty(config.gtin)) {
				StringTokenizer tokenizer_gtin = new StringTokenizer(config.gtin, ";");
				while (tokenizer_gtin.hasMoreElements()) {
					String gtin_fremdartikel = tokenizer_gtin.nextToken();
					if (!gtin_fremdartikel.equals(articleSet.getEan())) {
						element = (GoogleShopping) element.clone();
						element.setGtin(gtin_fremdartikel);
						element.setId(articleSet.getNumber() + "_" + gtin_fremdartikel);
						// CustomLabel0 Produktart
						if (erpArticle.getInfo() != null && erpArticle.getInfo().contains("lowROAS"))
							element.setCustomLabel0("lowROAS_Produkte");
						else
							element.setCustomLabel0("normale_Produkte");
						googleShoppingList.add(element);
					}
				}
			}

		});

		try {
			Map<String, Object> root = new HashMap<>();
			log.info("---Start buildingGoogleShoppingExport Files");
			root.put("googleShoppingList", googleShoppingList);
			Configuration cfg = config.getConfiguration();

			Template googleShoppingTemplate = cfg.getTemplate("googleShoppingTemplate.csv");

			FileWriter writerGoogleShopping = new FileWriter(config.outputDir + "googleShopping.csv", false);
			googleShoppingTemplate.process(root, writerGoogleShopping);
			writerGoogleShopping.close();

			// List googleShoppingList_noIdentifier
			root = new HashMap<>();
			root.put("googleShoppingList", googleShoppingList_noIdentifier);

			writerGoogleShopping = new FileWriter(config.outputDir + "googleShopping_einzigartigeProdukte.csv", false);
			googleShoppingTemplate.process(root, writerGoogleShopping);
			writerGoogleShopping.close();

			log.info("---Complete buildingGoogleShoppingExport Files");
			
			//System.out.println("-----ActiveProfile is: " + activeProfile);
			if (activeProfile.equals("prod") || activeProfile.equals("maxprod")) {
			// FTP Upload:
			log.info("---Start uploading GoogleShoppingExport Files");
			config.connect("uploads.google.com", 21, "mc-ftp-8309566","vjNaP3NPqjZb5or9p");
			InputStream input = new FileInputStream(new File(config.outputDir + "googleShopping.csv"));
			config.ftpClient.storeFile("googleShopping.csv", input);
			config.logout();
				
			config.connect("uploads.google.com", 21, "mc-ftp-8309566","vjNaP3NPqjZb5or9p");
			input = new FileInputStream(new File(config.outputDir + "googleShopping_einzigartigeProdukte.csv"));
			config.ftpClient.storeFile("googleShopping_einzigartigeProdukte.csv", input);
			config.logout();
			log.info("---Complete uploading GoogleShoppingExport Files");
			}
			
			log.info("---Complete runExportDataGoogleShopping");

		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}

}
