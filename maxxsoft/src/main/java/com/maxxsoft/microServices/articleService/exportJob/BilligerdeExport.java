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
import com.maxxsoft.microServices.articleService.model.export.Billigerde;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
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
public class BilligerdeExport {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private MagentoProductRepository magentoProductRepository;

	@Autowired
	PriceService priceService;
	@Autowired
	CommonExportConfig config;

	FTPClient ftp = null;
	byte pic[] = null;
	List<String> pictureURLs = new ArrayList<>();

	private static ArrayList<Billigerde> billigerdeList = new ArrayList();

	public void runBilligerdeExport() {
		articleRepository.findAll().forEach(article -> {
			if (article.getArticleId() < 15) {
				BigDecimal sellPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(), 1L);
				Artikel erpArticle = erpArticleService.getArtikelByNumber(article.getNumber()).get();
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(article.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(article.getNumber());
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(media.getFile());
					});
				}

				int actualStock = article.getStock() - article.getPreOrder();
				Billigerde element = new Billigerde();
				element.setAid(String.valueOf(erpArticle.getRecId()));
				element.setName(article.getShortName().replaceAll("|", " "));
				element.setPrice(sellPrice.toString());
				element.setLink(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=bide");
				element.setShop_cat(config.categoryPath);
				element.setBrand("von moebel-guenstig24.de");
				element.setMpn(article.getNumber());
				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setGtin(article.getEan());
				else
					element.setGtin("");
				element.setImage(config.mediaURL + pictureURLs.get(0));
				if (actualStock > 0) {
					element.setDlv_time("3-6 Tage");
				} else {
					element.setDlv_time("3-6 Wochen");
				}
				element.setDlv_cost("0.00");
				element.setDlv_cost_at("29.90");

				element.setDesc(article.getShortDescription());
				element.setOld_price("");
				if (config.besonderheit.contains("mit Beleuchtung"))
					element.setEek("A+++ bis A");
				else
					element.setEek("");
				element.setSize(config.breite + " " + config.hoehe + " " + config.tiefe);
				element.setColor(config.farbe1);
				element.setMaterial("siehe Artikelbeschreibung");
				element.setBilligerde_class(config.lastCategory);
				element.setFeatures(config.besonderheit);
				element.setStyle(config.stilwelt);

				if (actualStock > 0) {
					billigerdeList.add(element);
				}

			}
		});

		// articleSetRepository.findAll().forEach(articleSet -> {
		// BigDecimal setBuyPrice =
		// priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
		// BigDecimal sellPrice =
		// priceService.getSellingPrice(articleSet.getNumber(), setBuyPrice,
		// 1L);
		// Artikel erpArticle =
		// erpArticleService.getArtikelByNumber(articleSet.getNumber()).get();
		// Optional<EbayArticle> magentoProductOptional =
		// magentoProductRepository
		// .findBySku(articleSet.getNumber());
		// if (magentoProductOptional.isPresent()) {
		// config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
		// }
		// List<MagentoMedia> magentoMediaList =
		// magentoMediaRepository.findBySku(articleSet.getNumber());
		// if (magentoMediaList.size() > 0) {
		// magentoMediaList.forEach(media -> {
		// pictureURLs.add(media.getFile());
		// });
		// }
		//
		// Saving actual stock in stock. This value is equal to the mimimum
		// actual stock among all the partlist
		// int actualStock = articleSet.getStock();
		// Billigerde element = new Billigerde();
		// element.setAid(String.valueOf(erpArticle.getRecId()));
		// element.setName(articleSet.getShortName().replaceAll("|", " "));
		// element.setPrice(sellPrice.toString());
		// element.setLink(
		// config.urlWebsite + "/" + config.urlProduct +
		// ".html" + "?utm_source=bide");
		// element.setShop_cat(config.categoryPath);
		// element.setBrand("von moebel-guenstig24.de");
		// element.setMpn(articleSet.getNumber());
		// if (articleSet.getEan() != null && articleSet.getEan().length() >= 2)
		// element.setGtin(articleSet.getEan());
		// else
		// element.setGtin("");
		// element.setImage(config.mediaURL + pictureURLs.get(0));
		// if (actualStock > 0) {
		// element.setDlv_time("3-6 Tage");
		// } else {
		// element.setDlv_time("3-6 Wochen");
		// }
		// element.setDlv_cost("0.00");
		// element.setDlv_cost_at("29.90");
		//
		// element.setDesc(articleSet.getShortDescription());
		// element.setOld_price("");
		// if (config.besonderheit.contains("mit Beleuchtung"))
		// element.setEek("A+++ bis A");
		// else
		// element.setEek("");
		// element.setSize(
		// config.breite + " " + config.hoehe + " " +
		// config.tiefe);
		// element.setColor(config.farbe1);
		// element.setMaterial("siehe Artikelbeschreibung");
		// element.setBilligerde_class(config.lastCategory);
		// element.setFeatures(config.besonderheit);
		// element.setStyle(config.stilwelt);
		//
		// if (actualStock > 0)
		// billigerdeList.add(element);
		// });

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("billigerdeList", billigerdeList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("billigerdeTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "billigerde.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "billigerde.csv"));
			config.ftpClient.storeFile("billigerde.csv", input);
			config.logout();
			log.info("billigerde Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
