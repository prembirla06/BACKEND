package com.maxxsoft.microServices.orderService.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderRequest {

	private String tourPosition;

	private String tourDeliveryDate;

	private String deliveryCity;

	private String deliveryCountryCode;

	private String deliveryName1;

	private String deliveryName2;

	private String deliveryName3;

	private String deliveryPhoneNumber;

	private String deliveryStreetAndHouseNumber;

	private String deliveryZipCode;

	private String invoiceCity;

	private String invoiceCountryCode;

	private String invoiceName1;

	private String invoiceName2;

	private String invoiceName3;

	private String invoicePhoneNumber;

	private String invoiceStreetAndHouseNumber;

	private String invoiceZipCode;

	private String note;

	private String onHoldUntil;

}
