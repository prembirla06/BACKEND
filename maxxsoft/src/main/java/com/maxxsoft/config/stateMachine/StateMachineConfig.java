package com.maxxsoft.config.stateMachine;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.config.stateMachine.actions.DeliveryAction;
import com.maxxsoft.config.stateMachine.actions.ShipAction;
import com.maxxsoft.config.stateMachine.guards.DeliveryGuard;

import lombok.extern.java.Log;

@Log
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

	@Autowired
	ShipAction shipAction;

	@Autowired
	DeliveryAction deliveryAction;

	@Autowired
	DeliveryGuard deliveryGuard;

	@Override
	public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
		states.withStates().initial(OrderStates.RECEIVED).states(EnumSet.allOf(OrderStates.class))
				.end(OrderStates.DELIVERED).end(OrderStates.CANCELLED);
		log.info("testing");
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
		transitions.withExternal().source(OrderStates.RECEIVED).target(OrderStates.PAID).event(OrderEvents.PAID).and()
				.withExternal().source(OrderStates.RECEIVED).target(OrderStates.PAYMENT_UNCONFIRMED)
				.event(OrderEvents.PAYMENT_UNCONFIRMED).and().withExternal().source(OrderStates.PAID)
				.target(OrderStates.PROCESSING).event(OrderEvents.AVAILABLE).and().withExternal()
				.source(OrderStates.PAYMENT_UNCONFIRMED).target(OrderStates.PAID).event(OrderEvents.PAID).and()
				.withExternal().source(OrderStates.PAID).target(OrderStates.PAYMENT_UNCONFIRMED)
				.event(OrderEvents.UNPAID).and().withExternal().source(OrderStates.PAYMENT_UNCONFIRMED)
				.target(OrderStates.CANCELLED).event(OrderEvents.CANCEL).and().withExternal().source(OrderStates.PAID)
				.target(OrderStates.UNAVAILABLE).event(OrderEvents.UNAVAILABLE).and().withExternal()
				.source(OrderStates.UNAVAILABLE).target(OrderStates.PROCESSING).event(OrderEvents.AVAILABLE).and()
				.withExternal().source(OrderStates.PROCESSING).target(OrderStates.INTERNAL_SHIPPING)
				.event(OrderEvents.INTERNAL_SHIPPING).and().withExternal().source(OrderStates.INTERNAL_SHIPPING)
				.target(OrderStates.PROCESSING).event(OrderEvents.REMOVED_FROM_TOUR).and().withExternal()
				.source(OrderStates.PROCESSING).target(OrderStates.EXTERNAL_SHIPPING)
				.event(OrderEvents.EXTERNAL_SHIPPING).and().withExternal().source(OrderStates.INTERNAL_SHIPPING)
				.target(OrderStates.PACKED).event(OrderEvents.PACK).and().withExternal()
				.source(OrderStates.EXTERNAL_SHIPPING).target(OrderStates.PACKED_FOR_ES).event(OrderEvents.PACK_ES)
				.and().withExternal().source(OrderStates.PACKED).target(OrderStates.INTERNAL_SHIPPING)
				.event(OrderEvents.UNPACK).and().withExternal().source(OrderStates.PACKED_FOR_ES)
				.target(OrderStates.EXTERNAL_SHIPPING).event(OrderEvents.UNPACK_ES).and().withExternal()
				.source(OrderStates.PACKED).target(OrderStates.SHIPPED).event(OrderEvents.TRANSACTION).and()
				.withExternal().source(OrderStates.PACKED_FOR_ES).target(OrderStates.SHIPPED_BY_ES)
				.event(OrderEvents.TRANSACTION_ES).and().withExternal().source(OrderStates.SHIPPED)
				.target(OrderStates.DELIVERED).event(OrderEvents.DELIVERY).and().withExternal()
				.source(OrderStates.SHIPPED_BY_ES).target(OrderStates.DELIVERED_BY_ES).event(OrderEvents.DELIVERY_ES)
				.and().withExternal().source(OrderStates.INTERNAL_SHIPPING).target(OrderStates.SHIPPED)
				.event(OrderEvents.TRANSACTION).and().withExternal().source(OrderStates.EXTERNAL_SHIPPING)
				.target(OrderStates.SHIPPED).event(OrderEvents.TRANSACTION).and().withExternal()
				.source(OrderStates.SHIPPED).target(OrderStates.DELIVERED).event(OrderEvents.DELIVERY)
				.guard(deliveryGuard).and().withExternal().source(OrderStates.PROCESSING).target(OrderStates.HOLD)
				.event(OrderEvents.HOLD).and().withExternal().source(OrderStates.HOLD).target(OrderStates.PROCESSING)
				.event(OrderEvents.UNHOLD).and().withExternal().source(OrderStates.PROCESSING)
				.target(OrderStates.CANCELLED).event(OrderEvents.CANCEL).and().withExternal().source(OrderStates.PAID)
				.target(OrderStates.CANCELLED).event(OrderEvents.CANCEL).and().withExternal()
				.source(OrderStates.RECEIVED).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {

		StateMachineListenerAdapter<OrderStates, OrderEvents> adapter = new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
			@Override
			public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
				log.info(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
			}
		};
		config.withConfiguration().autoStartup(false).listener(adapter);
	}

}
