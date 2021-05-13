package com.maxxsoft.microServices.orderService.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderArticleResponse {

	private String articleNumber;

	private String shortName;

	private String substituteNumber;

	private int stock;

	private int actualStock;

	private int deliveryTime;

}
