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
import com.maxxsoft.microServices.articleService.model.export.Rakuten;
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
public class RakutenExport {

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
	private static ArrayList<Rakuten> rakutenList = new ArrayList();

	public void runRakutenExport() {
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
				Rakuten element = new Rakuten();

				element.setId("");
				element.setArtikelnummer(article.getNumber());
				element.setProduktname(article.getShortName().replace(";", ","));
				element.setPreis(sellPrice.toString());
				element.setVersandgruppe("1");
				element.setMwstKlasse("1");
				// Mögliche Werte für Lieferzeit: 0,3,5,7,10,15,20,30,40,50,60
				int deliveryTime = article.getDeliveryTime();
				if (deliveryTime < 3)
					element.setLieferzeit("3");
				else if (deliveryTime < 5)
					element.setLieferzeit("5");
				else if (deliveryTime < 7)
					element.setLieferzeit("7");
				else if (deliveryTime < 10)
					element.setLieferzeit("10");
				else if (deliveryTime < 15)
					element.setLieferzeit("15");
				else if (deliveryTime < 20)
					element.setLieferzeit("20");
				else if (deliveryTime < 30)
					element.setLieferzeit("30");
				else if (deliveryTime < 40)
					element.setLieferzeit("40");
				else if (deliveryTime < 50)
					element.setLieferzeit("50");
				else if (deliveryTime < 60)
					element.setLieferzeit("60");
				else
					element.setLieferzeit("60");

				element.setProduktBestellbar("1");
				element.setSichtbar("1");
				element.setBeschreibung(article.getShortDescription());
				if (config.categoryPath.length() >= 2) {
					element.setKategorie(config.categoryPath);
				} else
					element.setKategorie("unbekannt");

				element.setBild1(config.mediaURL + pictureURLs.get(0));

				rakutenList.add(element);
			}

		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("rakutenList", rakutenList);
			Configuration cfg = config.getConfiguration();

			Template rakutenTemplate = cfg.getTemplate("rakutenTemplate.csv");
			FileWriter writerRakuten = new FileWriter(config.outputDir + "rakuten.csv");
			rakutenTemplate.process(root, writerRakuten);
			writerRakuten.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "rakuten.csv"));
			config.ftpClient.storeFile("rakuten.csv", input);
			config.logout();
			log.info("Rakuten Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
