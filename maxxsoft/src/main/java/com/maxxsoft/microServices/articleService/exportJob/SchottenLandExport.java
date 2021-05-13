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
import com.maxxsoft.microServices.articleService.model.export.Schottenland;
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
public class SchottenLandExport {

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

	private static ArrayList<Schottenland> schottenlandList = new ArrayList();

	public void runSchottenlandExport() {
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
				Schottenland element = new Schottenland();

				element.setAan(article.getNumber());
				element.setProduktname(article.getShortName().replaceAll(";", ","));
				element.setProduktpreis(sellPrice.toString());
				element.setDeeplink(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=slde");
				element.setDeeplink_mobile(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=slde");
				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setEan(article.getEan());
				else
					element.setEan("");
				element.setHersteller("von moebel-guenstig24.de");
				if (actualStock > 0) {
					element.setLieferzeit("3-6 Tage");
				} else {
					element.setLieferzeit("3-6 Wochen");
				}

				element.setVersandkosten_vorkasse("0,00");
				element.setVersandkosten_kreditkarte("0,00");
				element.setVersandkosten_lastschrift("0,00");
				element.setVersandkosten_rechnung("0,00");
				element.setVersandkosten_paypal("0,00");

				if (config.besonderheit.contains("mit Beleuchtung"))
					element.setEnergieeffizienzklasse("A+++ bis A");
				else
					element.setEnergieeffizienzklasse("");

				element.setProduktbild(config.mediaURL + pictureURLs.get(0));

				element.setHan(article.getNumber());
				element.setArtikelbeschreibung(article.getShortDescription());

				element.setWarenkorb_link(""); // TODO: noch nicht implementiert
				element.setWarenkorb_link_mobile(""); // TODO: noch nicht
														// implementiert

				element.setKategorie(config.categoryPath);

				schottenlandList.add(element);
			}
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("schottenlandList", schottenlandList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("schottenlandTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "schottenland.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "schottenland.csv"));
			config.ftpClient.storeFile("schottenland.csv", input);
			config.logout();
			log.info("schottenland Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
