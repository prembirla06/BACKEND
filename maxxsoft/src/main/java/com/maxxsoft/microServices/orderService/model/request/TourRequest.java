package com.maxxsoft.microServices.orderService.model.request;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.maxxsoft.common.model.TourStates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TourRequest {

	private String tourName;

	@Enumerated(EnumType.STRING)
	private TourStates tourState;

	private String note;

	private String path;

	private String vehicleCategory;

	Set<TourOrderRequest> TourOrderRequestSet = new HashSet();
}
