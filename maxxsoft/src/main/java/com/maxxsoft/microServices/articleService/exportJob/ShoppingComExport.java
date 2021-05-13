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
import com.maxxsoft.microServices.articleService.model.export.Shoppingcom;
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
public class ShoppingComExport {

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
	private MagentoProductRepository magentoProductRepository;

	FTPClient ftp = null;
	byte pic[] = null;
	List<String> pictureURLs = new ArrayList<>();

	private static ArrayList<Shoppingcom> shoppingcomList = new ArrayList();

	public void runShoppingcomExport() {
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
				Shoppingcom element = new Shoppingcom();

				element.setHaendler_sku(article.getNumber());
				element.setPreis(sellPrice.toString());
				element.setProdukt_url(config.urlWebsite + "/" + config.urlProduct + ".html"
						+ "?utm_source=shoppingdotcom&utm_medium=ppc");
				element.setProduktbild_url(config.mediaURL + pictureURLs.get(0));
				element.setProduktname(article.getShortDescription().replaceAll(";", ","));
				element.setZustand("New");

				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setEan(article.getEan());
				else
					element.setEan("");

				if (config.besonderheit.contains("mit Beleuchtung"))
					element.setEek("A+++ bis A");
				else
					element.setEek("");
				element.setFarbe(config.farbe1);
				element.setHersteller("von moebel-guenstig24.de");
				element.setKategoriename(config.categoryPath);
				element.setProduktbeschreibung(article.getShortDescription());

				element.setProdukttyp("Möbel");
				element.setStyle(config.stilwelt);

				if (actualStock > 0) {
					element.setVerfuegbarkeit("in Stock");
				} else {
					element.setVerfuegbarkeit("Pre-MagentoOrder");
				}

				element.setVersandkosten("0,00");

				// element.setSkuGebot(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 2000)));
				//
				if (actualStock > 0)
					shoppingcomList.add(element);
			}
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("shoppingcomList", shoppingcomList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("shoppingcomTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "shoppingcom.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "shoppingcom.csv"));
			config.ftpClient.storeFile("shoppingcom.csv", input);
			config.logout();
			log.info("shoppingcomExport done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
