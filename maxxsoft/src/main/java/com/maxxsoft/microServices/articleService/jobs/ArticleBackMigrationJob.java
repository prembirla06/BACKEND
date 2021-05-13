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
package com.maxxsoft.microServices.articleService.jobs;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.articleService.utility.MapArticleImagesWithArticleFromFtp;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleBackMigrationJob {

	@Value("${ftp.username}")
	private String username;
	@Value("${ftp.password}")
	private String password;
	@Value("${ftp.host}")
	private String host;
	@Value("${ftp.port}")
	private int port;

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
	ImageService imageService;

	byte pic[] = null;
	Set<String> newArtNums = new HashSet<>(Arrays.asList(
			"30-B1-UH-80",
			"30-B1-UH-81",
			"30-B1-UH-82"
			));

	// @Autowired
	// FtpConfiguration1 f;

	//@Scheduled(fixedDelay = 10000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runArticleBackMigration() {
		System.out.println("here");
		erpArticleService.getAllArtikelByType("N").forEach(artikel -> {
			if (newArtNums.contains(artikel.getArtnum())
					&& !articleRepository.findByNumber(artikel.getArtnum()).isPresent()) {
				System.out.println("article number--" + artikel.getArtnum() + "  EAN--" + artikel.getBarcode()
						+ "   Substitute number--" + artikel.getErsatzArtnum());
				Article article = new Article();
				article.setBuyPrice(artikel.getEkPreis());
				article.setNumber(artikel.getArtnum());
				if (StringUtils.isEmpty(artikel.getBarcode())) {
					article.setEan("NULLBARCODE-" + artikel.getArtnum());
				} else {
					article.setEan(artikel.getBarcode());
				}
				article.setShortName(artikel.getKasName());
				article.setLongName(artikel.getLangname());
				article.setSubstituteNumber(artikel.getErsatzArtnum());
				article.setPackageUnit(artikel.getVpe().intValue());
				article.setTotalWeight(artikel.getGewicht().intValue());
				if (!(artikel.getWarengruppe() == 80160)) {
				article.setDimension(Integer.valueOf(artikel.getDimension()));
				}
				article.setLongDescription(createDescriptions(artikel.getShopLangtext()));
				article.setShortDescription(createDescriptions(artikel.getShopKurztext()));
				article.setStock(artikel.getMengeAkt().intValue());
				if (artikel.getWarengruppe() == 80160) {
					article.setStandalone(false);
				}
				// ImageRequest ai = new ImageRequest("test", 1, pic);
				// article.addImage(ai);
				articleRepository.save(article);

			}
		});
	}

	//@Scheduled(fixedDelay = 10000000)
	@Transactional
	public void runArticleSetBackMigration() {

		Set<Artikel> artikelSets = erpArticleService.getAllArtikelByType("S");
		Set<Artikel> faultyArtikelSets = new LinkedHashSet<Artikel>();
		System.out.println("before--" + artikelSets.size());
		artikelSets.forEach(artikelSet -> {
			erpArticleStueckRepository.findByRecId(artikelSet.getRecId()).forEach(result -> {

				Artikel artikel = erpArticleRepository.findById((Integer) result[0]).get();

				Optional<Article> optionalArticle = articleRepository.findByNumber(artikel.getArtnum());

				if (!optionalArticle.isPresent()) {
					faultyArtikelSets.add(artikelSet);
				}
			});

		});

		System.out.println("faluty--" + faultyArtikelSets.size());
		faultyArtikelSets.forEach(f -> System.out.println(f.getRecId()));

		Set<Artikel> cleanArtikelSets = artikelSets.stream().filter(e -> !faultyArtikelSets.contains(e))
				.collect(Collectors.toSet());

		System.out.println("clean--" + cleanArtikelSets.size());

		cleanArtikelSets.forEach(artikelSet -> {
			if (newArtNums.contains(artikelSet.getArtnum())
					&& !articleSetRepository.findByNumber(artikelSet.getArtnum()).isPresent()) {
				// System.out.println(
				// "Rec_ID--" + artikelSet.getRecId() + "article number--" +
				// artikelSet.getArtnum() + " EAN--"
				// + artikelSet.getBarcode() + " Substitute number--" +
				// artikelSet.getErsatzArtnum());
				ArticleSet articleSet = new ArticleSet();
				// articleSet.setBuyPrice(artikel.getEkdsPreis());
				articleSet.setNumber(artikelSet.getArtnum());
				if (StringUtils.isEmpty(artikelSet.getBarcode())) {
					articleSet.setEan("NULLBARCODE-" + artikelSet.getArtnum());
				} else {
					articleSet.setEan(artikelSet.getBarcode());
				}
				articleSet.setShortName(artikelSet.getKasName());
				articleSet.setLongName(artikelSet.getLangname());
				articleSet.setSubstituteNumber(artikelSet.getErsatzArtnum());
				articleSet.setDimension(Integer.valueOf(artikelSet.getDimension()));
				articleSet.setLongDescription(createDescriptions(artikelSet.getShopLangtext()));
				articleSet.setShortDescription(createDescriptions(artikelSet.getShopKurztext()));
				articleSet.setStock(artikelSet.getMengeAkt().intValue());

				// articleSet.setPackageUnit(artikel.getVpe().intValue());
				// articleSet.setTotalWeight(artikel.getGewicht().intValue());

				// articleSet.setTotalArticles(articleSetRequest.getTotalArticles());

				//

				erpArticleStueckRepository.findByRecId(artikelSet.getRecId()).forEach(result -> {

					Artikel artikel = erpArticleRepository.findById((Integer) result[0]).get();

					Optional<Article> optionalArticle = articleRepository.findByNumber(artikel.getArtnum());

					if (optionalArticle.isPresent()) {
						articleSet.addArticle(optionalArticle.get(), ((BigDecimal) result[1]).intValue());
					} else {

						System.out.println("*****************NO VALUE PRESENT****(NOT MIGRATED ARTICLE)***********");
						System.out.println("Artikle Set ID-----" + artikelSet.getRecId() + "--Associated Artikle ID--"
								+ artikel.getRecId() + "--Warengruppe--" + artikel.getWarengruppe());
						// System.out.println("*****************END***************");
					}

				});
				// Hack to avoid Null Article Set ID
				articleSet.getArticleSetRelations().forEach(asr -> {
					if (asr.getArticleSetId() == null) {
						asr.setArticleSetId(articleSet.getArticleSetId());
					}

				});
				articleSetRepository.save(articleSet);
			}
		});
		System.out.println("finish");
	}

	//@Scheduled(fixedDelay = 10000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runArticleImageMigration() throws IOException {

		articleRepository.findAllStandalone().forEach(article -> {
			if (newArtNums.contains(article.getNumber())) {
				mapArticleImagesWithArticleFromFtp.downloadArticleImages(article.getArticleId(), article.getNumber());
			}
		});

		// Optional<Article> article = articleRepository.findById(2L);
		// mapArticleImagesWithArticleFromFtp.downloadArticleImages(article.get().getArticleId(),
		// article.get().getNumber());
	}

	//@Scheduled(fixedDelay = 10000000)
	public void runArticleSetImageMigration() throws IOException {
		articleSetRepository.findAll().forEach(articleSet -> {
			if (newArtNums.contains(articleSet.getNumber())) {
				mapArticleImagesWithArticleFromFtp.downloadArticleSetImages(articleSet.getArticleSetId(),
						articleSet.getNumber());
			}
		});

		// Optional<ArticleSet> articleSet =
		// articleSetRepository.findById(155L);
		// mapArticleImagesWithArticleFromFtp.downloadArticleSetImages(articleSet.get().getArticleSetId(),
		// articleSet.get().getNumber());
	}

	// @Scheduled(fixedDelay = 10000000)
	public void runBuyPriceUpdate() {
		log.info("start...runBuyPriceUpdate..");
		articleRepository.findAll().forEach(article -> {
			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
			if (artikelOptional.isPresent()) {
				Artikel artikel = artikelOptional.get();
				article.setBuyPrice(artikel.getEkPreis());
				articleRepository.saveAndFlush(article);
			}
		});
		log.info("end...runBuyPriceUpdate..");
	}

	private String createDescriptions(String xmlText) {
		// String beschreibungMagento = new String();
		// String rohBeschreibung = new String(artikelCAO.getShopLangtext());
		String htmlText = new String();

		// XML transformation
		// 1. Text:
		String text = new String();
		boolean foundText = false;
		boolean foundAufz = false;

		while (xmlText.length() > 0) {
			if (xmlText.indexOf("<Text>") == 0 && xmlText.indexOf("</Text>") != -1) {
				// HTML Aufbauen
				text = text + "<p><span style=\"font-size: medium;\">"
						+ xmlText.substring((xmlText.indexOf("<Text>") + 6), xmlText.indexOf("</Text>"))
						+ "</span></p>";
				// Text aus Rohdaten entfernen
				xmlText = xmlText.substring(xmlText.indexOf("</Text>") + 7);
				foundText = true;

			} else if (xmlText.indexOf("<Aufz>") == 0 && xmlText.indexOf("</Aufz>") != -1) {

				String aufz = new String(
						xmlText.substring((xmlText.indexOf("<Aufz>") + 6), xmlText.indexOf("</Aufz>")));
				// entferne /n und /r am Anfang
				while (aufz.charAt(0) == '\n' || aufz.charAt(0) == '\r') {
					aufz = aufz.substring(1);
				}
				String headline = new String(aufz.substring(0, aufz.indexOf("\n")));
				String bulletpoints = new String(aufz.substring(aufz.indexOf("\n"), aufz.length()));
				// entferne /n und /r am Anfang
				while (bulletpoints.charAt(0) == '\n' || bulletpoints.charAt(0) == '\r') {
					bulletpoints = bulletpoints.substring(1);
				}
				String[] bulletpoint = bulletpoints.split("\n");

				// add headline
				text = text + "<h3><span style=\"text-decoration: underline;\">" + headline + "</span></h3>" +
				// start P for bulletpoint
						"<p>";
				// add bulletpoint
				for (int i = 0; i < bulletpoint.length; i++) {
					text = text + "<span style=\"font-size: medium;\">" + bulletpoint[i] + "</span><br />";
				}
				// end P for bulletpoint
				text = text + "</p>";

				xmlText = xmlText.substring(xmlText.indexOf("</Aufz>") + 7);
				foundAufz = true;
			} else {
				// No "Text" and "Aufz" found, delete character;
				xmlText = xmlText.substring(1);
			}
		}

		htmlText = htmlText + text;

		return htmlText;
	}
}
