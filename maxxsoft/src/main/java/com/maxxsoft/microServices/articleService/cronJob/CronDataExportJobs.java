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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.exportJob.BilligerdeExport;
import com.maxxsoft.microServices.articleService.exportJob.Check24Export;
import com.maxxsoft.microServices.articleService.exportJob.GoogleShoppingExport;
import com.maxxsoft.microServices.articleService.exportJob.GuenstigerdeExport;
import com.maxxsoft.microServices.articleService.exportJob.IdealoExport;
import com.maxxsoft.microServices.articleService.exportJob.LeGuideExport;
import com.maxxsoft.microServices.articleService.exportJob.MobeldeExport;
import com.maxxsoft.microServices.articleService.exportJob.Moebel24deExport;
import com.maxxsoft.microServices.articleService.exportJob.RakutenExport;
import com.maxxsoft.microServices.articleService.exportJob.RealExport;
import com.maxxsoft.microServices.articleService.exportJob.SchottenLandExport;
import com.maxxsoft.microServices.articleService.exportJob.ShoppingComExport;
import com.maxxsoft.microServices.articleService.exportJob.StylightExport;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class CronDataExportJobs {

	@Autowired
	CommonConfigRepository commonConfigRepository;

	@Autowired
	GoogleShoppingExport googleShoppingExport;

	@Autowired
	BilligerdeExport billigerdeExport;

	@Autowired
	Check24Export check24Export;

	@Autowired
	GuenstigerdeExport guenstigerdeExport;

	@Autowired
	IdealoExport idealoExport;

	@Autowired
	LeGuideExport leGuideExport;

	@Autowired
	MobeldeExport mobeldeExport;

	@Autowired
	Moebel24deExport moebel24deExport;

	@Autowired
	RakutenExport rakutenExport;

	@Autowired
	RealExport realExport;

	@Autowired
	SchottenLandExport schottenLandExport;

	@Autowired
	ShoppingComExport shoppingComExport;

	@Autowired
	StylightExport stylightExport;

	// need to run every hour
	// @Scheduled(fixedDelay = 10000000)
	@Scheduled(cron = "${cronexpression.CronDataExportJobs}")
	public boolean runExportJobs() {
		log.info("start export jobs.....");
		Boolean googleShoppingExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("GoogleShoppingExport_Flag").getValue());
		if (googleShoppingExportFlag) {
			googleShoppingExport.runExportDataGoogleShopping();
		}

		Boolean billigerdeExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("BilligerdeExport_Flag").getValue());
		if (billigerdeExportFlag) {
			billigerdeExport.runBilligerdeExport();
		}

		Boolean check24ExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("Check24Export_Flag").getValue());
		if (check24ExportFlag) {
			check24Export.runCheck24Export();
		}

		Boolean guenstigerdeExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("GuenstigerdeExport_Flag").getValue());
		if (guenstigerdeExportFlag) {
			guenstigerdeExport.runGuenstigerdeExport();
		}

		Boolean idealoExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("IdealoExport_Flag").getValue());
		if (idealoExportFlag) {
			idealoExport.runIdealoExport();
		}

		Boolean leGuideExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("LeGuideExport_Flag").getValue());
		if (leGuideExportFlag) {
			leGuideExport.runLeGuideExport();
		}

		Boolean mobeldeExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("MoebeldeExport_Flag").getValue());
		if (mobeldeExportFlag) {
			mobeldeExport.runMoebeldeExport();
		}

		Boolean moebel24deExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("Moebel24deExport_Flag").getValue());
		if (moebel24deExportFlag) {
			moebel24deExport.runMoebel24deExport();
		}

		Boolean rakutenExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("RakutenExport_Flag").getValue());
		if (rakutenExportFlag) {
			rakutenExport.runRakutenExport();
		}

		Boolean realExportFlag = Boolean.valueOf(commonConfigRepository.findByConfigKey("RealExport_Flag").getValue());
		if (realExportFlag) {
			realExport.runRealExport();
		}

		Boolean schottenLandExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("SchottenlandExport_Flag").getValue());
		if (schottenLandExportFlag) {
			schottenLandExport.runSchottenlandExport();
		}

		Boolean shoppingComExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("ShoppingcomExport_Flag").getValue());
		if (shoppingComExportFlag) {
			shoppingComExport.runShoppingcomExport();
		}

		Boolean stylightExportFlag = Boolean
				.valueOf(commonConfigRepository.findByConfigKey("StylightExport_Flag").getValue());
		if (stylightExportFlag) {
			stylightExport.runStylightExport();
		}

		log.info("end export jobs.....");
		return true;
	}
}
