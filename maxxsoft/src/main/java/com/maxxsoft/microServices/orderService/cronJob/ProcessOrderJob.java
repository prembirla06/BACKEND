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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.microServices.magentoService.service.MagentoService;
import com.maxxsoft.microServices.orderService.model.Order;
import com.maxxsoft.microServices.orderService.service.OrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ProcessOrderJob {

	@Autowired
	private OrderService orderService;

	@Autowired
	private MagentoService magentoService;

	public static final String CRON_EXPRESSION = "0 0 5 * * *";

	// once a day
	@Scheduled(cron = "${cronexpression.ProcessOrderJob}")
	// @Scheduled(fixedRate = 10000000)
	public boolean runProcessReceivedOrderJob() {
		log.info("start..ProcessOrderJob..." + LocalDateTime.now());
		try {
			List<Order> orderList = orderService.getAllPaidOrders();
			orderList.forEach(order -> {
				if (order.getOrderState().equals(OrderStates.PAID)) {
					orderService.checkStockAndMoveOrder(order);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..ProcessOrderJob..." + LocalDateTime.now());
		return true;
	}
}
