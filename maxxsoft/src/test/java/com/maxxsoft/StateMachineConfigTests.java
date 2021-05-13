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
package com.maxxsoft;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;

@SpringBootTest
public class StateMachineConfigTests {

	@Autowired
	StateMachineFactory<OrderStates, OrderEvents> factory;

	// @Test
	public void testStateMachineInternalShipping() {

		StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(UUID.randomUUID());

		stateMachine.start();
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNAVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.AVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNPAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.PAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.INTERNAL_SHIPPING);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.TRANSACTION);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.DELIVERY);
		System.out.println(stateMachine.getState().toString());

	}

	// @Test
	public void testStateMachineExternalShipping() {

		StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(UUID.randomUUID());

		stateMachine.start();
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNAVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.AVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNPAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.PAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.EXTERNAL_SHIPPING);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.TRANSACTION);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.DELIVERY);
		System.out.println(stateMachine.getState().toString());

	}

	// @Test
	public void testStateMachineHoldAndCancel() {

		StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(UUID.randomUUID());

		stateMachine.start();
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNAVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.AVAILABLE);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNPAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.PAID);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.HOLD);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.UNHOLD);
		System.out.println(stateMachine.getState().toString());

		stateMachine.sendEvent(OrderEvents.CANCEL);
		System.out.println(stateMachine.getState().toString());

	}

}
