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
import com.maxxsoft.microServices.articleService.model.export.Guenstigerde;
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
public class GuenstigerdeExport {

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

	private static ArrayList<Guenstigerde> guenstigerdeList = new ArrayList();

	public void runGuenstigerdeExport() {
		articleRepository.findAll().forEach(article -> {
			if (article.getArticleId() < 15) {
				Guenstigerde element = new Guenstigerde();
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
				element.setBestellnummer(article.getNumber());
				element.setHerstellerArtNr(article.getNumber());
				element.setHersteller("von moebel-guenstig24.de");
				element.setProduktBezeichnung(article.getShortName().replaceAll(";", ","));
				element.setProduktBeschreibung(article.getShortDescription());
				element.setPreis(sellPrice.toString());
				if (actualStock > 0) {
					element.setLieferzeit("3-6 Tage");
				} else {
					element.setLieferzeit("3-6 Wochen");
				}
				element.setProduktLink(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=gude");
				element.setFotoLink(config.mediaURL + pictureURLs.get(0));
				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setEanCode(article.getEan());
				else
					element.setEanCode("");
				element.setKategorie(config.categoryPath);

				element.setVersandPayPal("0,00");
				element.setVersandKreditkarte("0,00");
				element.setVersandLastschrift("0,00");
				element.setVersandRechnung("0,00");

				if (config.besonderheit.contains("mit Beleuchtung"))
					element.setEnergieeffizienzklasse("A+++ bis A");
				else
					element.setEnergieeffizienzklasse("");
				element.setFarbe(config.farbe1);

				guenstigerdeList.add(element);
			}
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("guenstigerdeList", guenstigerdeList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("guenstigerdeTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "guenstigerde.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "guenstigerde.csv"));
			config.ftpClient.storeFile("guenstigerde.csv", input);
			config.logout();
			log.info("guenstigerde Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
