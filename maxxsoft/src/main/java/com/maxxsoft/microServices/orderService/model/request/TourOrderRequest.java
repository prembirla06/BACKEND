package com.maxxsoft.microServices.orderService.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TourOrderRequest {

	private Long orderId;

	private String tourPosition;

	private String tourDeliveryDate;

}
