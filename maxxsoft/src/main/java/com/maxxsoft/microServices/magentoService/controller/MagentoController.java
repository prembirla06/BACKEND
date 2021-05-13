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
package com.maxxsoft.microServices.magentoService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.microServices.magentoService.model.MagentoCustomAttribute;
import com.maxxsoft.microServices.magentoService.model.MagentoUser;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoMediaResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.service.MagentoService;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/magento")
@Transactional
public class MagentoController {

	@Autowired
	private MagentoService magentoService;

	@PostMapping(value = "/getToken")
	public String addArtgetMogentoAccessTokenicleImage(@RequestBody MagentoUser magentoUser) {
		return magentoService.getMogentoAccessToken(magentoUser);
	}

	@PostMapping(value = "/products")
	public String saveProducts(@RequestBody MagentoProductList magentoProductList) {
		HttpHeaders headers = magentoService.getHeader();
		String response = magentoService.saveProduct(magentoProductList, headers);
		magentoService.saveProductMedia(magentoProductList, headers);
		return response;
	}

	@PutMapping(value = "/products/{sku}")
	public String updateProducts(@PathVariable String sku, @RequestBody MagentoProductList magentoProductList) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.updateProductBySkuFormUI(sku, magentoProductList, headers);
	}

	@GetMapping(value = "/products")
	public String getAllProducts() {
		return magentoService.getProductList();
	}

	@GetMapping(value = "/products/{sku}")
	public String getProductBySKU(@PathVariable String sku) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.getProductBySKU(sku, headers);
	}

	@DeleteMapping(value = "/products/{sku}")
	public String deleteBySKU(@PathVariable String sku) {
		return magentoService.deleteProductBySku(sku);
	}

	@GetMapping(value = "/categories")
	public String getAllMagentoCategories() {
		return magentoService.getCategories();
	}

	@PostMapping(value = "/customAttribute")
	public MagentoCustomAttribute createCustomAttribute(@RequestBody MagentoCustomAttribute magentoCustomAttribute) {
		return magentoService.createCustomAttribute(magentoCustomAttribute);
	}

	@GetMapping(value = "/customAttribute")
	public List<MagentoCustomAttribute> getCustomAttributes() {
		return magentoService.getCustomAttributes();
	}

	@DeleteMapping(value = "/customAttribute/{magentoCustomAttributeId}")
	public void deleteCustomAttribute(@PathVariable Long magentoCustomAttributeId) {
		magentoService.deleteCustomAttribute(magentoCustomAttributeId);
	}

	@GetMapping(value = "/products/{sku}/media")
	public MagentoMediaResponse[] getMedia(@PathVariable String sku) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.getMedia(sku, headers);
	}

	@PostMapping(value = "/products/{sku}/media")
	public String createMedia(@PathVariable String sku, @RequestBody MagentoMediaRequest magentoMediaRequest) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.createMedia(sku, magentoMediaRequest, headers);
	}

	@DeleteMapping(value = "/products/{sku}/media/{enrtyId}")
	public String deleteMedia(@PathVariable String sku, @PathVariable Long entryId) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.deleteMedia(sku, entryId, headers);
	}

	@PutMapping(value = "/products/{sku}/media/{enrtyId}")
	public String updateProducts(@PathVariable String sku, @PathVariable Long entryId,
			@RequestBody MagentoMediaRequest magentoMediaRequest) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.updateMedia(sku, entryId, magentoMediaRequest, headers);
	}

	@GetMapping(value = "/products/attributes/{attributeCode}/options")
	public String getAttribute(@PathVariable String attributeCode) {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.getAttributeData(attributeCode, headers);
	}

	@GetMapping(value = "/urlKey/{name}")
	public String getUrlKey(@PathVariable String name) {
		return magentoService.urlKeyBuilder(name);
	}

	@GetMapping(value = "/allOrders")
	public MagentoOrderResponse getAllOrders() {
		HttpHeaders headers = magentoService.getHeader();
		return magentoService.getAllOrders(headers);
	}

}