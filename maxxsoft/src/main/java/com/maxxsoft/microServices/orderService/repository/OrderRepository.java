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
package com.maxxsoft.microServices.orderService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxxsoft.microServices.orderService.model.Order;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findTopByMarketPlaceOrderByOrderIdDesc(String marketPlace);

	@Query(value = "SELECT o FROM Order o Where o.orderState NOT IN('PROCESSED','CANCELLED')")
	List<Order> findAllActiveOrders();

	@Query(value = "SELECT o FROM Order o Where o.orderState IN('PAID')")
	List<Order> findAllPaidOrders();

	//
	// @Override
	// @Query(value = "SELECT DISTINCT a FROM OrderRequest a JOIN FETCH
	// a.orderArticleRelationSet b")
	// List<OrderRequest> findTopByMarketPlaceOrderByOrderIdDesc();

}
