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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.Moebelde;
import com.maxxsoft.microServices.articleService.model.export.MoebeldeBiddingFeed;
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
public class MobeldeExport {

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
	
	double convesionRate = 0.50;
	int marketingPercentage = 12;
	int startingHour = 15;
	int endingHour = 23;

	private static ArrayList<Moebelde> moebeldeList = new ArrayList();
	private static ArrayList<MoebeldeBiddingFeed> moebeldeBiddingFeedList = new ArrayList();

	public void runMoebeldeExport() {
		moebeldeList = new ArrayList<Moebelde>();
		moebeldeBiddingFeedList = new ArrayList<MoebeldeBiddingFeed>();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		log.info("---Start moebel.de Export");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				int actualStock = article.getStock() - article.getPreOrder();
				Moebelde element = new Moebelde();
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
				element.setArtNr(article.getNumber());
				element.setArtName(article.getShortName().replace(";", ","));
				// Description without ";" and "|" and "\n" and "\r"
				String desc_temp = article.getLongDescription().replace(";", ",");
				desc_temp = desc_temp.replace("|", ",");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				element.setArtBeschreibung(desc_temp);
				if (actualStock > 0) {
					element.setArtMenge(String.valueOf(actualStock));
				}
				else element.setArtMenge("1");
				element.setArtUrl(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=mode");
				if (pictureURLs.size() >= 1)
				element.setArtImgUrl(config.mediaURL + pictureURLs.get(0));
				else log.error("MoebeldeExport.java: No Image there for Article "+ article.getNumber() +" -> Error Message");
				element.setArtPreis(sellPrice.toString());
				element.setArtWaehrung("EUR");

				element.setArtLieferkosten("0,00");

				element.setArtLieferzeit("Tage");
				if (actualStock > 0) {
					element.setArtLieferzeitWert("3-6");
				} else {
					element.setArtLieferzeitWert("31-42");
				}
				element.setArtVersandAt("ja");
				if (actualStock > 0) {
				element.setArtVerfuegbarkeit(String.valueOf(actualStock));
				}
				else element.setArtVerfuegbarkeit("1");
				element.setArtMarke("von moebel-guenstig24.de");
				element.setArtKategorie(config.categoryPath);

				if (pictureURLs.size() >= 2)
					element.setArtImgUrl2(config.mediaURL + pictureURLs.get(1));
				else
					element.setArtImgUrl2("");
				if (pictureURLs.size() >= 3)
					element.setArtImgUrl3(config.mediaURL + pictureURLs.get(2));
				else
					element.setArtImgUrl3("");
				if (pictureURLs.size() >= 4)
					element.setArtImgUrl4(config.mediaURL + pictureURLs.get(3));
				else
					element.setArtImgUrl4("");

				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setArtEan(article.getEan());
				else
					element.setArtEan("");
				if (config.farbe1 != "" && config.farbe2 != "")
					element.setArtFarbe(config.farbe1.replace("/", ", ") + ", " + config.farbe2.replace("/", ", "));
				else
					element.setArtFarbe(config.farbe1 + config.farbe2);

				element.setArtHauptfarbe(config.farbe1);
				element.setArtStil(config.stilwelt);
				element.setArtExtras(config.besonderheit.replace("/", ", "));
				element.setArtBreite(config.breite);
				element.setArtBreiteEinheit("cm");
				element.setArtTiefe(config.tiefe);
				element.setArtTiefeEinheit("cm");
				element.setArtHoehe(config.tiefe);
				element.setArtHoeheEinheit("cm");
				element.setArtStreichpreis("");
				// Is on discount?
				//if (isAktion) {
				// element.setArtStreichpreis(String.valueOf(grundPreis.floatValue()));
				// element.setArtPreis(String.valueOf(aktionsPreis.floatValue()));
				// } else element.setArtStreichpreis("");
				//
				//if (actualStock > 0)
					moebeldeList.add(element);

				// Bidding Feed
				MoebeldeBiddingFeed elementbf = new MoebeldeBiddingFeed();
				elementbf.setArticleNumber(article.getNumber());
				// elementbf.setBidDesktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,2000)));
				// // stand 15.01.2020
				// elementbf.setBidDesktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,5000)));
				
				
				// estimated conversion rate 0,35%, 12% marketing costs, calculation: 
				if (hour >= startingHour && hour <= endingHour) {
				if (actualStock > 0) {
					BigDecimal cpc = new BigDecimal(0);
					cpc = sellPrice.multiply(BigDecimal.valueOf(marketingPercentage)).divide(BigDecimal.valueOf(100));
					cpc = cpc.multiply(BigDecimal.valueOf(convesionRate)).divide(BigDecimal.valueOf(100));
					if (cpc.doubleValue() <= 0.50) {
						elementbf.setBidDesktop(String.valueOf(cpc.doubleValue()));
					}
					else elementbf.setBidDesktop("0.50");
				}
				else {
					// goods which are not on stock always 0,04 euro
					elementbf.setBidDesktop("0.02");
				}
				} else {
					elementbf.setBidDesktop("0.02");
				}
				// war mal 100
				elementbf.setFactorMobile("1");

				moebeldeBiddingFeedList.add(elementbf);
			}
		});
		
		
		articleSetRepository.findAll().forEach(articleSet -> {
				int actualStock = articleSet.getStock();
				Moebelde element = new Moebelde();
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
				element.setArtNr(articleSet.getNumber());
				element.setArtName(articleSet.getShortName().replace(";", ","));
				// Description without ";" and "|" and "\n" and "\r"
				String desc_temp = articleSet.getLongDescription().replace(";", ",");
				desc_temp = desc_temp.replace("|", ",");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				element.setArtBeschreibung(desc_temp);
				if (actualStock > 0) {
					element.setArtMenge(String.valueOf(actualStock));
				}
				else element.setArtMenge("1");
				element.setArtUrl(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=mode");
				if (pictureURLs.size() >= 1)
				element.setArtImgUrl(config.mediaURL + pictureURLs.get(0));
				else log.error("MoebeldeExport.java: No Image there for Article "+ articleSet.getNumber() +" -> Error Message");
				element.setArtPreis(sellPrice.toString());
				element.setArtWaehrung("EUR");

				element.setArtLieferkosten("0,00");

				element.setArtLieferzeit("Tage");
				if (actualStock > 0) {
					element.setArtLieferzeitWert("3-6");
				} else {
					element.setArtLieferzeitWert("31-42");
				}
				element.setArtVersandAt("ja");
				if (actualStock > 0) {
				element.setArtVerfuegbarkeit(String.valueOf(actualStock));
				}
				else element.setArtVerfuegbarkeit("1");
				element.setArtMarke("von moebel-guenstig24.de");
				element.setArtKategorie(config.categoryPath);

				if (pictureURLs.size() >= 2)
					element.setArtImgUrl2(config.mediaURL + pictureURLs.get(1));
				else
					element.setArtImgUrl2("");
				if (pictureURLs.size() >= 3)
					element.setArtImgUrl3(config.mediaURL + pictureURLs.get(2));
				else
					element.setArtImgUrl3("");
				if (pictureURLs.size() >= 4)
					element.setArtImgUrl4(config.mediaURL + pictureURLs.get(3));
				else
					element.setArtImgUrl4("");

				if (articleSet.getEan() != null && articleSet.getEan().length() >= 2)
					element.setArtEan(articleSet.getEan());
				else
					element.setArtEan("");
				if (config.farbe1 != "" && config.farbe2 != "")
					element.setArtFarbe(config.farbe1.replace("/", ", ") + ", " + config.farbe2.replace("/", ", "));
				else
					element.setArtFarbe(config.farbe1 + config.farbe2);

				element.setArtHauptfarbe(config.farbe1);
				element.setArtStil(config.stilwelt);
				element.setArtExtras(config.besonderheit.replace("/", ", "));
				element.setArtBreite(config.breite);
				element.setArtBreiteEinheit("cm");
				element.setArtTiefe(config.tiefe);
				element.setArtTiefeEinheit("cm");
				element.setArtHoehe(config.tiefe);
				element.setArtHoeheEinheit("cm");
				element.setArtStreichpreis("");
				// Is on discount?
				//if (isAktion) {
				// element.setArtStreichpreis(String.valueOf(grundPreis.floatValue()));
				// element.setArtPreis(String.valueOf(aktionsPreis.floatValue()));
				// } else element.setArtStreichpreis("");
				//
				//if (actualStock > 0)
					moebeldeList.add(element);

				// Bidding Feed
				MoebeldeBiddingFeed elementbf = new MoebeldeBiddingFeed();
				elementbf.setArticleNumber(articleSet.getNumber());
				// elementbf.setBidDesktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,2000)));
				// // stand 15.01.2020
				// elementbf.setBidDesktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,5000)));
				
				
				// estimated conversion rate 0,35%, 12% marketing costs, calculation: 
				if (hour >= startingHour && hour <= endingHour) {
				if (actualStock > 0) {
					BigDecimal cpc = new BigDecimal(0);
					cpc = sellPrice.multiply(BigDecimal.valueOf(marketingPercentage)).divide(BigDecimal.valueOf(100));
					cpc = cpc.multiply(BigDecimal.valueOf(convesionRate)).divide(BigDecimal.valueOf(100));
					if (cpc.doubleValue() <= 0.50) {
						elementbf.setBidDesktop(String.valueOf(cpc.doubleValue()));
					}
					else elementbf.setBidDesktop("0.50");
				}
				else {
					// goods which are not on stock always 0,04 euro
					elementbf.setBidDesktop("0.02");
				}
				} else {
					elementbf.setBidDesktop("0.02");
				}
				// war mal 100
				elementbf.setFactorMobile("1");
				
				
				moebeldeBiddingFeedList.add(elementbf);
		});

		try {
			Configuration cfg = config.getConfiguration();
			Map<String, Object> root = new HashMap<>();
			root.put("moebeldeList", moebeldeList);

			Template moebeldeTemplate = cfg.getTemplate("moebeldeTemplate.csv");

			FileWriter writerMoebelde = new FileWriter(config.outputDir + "moebelde.csv", false);
			moebeldeTemplate.process(root, writerMoebelde);
			writerMoebelde.close();

			
			// FTP Upload:
			String localFile = config.outputDir + "moebelde.csv";
			config.uploadSftpFromPath(localFile,"/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/moebelde.csv");

			log.info("moebel.de productfile upload done!");

			root.clear();
			root.put("moebeldeBiddingFeedList", moebeldeBiddingFeedList);

			Template moebeldeBiddingFeedTemplate = cfg.getTemplate("moebeldeBiddingFeedTemplate.csv");

			FileWriter writerMoebelde2 = new FileWriter(config.outputDir + "moebeldebiddingfeed.csv", false);
			moebeldeBiddingFeedTemplate.process(root, writerMoebelde2);
			writerMoebelde2.close();

			// FTP Upload:
			localFile = config.outputDir + "moebeldebiddingfeed.csv";
			config.uploadSftpFromPath(localFile, "/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/moebeldebiddingfeed.csv");

			log.info("moebel.de bidding feed upload done!");

		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS! Moebel.de Export");
			e.printStackTrace();
		}
	}
}
