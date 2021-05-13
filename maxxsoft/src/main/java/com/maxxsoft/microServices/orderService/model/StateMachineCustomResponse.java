package com.maxxsoft.microServices.orderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateMachineCustomResponse {

	private String oldState;
	private String newState;

}
