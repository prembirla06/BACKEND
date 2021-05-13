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
package com.maxxsoft.microServices.articleService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.microServices.articleService.cronJob.ArticleEndOfLifeJob;
import com.maxxsoft.microServices.articleService.cronJob.ArticleMarkOrphanJob;
import com.maxxsoft.microServices.articleService.cronJob.ArticleStockAndDeliveryTimeAndPreOrderUpdateJob;
import com.maxxsoft.microServices.articleService.cronJob.CronDataExportJobs;
import com.maxxsoft.microServices.articleService.cronJob.MagentoSellingPriceAndCategoryPositionUpdateJob;
import com.maxxsoft.microServices.articleService.cronJob.UpdateMinMengeErpJob;
import com.maxxsoft.microServices.articleService.cronJob.UpdateTotalWeightJob;
import com.maxxsoft.microServices.orderService.cronJob.FetchOrderJob;
import com.maxxsoft.microServices.orderService.cronJob.ProcessOrderJob;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/job")
@Transactional
public class JobController {

	@Autowired
	private ArticleEndOfLifeJob articleEndOfLifeJob;

	@Autowired
	private ArticleMarkOrphanJob articleMarkOrphanJob;

	@Autowired
	private ArticleStockAndDeliveryTimeAndPreOrderUpdateJob articleStockAndDeliveryTimeAndPreOrderUpdateJob;

	@Autowired
	private MagentoSellingPriceAndCategoryPositionUpdateJob magentoSellingPriceAndCategoryPositionUpdateJob;

	@Autowired
	private UpdateMinMengeErpJob updateMinMengeErpJob;

	@Autowired
	private UpdateTotalWeightJob updateTotalWeightJob;

	@Autowired
	private CronDataExportJobs cronDataExportJobs;

	@Autowired
	private ProcessOrderJob processOrderJob;

	@Autowired
	private FetchOrderJob fetchOrderJob;

	@GetMapping(value = "/endOfLife")
	public boolean runEndOfLife() {
		return articleEndOfLifeJob.runArticleEndOfLifeJob();
	}

	@GetMapping(value = "/markArticleAsOrphan")
	public boolean runMarkArticleAsOrphan() {
		return articleMarkOrphanJob.runArticleMarkOrphanJob();
	}

	@GetMapping(value = "/stockAndDeliveryTimeAndPreOrderUpdate")
	public boolean runStockAndDeliveryTimeAndPreOrderUpdate() {
		return articleStockAndDeliveryTimeAndPreOrderUpdateJob.runStockAndDeliveryTimeAndPreOrderUpdate();
	}

	@GetMapping(value = "/magentoSellingPriceAndCategoryPositionUpdateArticles")
	public boolean runMagentoSellingPriceAndCategoryPositionUpdate() {
		return magentoSellingPriceAndCategoryPositionUpdateJob.runMagentoSellingPriceUpdateJobForArticles();
	}

	@GetMapping(value = "/magentoSellingPriceAndCategoryPositionUpdateSet")
	public boolean runMagentoSellingPriceAndCategoryPositionUpdateForSet() {
		return magentoSellingPriceAndCategoryPositionUpdateJob.runMagentoSellingPriceForArticleSet();
	}

	@GetMapping(value = "/updateMinMengeErp")
	public boolean runUpdateMinMengeErp() {
		return updateMinMengeErpJob.runUpdateMinMengeErpJob();
	}

	@GetMapping(value = "/updateTotalWeight")
	public boolean runUpdateTotalWeight() {
		return updateTotalWeightJob.runUpdateTotalWeightJob();
	}

	@GetMapping(value = "/exportJobs")
	public boolean runExportJobs() {
		return cronDataExportJobs.runExportJobs();
	}

	@GetMapping(value = "/processOrderJob")
	public boolean runProcessOrderJob() {
		return processOrderJob.runProcessReceivedOrderJob();
	}

	@GetMapping(value = "/fetchOrderJob")
	public boolean runFetchOrderJob() {
		return fetchOrderJob.runFetchOrderJob();
	}
}