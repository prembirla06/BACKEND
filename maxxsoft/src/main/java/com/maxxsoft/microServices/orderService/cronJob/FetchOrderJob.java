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
package com.maxxsoft.microServices.orderService.cronJob;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.microServices.magentoService.service.MagentoService;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;
import com.maxxsoft.microServices.orderService.service.OrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class FetchOrderJob {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MagentoService magentoService;

	public static final String CRON_EXPRESSION = "0 0 5 * * *";

	// @PostConstruct
	// public void init() {
	// CronExpression cronTrigger = new CronExpression.parse(CRON_EXPRESSION);
	// LocalDateTime next = cronTrigger.next(LocalDateTime.now());
	//
	// System.out.println("Next Execution Time: " + next);
	// }

	// once a day
	@Scheduled(cron = "${cronexpression.FetchOrderJob}")
	// @Scheduled(fixedRate = 10000000)
	public boolean runFetchOrderJob() {

		log.info("start..FetchOrderJob..." + LocalDateTime.now());
		try {
			HttpHeaders headers = magentoService.getHeader();
			MagentoOrderResponse magentoOrderResponse = null;
			String latestOrderNumber = orderService.findLatestOrdersNumber("Magento");
			if (StringUtils.isEmpty(latestOrderNumber)) {
				magentoOrderResponse = magentoService.getAllOrders(headers);
			} else {
				magentoOrderResponse = magentoService.getNewOrders(headers, latestOrderNumber);
			}

			orderService.receive(magentoOrderResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..FetchOrderJob..." + LocalDateTime.now());
		return true;
	}
}
