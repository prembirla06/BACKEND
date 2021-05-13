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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.model.request.Categories;
import com.maxxsoft.microServices.magentoService.repository.MagentoProductRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class MagentoCategoryNameUpdateAtLocal {

	@Autowired
	private MagentoProductRepository magentoProductRepository;

	@Autowired
	private MagentoService magentoService;

	// @Scheduled(fixedRate = 10000000)
	public void MagentoCategoryNameUpdateAtLocalJob() {
		System.out.println("start...MagentoCategoryNameUpdateAtLocalJob..");
		HttpHeaders headers = magentoService.getHeader();
		Categories categories = magentoService.getCategories1(headers);
		magentoProductRepository.findAll().forEach(product -> {
			// if (product.getMagentoProductId() == 2) {
			product.getMagentoCategories().forEach(category -> {
				category.setCategoryName(
						magentoService.getMagentoCategoryName(Integer.valueOf(category.getCategory()), categories));
			});
			magentoProductRepository.saveAndFlush(product);
			// }
		});

		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	// public void MagentoCategoryAddDeleteTest() {
	// System.out.println("start...MagentoCategoryAddDeleteTest..");
	// // magentoService.addCategory("80410FF+8043703", 1, "107", "% SALE %");
	// magentoService.deleteCategory("80410FF+8043703", 107);
	//
	// System.out.println("end.....");
	// }

}