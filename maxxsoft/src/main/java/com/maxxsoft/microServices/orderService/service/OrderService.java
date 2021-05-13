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
package com.maxxsoft.microServices.orderService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.statemachine.StateMachine;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.common.model.TourStates;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;
import com.maxxsoft.microServices.orderService.model.Order;
import com.maxxsoft.microServices.orderService.model.StateMachineCustomResponse;
import com.maxxsoft.microServices.orderService.model.Tour;
import com.maxxsoft.microServices.orderService.model.request.OrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourOrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourRequest;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface OrderService {

	StateMachine<OrderStates, OrderEvents> cancel(Long orderId);

	public void receive(MagentoOrderResponse magentoBatch);

	public List<Order> getAllOrders();

	public List<Order> getAllActiveOrders();

	public List<Order> getAllPaidOrders();

	// public List<OrderRequest> getAllReceivedOrders();

	public Optional<Order> getOrderById(Long orderId);

	public String findLatestOrdersNumber(String marketPlace);

	public StateMachineCustomResponse sendEventForOrder(Long orderId, OrderEvents orderEvent);

	public Order updateOrder(OrderRequest orderRequest, Long orderId);

	public void checkStockAndMoveOrder(Order order);

	public void checkPaymentAndMoveOrder(Order order);

	public void createTour(TourRequest tourRequest);

	public List<Tour> getAllTour();

	public Tour updateTour(Long tourId, TourRequest tourRequest);

	public Tour addOrderToTour(Long tourId, TourOrderRequest tourOrderRequest);

	void removeOrderFromTour(Long tourId, Long orderId);

	public Tour updateTourState(Long tourId, TourStates tourState);

	public String getTourPath(Long tourId);

}