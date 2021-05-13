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
package com.maxxsoft.microServices.orderService.interceptor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.microServices.orderService.model.Order;
import com.maxxsoft.microServices.orderService.repository.OrderRepository;
import com.maxxsoft.microServices.orderService.service.impl.OrderServiceImpl;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Component
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderStates, OrderEvents> {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public void preStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message,
			Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine) {
		Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(OrderServiceImpl.ORDER_ID, -1L)))
					.ifPresent(orderId -> {
						Order order = orderRepository.getOne(orderId);
						order.setOrderState(state.getId());
						orderRepository.save(order);
					});
		});
	}
}
