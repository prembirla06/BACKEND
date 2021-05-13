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
package com.maxxsoft.microServices.articleService.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxxsoft.microServices.articleService.model.SellingPlatform;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PlatformConfigurationService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service
@Slf4j
public class PlatformConfigurationServiceImpl implements PlatformConfigurationService {

	@Autowired
	SellingPlatformRepository sellingPlatformRepository;

	@Override
	public List<SellingPlatform> getPlatformList() {
		return sellingPlatformRepository.findAll();
	}
}