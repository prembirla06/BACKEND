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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleDescriptionMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	private ErpArticleStueckRepository erpArticleStueckRepository;

	// @Scheduled(fixedRate = 1000000)
	public void runDescMigrationArticle() {

		System.out.println("start.....");

		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
				if (artikelOptional.isPresent()) {
					Artikel artikel = artikelOptional.get();
					if (StringUtils.isEmpty(artikel.getShopLangtext())) {
						System.out.println("long desciption not present for Artikel Number--" + article.getNumber());
					} else if (StringUtils.isEmpty(artikel.getShopKurztext())) {
						System.out.println("short desciption not present for Artikel Number--" + article.getNumber());
					} else {
						article.setLongDescription(createDescriptions(artikel.getShopLangtext()));
						article.setShortDescription(createDescriptions(artikel.getShopKurztext()));
						articleRepository.save(article);
					}
				}
			}
		});
		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void runDescMigrationArticleSet() {
		System.out.println("start.....");

		articleSetRepository.findAll().forEach(articleSet -> {

			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
			if (artikelOptional.isPresent()) {
				Artikel artikel = artikelOptional.get();
				if (StringUtils.isEmpty(artikel.getShopLangtext())) {
					System.out.println("long desciption not present for Artikel Number--" + articleSet.getNumber());
				} else if (StringUtils.isEmpty(artikel.getShopKurztext())) {
					System.out.println("short desciption not present for Artikel Number--" + articleSet.getNumber());
				} else {
					articleSet.setLongDescription(createDescriptions(artikel.getShopLangtext()));
					articleSet.setShortDescription(createDescriptions(artikel.getShopKurztext()));
					articleSetRepository.save(articleSet);
				}
			}

		});
		System.out.println("end.....");
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
