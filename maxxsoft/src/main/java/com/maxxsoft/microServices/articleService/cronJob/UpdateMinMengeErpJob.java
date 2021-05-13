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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class UpdateMinMengeErpJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	CommonConfigRepository commonConfigRepository;

	@Autowired
	ErpArticleRepository erpArticleRepository;

	// once a day
	@Scheduled(cron ="${cronexpression.UpdateMinMengeErpJob}")
	public boolean runUpdateMinMengeErpJob() {
		log.info("start..runUpdateMinMengeErpJob..." + LocalDateTime.now());
		try {
			int supplierDefaultDeliveryTime = Integer
					.valueOf(commonConfigRepository.findByConfigKey("Supplier_Default_Delivery_Time").getValue());
			articleRepository.findAll().forEach(article -> {
				int mengeMin = 0;
				Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
				if (artikelOptional.isPresent()) {
					Artikel artikel = artikelOptional.get();
					Integer supplierDeliveyTime = erpArticleRepository.getSupplierDeliveyTime(artikel.getRecId());
					if (supplierDeliveyTime == null) {
						supplierDeliveyTime = supplierDefaultDeliveryTime;
					}
					mengeMin = erpArticleService.getOrderLastxDays(artikel.getRecId(), supplierDeliveyTime * 2)
							+ article.getPreOrder();
					artikel.setMengeMin(new BigDecimal(mengeMin));
					// System.out.println(artikel.getArtnum() + "-------" +
					// mengeMin);
					erpArticleRepository.saveAndFlush(artikel);
				}
			});

			articleSetRepository.findAll().forEach(articleSet -> {
				int mengeMin = 0;
				Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
				if (artikelOptional.isPresent()) {
					Artikel artikel = artikelOptional.get();
					Integer supplierDeliveyTime = erpArticleRepository.getSupplierDeliveyTime(artikel.getRecId());
					if (supplierDeliveyTime == null) {
						supplierDeliveyTime = supplierDefaultDeliveryTime;
					}
					mengeMin = erpArticleService.getOrderLastxDays(artikel.getRecId(), supplierDeliveyTime * 2)
							+ articleSet.getPreOrder();
					artikel.setMengeMin(new BigDecimal(mengeMin));
					erpArticleRepository.saveAndFlush(artikel);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..runUpdateMinMengeErpJob..." + LocalDateTime.now());
		return true;
	}
}
