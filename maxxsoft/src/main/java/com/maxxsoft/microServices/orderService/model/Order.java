package com.maxxsoft.microServices.orderService.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maxxsoft.common.model.OrderStates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders", schema = "public")
public class Order {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long orderId;

	@Enumerated(EnumType.STRING)
	private OrderStates orderState;

	private String email;

	// @Column(name = "is_same_as_invoice")
	// private Boolean isSameAsInvoice;

	private String deliveryCity;

	private String deliveryCountryCode;

	private String deliveryName1;

	private String deliveryName2;

	private String deliveryName3;

	private String deliveryPhoneNumber;

	private String deliveryStreetAndHouseNumber;

	private String deliveryZipCode;

	private String estimateShippingDate;

	private String invoiceCity;

	private String invoiceCountryCode;

	private String invoiceName1;

	private String invoiceName2;

	private String invoiceName3;

	private String invoicePhoneNumber;

	private String invoiceStreetAndHouseNumber;

	private String invoiceZipCode;

	private String marketPlace;

	private String note;

	private String onHoldUntil;

	private String orderDate;

	private String orderNumber;

	private String packingTimestamp;

	private String paymentMethod;

	private String paymentRefferenceNumber;

	private Float shippingCosts;

	private Boolean splitDelivery;

	private String tourName;

	private String tourPosition;

	private String tourDeliveryDate;

	private String orderDeliveryDate;

	@JsonManagedReference
	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<OrderArticleRelation> orderArticleRelationSet = new HashSet();

	private boolean packed;

	private boolean controlled;

	private boolean invoiceCreated;

	private String marketPlaceOrderStatus;

	// @JsonBackReference
	// @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	// @ManyToOne(fetch = FetchType.LAZY, optional = true)
	// @JoinColumn(name = "tourId", nullable = true)
	// private Tour tour;

}
