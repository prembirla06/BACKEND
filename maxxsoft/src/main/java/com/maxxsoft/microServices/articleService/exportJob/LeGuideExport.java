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
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.LeGuide;
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
public class LeGuideExport {

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

	FTPClient ftp = null;
	byte pic[] = null;
	List<String> pictureURLs = new ArrayList<>();

	private static ArrayList<LeGuide> leGuideList = new ArrayList();

	public void runLeGuideExport() {
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
				LeGuide element = new LeGuide();

				element.setKategorie(config.categoryPath);
				element.setProduct_id(String.valueOf(erpArticle.getRecId()));
				element.setTitel(article.getShortName().replaceAll(";", ","));
				element.setBeschreibung(article.getShortDescription());
				element.setPreis(sellPrice.toString());
				element.setProduct_URL(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=shco");
				element.setLanding_page_URL(config.urlWebsite + "/" + config.urlProduct + ".html");
				element.setBild_URL(config.mediaURL + pictureURLs.get(0));
				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setEan(article.getEan());
				else
					element.setEan("");

				element.setVersandkosten("0,00");

				if (actualStock > 0) {
					element.setVerfuegbarkeit("0");
					element.setLieferzeit("3-6 Tage");
				} else {
					element.setVerfuegbarkeit("30");
					element.setLieferzeit("3-6 Tage");
				}
				element.setGarantie("2");
				element.setProduktreferenz(article.getNumber());
				element.setMarke("von moebel-guenstig24.de");
				element.setMpn("");
				element.setStreichpreis("");
				element.setFarbe(config.farbe1);
				element.setGroesse(config.breite + " " + config.hoehe + " " + config.tiefe);
				element.setMaterial("siehe Artikelbeschreibung");
				element.setWaehrung("EUR");
				element.setGebrauchtware("0");
				element.setSonderangebot("0");
				element.setHandy_URL(config.urlWebsite + "/" + config.urlProduct + ".html");

				leGuideList.add(element);
			}
		});

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("leGuideList", leGuideList);
			Configuration cfg = config.getConfiguration();

			Template template = cfg.getTemplate("leGuideTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "leGuide.csv");
			template.process(root, writer);
			writer.close();

			// FTP Upload:
			config.connect();
			config.ftpClient.changeWorkingDirectory("/export");
			InputStream input = new FileInputStream(new File(config.outputDir + "leGuide.csv"));
			config.ftpClient.storeFile("leGuide.csv", input);
			config.logout();
			log.info("leGuide Export done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS!");
			e.printStackTrace();
		}
	}
}
