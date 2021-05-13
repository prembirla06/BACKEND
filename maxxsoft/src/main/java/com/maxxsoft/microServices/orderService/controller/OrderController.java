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
package com.maxxsoft.microServices.orderService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.Response;
import com.maxxsoft.common.model.StatusCode;
import com.maxxsoft.common.model.TourStates;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.model.request.ArticlePacketCalculations;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.orderService.model.Order;
import com.maxxsoft.microServices.orderService.model.StateMachineCustomResponse;
import com.maxxsoft.microServices.orderService.model.Tour;
import com.maxxsoft.microServices.orderService.model.request.OrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourOrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourRequest;
import com.maxxsoft.microServices.orderService.model.response.OrderArticleResponse;
import com.maxxsoft.microServices.orderService.service.OrderService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ArticleService articleService;

	@GetMapping(value = "/allOrders")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Order>> orders() {
		return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
	}

	@GetMapping(value = "/getOrder/{orderId}")
	public ResponseEntity<Order> getorderById(@PathVariable Long orderId) {
		Optional<Order> order = orderService.getOrderById(orderId);
		if (order.isPresent()) {
			return new ResponseEntity<>(order.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PutMapping(value = "/updateOrder/{orderId}")
	public Order updateProducts(@RequestBody OrderRequest orderRequest, @PathVariable Long orderId) {
		return orderService.updateOrder(orderRequest, orderId);
	}

	@GetMapping(value = "/sendEvent/{orderId}/{event}")
	public ResponseEntity<StateMachineCustomResponse> sendEvent(@PathVariable Long orderId,
			@PathVariable OrderEvents event) {
		return new ResponseEntity<>(orderService.sendEventForOrder(orderId, event), HttpStatus.OK);
	}

	@GetMapping(value = "/orderWeight/{articleNum}")
	public Double getArticleTotalWeight(@PathVariable String articleNum) {
		return articleService.getWeightByArtNum(articleNum);
	}

	@GetMapping(value = "/orderWeightAndVolume/{articleNum}")
	public ArticlePacketCalculations getArticleTotalWeightAndVolume(@PathVariable String articleNum) {
		return articleService.getWeightAndVolumeByArtNum(articleNum);
	}

	@GetMapping(value = "/orderArticlesName/{articleNum}")
	public String getAssociatedArticlesName(@PathVariable String articleNum) {
		return articleService.getArticleNameByArtNum(articleNum);
	}

	@GetMapping(value = "/orderArticles/{articleNum}")
	public ResponseEntity<OrderArticleResponse> getAssociatedArticles(@PathVariable String articleNum) {

		OrderArticleResponse orderArticleResponse = articleService.getArticleByArtNum(articleNum);

		if (orderArticleResponse != null) {
			return new ResponseEntity<>(orderArticleResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping(value = "/orderArticlesPackets/{articleNum}")
	public List<Packet> getAssociatedArticlesPacket(@PathVariable String articleNum) {
		return articleService.getPacketByArtNum(articleNum);
	}

	@PostMapping(value = "/tour")
	public ResponseEntity<Response> createTour(@RequestBody TourRequest tourRequest) {
		orderService.createTour(tourRequest);
		return new ResponseEntity<>(new Response(StatusCode.TOUR_CREATED.getCode(),
				StatusCode.TOUR_CREATED.getMessage(), StatusCode.TOUR_CREATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PutMapping(value = "/tour/{tourId}")
	public Tour updateTour(@RequestBody TourRequest tourRequest, @PathVariable Long tourId) {
		return orderService.updateTour(tourId, tourRequest);
	}

	@PutMapping(value = "/tour/{tourId}/{tourState}")
	public Tour updateTourState(@PathVariable Long tourId, @PathVariable TourStates tourState) {
		return orderService.updateTourState(tourId, tourState);
	}

	@GetMapping(value = "/tour")
	public List<Tour> getAllTour() {
		return orderService.getAllTour();
	}

	@GetMapping(value = "/tourPath/{tourId}")
	public String getAllTour(@PathVariable Long tourId) {
		return orderService.getTourPath(tourId);
	}

	@PutMapping(value = "/addOrderToTour/{tourId}")
	public Tour addOrderToTour(@RequestBody TourOrderRequest tourOrderRequest, @PathVariable Long tourId) {
		return orderService.addOrderToTour(tourId, tourOrderRequest);
	}

	@DeleteMapping(value = "/removeOrderFromTour/{tourId}/{orderId}")
	public ResponseEntity<Response> removeOrderFromTour(@PathVariable Long tourId, @PathVariable Long orderId) {
		orderService.removeOrderFromTour(tourId, orderId);
		return new ResponseEntity<>(
				new Response(StatusCode.ORDER_REMOVED_TOUR.getCode(), StatusCode.ORDER_REMOVED_TOUR.getMessage(),
						StatusCode.ORDER_REMOVED_TOUR.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}
}
