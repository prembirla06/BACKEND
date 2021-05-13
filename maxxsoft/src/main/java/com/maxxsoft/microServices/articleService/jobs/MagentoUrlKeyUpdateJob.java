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

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCustomAttributeValueRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductRequest;
import com.maxxsoft.microServices.magentoService.repository.MagentoCustomAttributeRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class MagentoUrlKeyUpdateJob {

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

	@Autowired
	MagentoCustomAttributeRepository magentoCustomAttributeRepository;

	@Autowired
	private PriceService priceService;

	@Autowired
	private MagentoService magentoService;

	//@Scheduled(fixedRate = 10000000)
	public void runMagentoUrlKeyUpdateJobForArticle() {
		HttpHeaders headers = magentoService.getHeader();

		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				System.out.println("article.getArticleId()------" + article.getArticleId());
				BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(), 1L);
				MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(article.getNumber(),
						headers);

				Set<MagentoCustomAttributeValueRequest> magentoCustomAttributeValueRequestSet = new HashSet<>();
				String longNameWithoutSpecialChars = article.getLongName().replaceAll("[\"/\\:]", "");
				MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
						"url_key", urlKeyBuilder(article.getLongName()));
				magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);

				MagentoProductList magentoProductList = new MagentoProductList();
				MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
				mogentoProductRequest.setSku(magentoProductResponse.getSku());
				mogentoProductRequest.setName(longNameWithoutSpecialChars);
				mogentoProductRequest.setPrice(sellingPrice);
				mogentoProductRequest.setVisibility(4);
				mogentoProductRequest.setCustom_attributes(magentoCustomAttributeValueRequestSet);
				mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
				magentoProductList.setProduct(mogentoProductRequest);
				String response = magentoService.updateProductBySku(article.getNumber(), magentoProductList, headers);
				if (response == null) {
					System.out.println("No product found for Sku --" + article.getNumber());
				}
			}

		});

	}

	//@Scheduled(fixedRate = 10000000)
	public void runMagentoUrlKeyUpdateJobForArticleSet() {

		HttpHeaders headers = magentoService.getHeader();

		articleSetRepository.findAll().forEach(articleSet -> {
			System.out.println("article.getArticleSetId()------" + articleSet.getArticleSetId());
			BigDecimal buyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), buyPrice, 1L);
			MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(articleSet.getNumber(),
					headers);

			Set<MagentoCustomAttributeValueRequest> magentoCustomAttributeValueRequestSet = new HashSet<>();
			String longNameWithoutSpecialChar = articleSet.getLongName().replaceAll("[\"/\\:]", "");
			MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
					"url_key", urlKeyBuilder(articleSet.getLongName()));
			magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);

			MagentoProductList magentoProductList = new MagentoProductList();
			MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
			mogentoProductRequest.setSku(magentoProductResponse.getSku());
			mogentoProductRequest.setName(longNameWithoutSpecialChar);
			mogentoProductRequest.setPrice(sellingPrice);
			mogentoProductRequest.setVisibility(4);
			mogentoProductRequest.setCustom_attributes(magentoCustomAttributeValueRequestSet);
			mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
			magentoProductList.setProduct(mogentoProductRequest);
			String response = magentoService.updateProductBySku(articleSet.getNumber(), magentoProductList, headers);
			if (response == null) {
				System.out.println("No product found for Sku --" + articleSet.getNumber());
			}
		});
	}

	private String urlKeyBuilder(String url_key) {
		url_key = url_key.replaceAll("[\"/\\:,+. ]", "-");
		url_key = url_key.replace("ü", "ue");
		url_key = url_key.replace("Ü", "Ue");
		url_key = url_key.replace("ö", "oe");
		url_key = url_key.replace("Ö", "Oe");
		url_key = url_key.replace("ä", "ae");
		url_key = url_key.replace("Ä", "Ae");
		url_key = url_key.replace("ß", "ss");
		url_key = url_key.replace("é", "e");
		url_key = url_key.toLowerCase();
		url_key = url_key.replaceAll("-----", "-");
		url_key = url_key.replaceAll("----", "-");
		url_key = url_key.replaceAll("---", "-");
		url_key = url_key.replaceAll("--", "-");
		if (url_key.charAt(url_key.length() - 1) == '-') {
			System.out.println("url_key------" + url_key);
			url_key = url_key.substring(0, url_key.length() - 1);
		}
		url_key = Normalizer.normalize(url_key, Normalizer.Form.NFD);
		url_key = url_key.replaceAll("[^\\p{ASCII}]", "");
		System.out.println("url_key------" + url_key);
		return url_key;
	}
}
