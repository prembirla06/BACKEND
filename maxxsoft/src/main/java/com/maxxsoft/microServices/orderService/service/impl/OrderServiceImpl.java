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
package com.maxxsoft.microServices.orderService.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.common.model.TourStates;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.mailService.model.Mail;
import com.maxxsoft.microServices.mailService.service.EmailSenderService;
import com.maxxsoft.microServices.orderService.interceptor.OrderStateChangeInterceptor;
import com.maxxsoft.microServices.orderService.jsonmodel.Address;
import com.maxxsoft.microServices.orderService.jsonmodel.BillingAddress;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrder;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;
import com.maxxsoft.microServices.orderService.model.Order;
import com.maxxsoft.microServices.orderService.model.OrderArticleRelation;
import com.maxxsoft.microServices.orderService.model.StateMachineCustomResponse;
import com.maxxsoft.microServices.orderService.model.Tour;
import com.maxxsoft.microServices.orderService.model.TourPath;
import com.maxxsoft.microServices.orderService.model.request.OrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourOrderRequest;
import com.maxxsoft.microServices.orderService.model.request.TourRequest;
import com.maxxsoft.microServices.orderService.repository.OrderArticleRelationRepository;
import com.maxxsoft.microServices.orderService.repository.OrderRepository;
import com.maxxsoft.microServices.orderService.repository.TourRepository;
import com.maxxsoft.microServices.orderService.service.OrderService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	public static final String ORDER_ID = "orderId";
	boolean outOfStock = false;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TourRepository tourRepository;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private OrderArticleRelationRepository orderArticleRelationRepository;

	@Autowired
	StateMachineFactory<OrderStates, OrderEvents> stateMachineFactory;
	@Autowired
	OrderStateChangeInterceptor orderStateChangeInterceptor;

	@Value("${spring.mail.username}")
	private String senderUserEmail;

	@Override
	public void receive(MagentoOrderResponse magentoOrderResponse) {

		List<MagentoOrder> magentoOrders = magentoOrderResponse.getItems();

		magentoOrders.forEach(magentoOrder -> {
			Order order = new Order();
			Tour tour = new Tour();
			tour.setTourName("T");
			Set<Order> orderSet = new HashSet<>();

			// System.out.println("magentoOrder.getIncrementId()--------------"
			// + magentoOrder.getIncrementId());
			order.setOrderState(OrderStates.RECEIVED);
			order.setOrderNumber(magentoOrder.getIncrementId());
			order.setEmail(magentoOrder.getCustomerEmail());
			order.setOrderDate(magentoOrder.getCreatedAt());
			order.setMarketPlace("Magento");
			order.setMarketPlaceOrderStatus(magentoOrder.getStatus());
			order.setShippingCosts(Float.valueOf(magentoOrder.getBaseShippingInclTax()));

			if (magentoOrder.getPayment().getMethod() != null) {
				if (magentoOrder.getPayment().getMethod().equals("banktransfer")) {
					order.setPaymentRefferenceNumber(magentoOrder.getIncrementId());
					order.setPaymentMethod(magentoOrder.getPayment().getMethod());
				}
				if (magentoOrder.getPayment().getMethod().equals("paypal_express")
						|| magentoOrder.getPayment().getMethod().equals("iways_paypalplus_payment")) {
					order.setPaymentRefferenceNumber(magentoOrder.getPayment().getLastTransId());
					order.setPaymentMethod("Paypal");
				}
				if (magentoOrder.getPayment().getMethod().equals("checkmo")) {
					// order.setPaymentRefferenceNumber(magentoOrder.getIncrementId());
					order.setPaymentMethod(magentoOrder.getPayment().getMethod());
				}

			}

			BillingAddress orderBillingAddress = magentoOrder.getBillingAddress();
			order.setInvoiceCity(orderBillingAddress.getCity());
			order.setInvoiceCountryCode(orderBillingAddress.getCountryId());
			order.setInvoiceZipCode(orderBillingAddress.getPostcode());
			order.setInvoiceStreetAndHouseNumber(orderBillingAddress.getStreet().get(0));
			if (orderBillingAddress.getStreet().size() > 1)
				order.setInvoiceStreetAndHouseNumber(
						order.getInvoiceStreetAndHouseNumber() + " " + orderBillingAddress.getStreet().get(1));
			order.setInvoicePhoneNumber(orderBillingAddress.getTelephone());
			order.setInvoiceName1(orderBillingAddress.getFirstname() + " " + orderBillingAddress.getLastname());
			order.setInvoiceName2(orderBillingAddress.getCompany());
			order.setInvoiceName3("");

			// Delivery Data
			Address orderShippingAddress = magentoOrder.getExtensionAttributes().getShippingAssignments().get(0)
					.getShipping().getAddress();
			order.setDeliveryCity(orderShippingAddress.getCity());
			order.setDeliveryCountryCode(orderShippingAddress.getCountryId());
			order.setDeliveryZipCode(orderShippingAddress.getPostcode());
			order.setDeliveryStreetAndHouseNumber(orderShippingAddress.getStreet().get(0));
			if (orderShippingAddress.getStreet().size() > 1)
				order.setDeliveryStreetAndHouseNumber(
						order.getDeliveryStreetAndHouseNumber() + " " + orderShippingAddress.getStreet().get(1));
			order.setDeliveryPhoneNumber(orderShippingAddress.getTelephone());
			order.setDeliveryName1(orderShippingAddress.getFirstname() + " " + orderShippingAddress.getLastname());
			order.setDeliveryName2(orderShippingAddress.getCompany());
			order.setDeliveryName3("");

			// order.setSameAsInvoice(checkIfDeliveryAndInvoiceAddressSame(order));
			// setJournalStatus(order, magentoOrder.getStatus());
			Set<OrderArticleRelation> orderArticleRelationSet = new HashSet<>();
			magentoOrder.getItems().forEach(item -> {
				OrderArticleRelation orderArticle = new OrderArticleRelation();
				orderArticle.setArticleNumber(item.getSku());
				orderArticle.setQuantity(item.getQtyOrdered());
				orderArticle.setPrice(item.getPriceInclTax());
				// orderArticle.setOrderId(order.getOrderId());
				orderArticle.setOrder(order);
				orderArticleRelationSet.add(orderArticle);
				// order.addOrderArticleRelation(orderArticle);
				// orderArticleRelationRepository.saveAndFlush(orderArticle);
			});
			Set<Integer> deliveryTimeSet = new HashSet<>();
			magentoOrder.getItems().forEach(item -> {
				deliveryTimeSet.add(articleService.getArticleByArtNum(item.getSku()).getDeliveryTime());
			});
			OptionalInt maxDeliveryTime = deliveryTimeSet.stream().mapToInt(v -> v).max();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime orderDate = LocalDateTime.parse(magentoOrder.getCreatedAt(), formatter);
			if (maxDeliveryTime.isPresent()) {
				order.setOrderDeliveryDate(orderDate.plusDays(maxDeliveryTime.getAsInt()).toString());
			}
			order.setOrderArticleRelationSet(orderArticleRelationSet);
			// orderSet.add(order);
			// tour.setOrderSet(orderSet);
			// order.setTour(tour);
			Order savedOrder = orderRepository.saveAndFlush(order);
			checkPaymentAndMoveOrder(savedOrder);
			checkStockAndMoveOrder(savedOrder);
		});
	}

	@Override
	public Order updateOrder(OrderRequest orderRequest, Long orderId) {
		Order savedOrder = orderRepository.getOne(orderId);
		// savedOrder.setTourName(orderRequest.getTourName());
		savedOrder.setTourPosition(orderRequest.getTourPosition());
		savedOrder.setTourDeliveryDate(orderRequest.getTourDeliveryDate());
		savedOrder.setDeliveryCity(orderRequest.getDeliveryCity());
		savedOrder.setDeliveryCountryCode(orderRequest.getDeliveryCountryCode());
		savedOrder.setDeliveryName1(orderRequest.getDeliveryName1());
		savedOrder.setDeliveryName2(orderRequest.getDeliveryName2());
		savedOrder.setDeliveryName3(orderRequest.getDeliveryName3());
		savedOrder.setDeliveryPhoneNumber(orderRequest.getDeliveryPhoneNumber());
		savedOrder.setDeliveryStreetAndHouseNumber(orderRequest.getDeliveryStreetAndHouseNumber());
		savedOrder.setDeliveryZipCode(orderRequest.getDeliveryZipCode());
		savedOrder.setInvoiceCity(orderRequest.getInvoiceCity());
		savedOrder.setInvoiceCountryCode(orderRequest.getInvoiceCountryCode());
		savedOrder.setInvoiceName1(orderRequest.getInvoiceName1());
		savedOrder.setInvoiceName2(orderRequest.getInvoiceName2());
		savedOrder.setInvoiceName3(orderRequest.getInvoiceName3());
		savedOrder.setInvoicePhoneNumber(orderRequest.getInvoicePhoneNumber());
		savedOrder.setInvoiceStreetAndHouseNumber(orderRequest.getInvoiceStreetAndHouseNumber());
		savedOrder.setInvoiceZipCode(orderRequest.getInvoiceZipCode());
		savedOrder.setNote(orderRequest.getNote());
		savedOrder.setOnHoldUntil(orderRequest.getOnHoldUntil());

		return orderRepository.saveAndFlush(savedOrder);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> getAllActiveOrders() {
		return orderRepository.findAllActiveOrders();
	}

	@Override
	public List<Order> getAllPaidOrders() {
		return orderRepository.findAllPaidOrders();
	}

	@Override
	public Optional<Order> getOrderById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	@Override
	public void checkStockAndMoveOrder(Order order) {
		outOfStock = false;
		order.getOrderArticleRelationSet().forEach(relation -> {
			int stock = articleService.getStock(relation.getArticleNumber());
			if (stock <= 0) {
				outOfStock = true;
			}
		});

		if (outOfStock) {
			sendEventForOrder(order.getOrderId(), OrderEvents.UNAVAILABLE);
		} else {
			sendEventForOrder(order.getOrderId(), OrderEvents.AVAILABLE);
		}

	}

	@Override
	public void checkPaymentAndMoveOrder(Order order) {
		if ("Moneyorder".equalsIgnoreCase(order.getPaymentMethod())
				|| "checkmo".equalsIgnoreCase(order.getPaymentMethod())) {
			sendEventForOrder(order.getOrderId(), OrderEvents.PAYMENT_UNCONFIRMED);
		} else {
			sendEventForOrder(order.getOrderId(), OrderEvents.PAID);
		}

	}

	@Override
	public String findLatestOrdersNumber(String marketPlace) {
		Order order = orderRepository.findTopByMarketPlaceOrderByOrderIdDesc(marketPlace);
		if (order == null) {
			return null;
		}
		return order.getOrderNumber();
	}

	@Override
	public StateMachine<OrderStates, OrderEvents> cancel(Long orderId) {
		StateMachine<OrderStates, OrderEvents> sm = build(orderId);
		sendEvent(orderId, sm, OrderEvents.CANCEL);
		return sm;
	}

	@Override
	public StateMachineCustomResponse sendEventForOrder(Long orderId, OrderEvents orderEvent) {
		StateMachineCustomResponse smcr = new StateMachineCustomResponse();
		StateMachine<OrderStates, OrderEvents> sm = build(orderId);
		smcr.setOldState(sm.getState().getId().toString());
		sendEvent(orderId, sm, orderEvent);
		smcr.setNewState(sm.getState().getId().toString());
		if (orderEvent.equals(OrderEvents.EXTERNAL_SHIPPING)) {
			sendExternalDeliveryEmail(orderId);
		}
		return smcr;
	}

	private void sendEvent(Long orderId, StateMachine<OrderStates, OrderEvents> sm, OrderEvents event) {
		Message message = MessageBuilder.withPayload(event).setHeader(ORDER_ID, orderId).build();
		sm.sendEvent(message);
	}

	private StateMachine<OrderStates, OrderEvents> build(Long orderId) {
		Order order = orderRepository.getOne(orderId);

		StateMachine<OrderStates, OrderEvents> stateMachine = stateMachineFactory
				.getStateMachine(Long.toString(order.getOrderId()));

		stateMachine.stop();

		stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
			sma.addStateMachineInterceptor(orderStateChangeInterceptor);
			sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderState(), null, null, null));
		});

		stateMachine.start();

		return stateMachine;
	}

	@Override
	public void createTour(TourRequest tourRequest) {

		Tour tour = new Tour();
		TourPath tourPath = new TourPath();
		tour.setTourName(tourRequest.getTourName());
		tour.setTourState(TourStates.PENDING);
		tour.setNote(tourRequest.getNote());
		tour.setVehicleCategory(tourRequest.getVehicleCategory());
		Set<Order> orderSet = new HashSet<>();
		tourRequest.getTourOrderRequestSet().forEach(tor -> {
			Order order = orderRepository.getOne(tor.getOrderId());
			order.setTourPosition(tor.getTourPosition());
			order.setTourDeliveryDate(tor.getTourDeliveryDate());
			order.setTourName(tourRequest.getTourName());
			orderSet.add(order);
			sendEventForOrder(order.getOrderId(), OrderEvents.INTERNAL_SHIPPING);
			sendDeliveryEmail(order);
		});
		tour.setOrderSet(orderSet);
		tourPath.setPath(tourRequest.getPath());
		tourPath.setTour(tour);
		tour.setTourPath(tourPath);
		tourRepository.save(tour);
	}

	@Override
	public Tour updateTour(Long tourId, TourRequest tourRequest) {

		Optional<Tour> tourOptional = tourRepository.findById(tourId);
		if (tourOptional.isPresent()) {
			Tour tour = tourOptional.get();
			tour.setTourName(tourRequest.getTourName());
			tour.setNote(tourRequest.getNote());
			tour.setVehicleCategory(tourRequest.getVehicleCategory());
			Set<Order> orderSet = new HashSet<>();
			tourRequest.getTourOrderRequestSet().forEach(tor -> {
				Order order = orderRepository.getOne(tor.getOrderId());
				order.setTourPosition(tor.getTourPosition());
				order.setTourDeliveryDate(tor.getTourDeliveryDate());
				order.setTourName(tourRequest.getTourName());
				orderSet.add(order);
			});
			tour.setOrderSet(orderSet);
			return tourRepository.saveAndFlush(tour);
		}
		return null;

	}

	@Override
	public Tour updateTourState(Long tourId, TourStates tourState) {

		Optional<Tour> tourOptional = tourRepository.findById(tourId);
		if (tourOptional.isPresent()) {
			Tour tour = tourOptional.get();
			tour.setTourState(tourState);
			return tourRepository.saveAndFlush(tour);
		}
		return null;

	}

	@Override
	public Tour addOrderToTour(Long tourId, TourOrderRequest tourOrderRequest) {

		Optional<Tour> tourOptional = tourRepository.findById(tourId);
		if (tourOptional.isPresent()) {
			Tour tour = tourOptional.get();
			Order order = orderRepository.getOne(tourOrderRequest.getOrderId());
			order.setTourPosition(tourOrderRequest.getTourPosition());
			order.setTourDeliveryDate(tourOrderRequest.getTourDeliveryDate());
			order.setTourName(tour.getTourName());
			tour.getOrderSet().add(order);
			sendEventForOrder(order.getOrderId(), OrderEvents.INTERNAL_SHIPPING);
			return tourRepository.save(tour);
		}
		return null;

	}

	@Override
	public void removeOrderFromTour(Long tourId, Long orderId) {

		Optional<Tour> tourOptional = tourRepository.findById(tourId);
		if (tourOptional.isPresent()) {
			Tour tour = tourOptional.get();
			Order order = orderRepository.getOne(orderId);
			order.setTourDeliveryDate(null);
			order.setTourName(null);
			order.setTourPosition(null);
			tour.getOrderSet().remove(order);
			sendEventForOrder(order.getOrderId(), OrderEvents.REMOVED_FROM_TOUR);
			tourRepository.save(tour);
		}

	}

	@Override
	public List<Tour> getAllTour() {
		return tourRepository.findAll();
	}

	@Override
	public String getTourPath(Long tourId) {
		Optional<Tour> tourOptional = tourRepository.findById(tourId);
		if (tourOptional.isPresent()) {
			Tour tour = tourOptional.get();
			return tour.getTourPath().getPath();
		}
		return null;
	}

	private void sendDeliveryEmail(Order order) {
		Mail mail = new Mail();
		mail.setFrom(senderUserEmail);
		mail.setMailTo(order.getEmail());
		mail.setSubject("Lieferung ihrer Bestellung von moebel-guenstig24.de");
		mail.setTemplateName("deliveryTour");

		Map<String, Object> model = new HashMap<String, Object>();
		LocalDateTime deliveryDateTime = Instant.ofEpochMilli((long) Double.parseDouble(order.getTourDeliveryDate()))
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
		model.put("deliveryDateTime", deliveryDateTime.format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")));
		model.put("deliveryPhoneNumber", order.getDeliveryPhoneNumber());
		model.put("customerName", order.getInvoiceName1());
		mail.setProps(model);

		try {
			emailSenderService.sendEmail(mail);
		} catch (javax.mail.MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendExternalDeliveryEmail(Long orderId) {
		Order order = orderRepository.getOne(orderId);
		Mail mail = new Mail();
		mail.setFrom(senderUserEmail);
		mail.setMailTo(order.getEmail());
		mail.setSubject("External Shipping");
		mail.setTemplateName("externalDelivery");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("deliveryPhoneNumber", order.getDeliveryPhoneNumber());
		model.put("customerName", order.getInvoiceName1());
		mail.setProps(model);

		try {
			emailSenderService.sendEmail(mail);
		} catch (javax.mail.MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
