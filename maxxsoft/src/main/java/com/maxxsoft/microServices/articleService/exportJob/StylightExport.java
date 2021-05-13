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
import com.maxxsoft.microServices.articleService.model.export.Stylight;
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
public class StylightExport {

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

	private static ArrayList<Stylight> stylightList = new ArrayList();

	public void runStylightExport() {
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
				Stylight element = new Stylight();

				element.setName(article.getShortName().replaceAll("\"", "").replaceAll(";", " "));
				element.setDesc(article.getShortDescription().replaceAll("\"", "").replaceAll(";", " "));
				element.setPrice(sellPrice.toString());
				element.setCurrency("EUR");
				if (!config.breite.isEmpty() && !config.tiefe.isEmpty() && !config.hoehe.isEmpty())
					element.setDimension(config.breite + "x" + config.tiefe + "x" + config.hoehe);
				element.setCategory(config.categoryPath);
				element.setShipping_cost("0"); // free shipping
				StringBuilder images = new StringBuilder(config.mediaURL + pictureURLs.get(0));
				for (int i = 1; i < pictureURLs.size(); i++) {
					images.append("|");
					images.append(config.mediaURL + pictureURLs.get(i));
				}
				element.setImages_url(images.toString());
				element.setGender("unisex");
				element.setBrand("moebel-guenstig24.de");
				element.setProduct_url(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=slde");
				element.setGTIN(article.getEan());
				element.setProduct_id(article.getNumber());
				element.setItem_group_id(""); // no grouping
				element.setAge_group(""); // no age group
				element.setFabric(""); // no fabric
				element.setColor(config.farbe1);
				if (config.farbe2.length() >= 2) {
					element.setColor(config.farbe1 + " | " + config.farbe2);
				}

				element.setPattern(""); // no pattern
				element.setStype(config.trendfarbe);

				if (actualStock >= 0) {
					element.setAvailability(String.valueOf(actualStock));
				}
				// element.setCpc_bid_desktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 2000)));
				// element.setCpc_bid_mobile(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 2000)));
				// element.setCpc_bid_tabled(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 2000)));
				// } else {
				// element.setAvailability("10");
				// element.setCpc_bid_desktop(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 4000)));
				// element.setCpc_bid_mobile(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 4000)));
				// element.setCpc_bid_tabled(String.valueOf(PreisCalculator.getCPCGebot(artikelCAO,
				// 4000)));
				// }

				stylightList.add(element);
			}
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("stylightList", stylightList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("stylightTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "stylight.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "stylight.csv"));
			config.ftpClient.storeFile("stylight.csv", input);
			config.logout();
			log.info("stylight Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
