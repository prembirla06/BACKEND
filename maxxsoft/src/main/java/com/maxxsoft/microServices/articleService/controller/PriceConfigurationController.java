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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.microServices.articleService.model.PriceConfiguration;
import com.maxxsoft.microServices.articleService.service.PriceConfigurationService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Transactional
public class PriceConfigurationController {

	@Autowired
	private PriceConfigurationService priceConfigurationService;

	@GetMapping(value = "/priceConfiguration")
	public List<PriceConfiguration> getAllPriceConfiguration() {
		return priceConfigurationService.getPriceConfigurationList();
	}

	@PostMapping(value = "/priceConfiguration")
	public PriceConfiguration createPriceConfiguration(@RequestBody PriceConfiguration priceConfiguration) {
		return priceConfigurationService.createPriceConfiguration(priceConfiguration);
	}

	@PutMapping(value = "/priceConfiguration/{priceConfigurationId}")
	public PriceConfiguration updatePriceConfiguration(@PathVariable Long priceConfigurationId,
			@RequestBody PriceConfiguration priceConfiguration) {
		return priceConfigurationService.updatePriceConfiguration(priceConfigurationId, priceConfiguration);
	}

	@DeleteMapping(value = "/priceConfiguration/{priceConfigurationId}")
	public void deletePriceConfiguration(@PathVariable Long priceConfigurationId) {
		priceConfigurationService.deletePriceConfiguration(priceConfigurationId);
	}
}