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
package com.maxxsoft.microServices.articleService.cronJob;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductRequest;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleEndOfLifeJob {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	MagentoService magentoService;

	@Autowired
	private PriceService priceService;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;

	@Scheduled(cron = "${cronexpression.ArticleEndOfLifeJob}")
	public boolean runArticleEndOfLifeJob() {
		log.info("start..runArticleEndOfLifeJob...at.." + LocalDateTime.now());
		HttpHeaders headers = magentoService.getHeader();
		try {
			articleRepository.findAll().forEach(article -> {
				if (article.isEol()) {
					int actualStock = article.getStock() - article.getPreOrder();
					if (actualStock <= 0) {
						String response = null;
						if (article.isStandalone()) {
						BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(),
								article.getBuyPrice(), sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
						// Disable product at magento
						MagentoProductResponse magentoProductResponse = magentoService
								.getProductBySKU1(article.getNumber(), headers);
						MagentoProductList magentoProductList = new MagentoProductList();
						MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
						mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
						mogentoProductRequest.setSku(magentoProductResponse.getSku());
						mogentoProductRequest.setName(article.getLongName().replaceAll("[\"/\\:]", ""));
						mogentoProductRequest.setStatus(0);
						mogentoProductRequest.setPrice(sellingPrice);
						mogentoProductRequest.setVisibility(4);
						magentoProductList.setProduct(mogentoProductRequest);

						response = magentoService.updateProductBySku(article.getNumber(), magentoProductList,
								headers);
						} else {
							response = "not needed";
						}
						if (response != null) {
							article.getArticleSetRelations().forEach(asr -> {
								ArticleSet as = asr.getArticleSet();
								if (as.getActive()) {
									BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(as.getArticleSetId());
									BigDecimal sellPriceSet = priceService.getSellingOrSalePrice(as.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
									
									// Disable SetProduct at magento
									MagentoProductResponse magentoSetProductResponse = magentoService
											.getProductBySKU1(as.getNumber(), headers);
									MagentoProductList magentoSetProductList = new MagentoProductList();
									MagentoProductRequest mogentoSetProductRequest = new MagentoProductRequest();
									mogentoSetProductRequest.setExtension_attributes(magentoSetProductResponse.getExtension_attributes());
									mogentoSetProductRequest.setSku(magentoSetProductResponse.getSku());
									mogentoSetProductRequest.setName(as.getLongName().replaceAll("[\"/\\:]", ""));
									mogentoSetProductRequest.setStatus(0);
									mogentoSetProductRequest.setPrice(sellPriceSet);
									mogentoSetProductRequest.setVisibility(4);
									magentoSetProductList.setProduct(mogentoSetProductRequest);
									
									String responseSet = magentoService.updateProductBySku(as.getNumber(), magentoSetProductList,
											headers);
									if (responseSet != null) {
									asr.getArticleSet().setActive(false);
									articleSetRepository.saveAndFlush(as);
									} else {
										asr.getArticleSet().setEan(asr.getArticleSet().getEan() + " !!DELETE ERROR ON MAGENTO!!");
									}
								}
							});
							article.setActive(false);
							articleRepository.save(article);
						} else {
							log.info("No product found for Sku --" + article.getNumber());
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..runArticleEndOfLifeJob...at.." + LocalDateTime.now());
		return true;
	}

}
