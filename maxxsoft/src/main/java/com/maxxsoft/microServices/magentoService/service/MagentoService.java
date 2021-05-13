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
package com.maxxsoft.microServices.magentoService.service;

import java.util.List;

import org.springframework.http.HttpHeaders;

import com.maxxsoft.microServices.articleService.model.request.AttributeResult;
import com.maxxsoft.microServices.articleService.model.request.Categories;
import com.maxxsoft.microServices.magentoService.model.MagentoCustomAttribute;
import com.maxxsoft.microServices.magentoService.model.MagentoUser;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoMediaResponse;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoPriceRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface MagentoService {

	public String getProductList();

	public MagentoProductResponse getProductBySKU1(String sku, HttpHeaders headers);

	public String getProductBySKU(String sku, HttpHeaders headers);

	public String getMogentoAccessToken(MagentoUser magentoUser);

	public String getCategories();

	public String saveProduct(MagentoProductList magentoProductList, HttpHeaders headers);

	public String deleteProductBySku(String sku);

	public MagentoCustomAttribute createCustomAttribute(MagentoCustomAttribute magentoCustomAttribute);

	public List<MagentoCustomAttribute> getCustomAttributes();

	public void deleteCustomAttribute(Long magentoCustomAttributeId);

	public String updateProductBySku(String sku, MagentoProductList magentoProductList, HttpHeaders headers);

	public String createMedia(String sku, MagentoMediaRequest magentoMediaRequest, HttpHeaders headers);

	public MagentoMediaResponse[] getMedia(String sku, HttpHeaders headers);

	public String deleteMedia(String sku, Long entryId, HttpHeaders headers);

	public String updateMedia(String sku, Long entryId, MagentoMediaRequest magentoMediaRequest, HttpHeaders headers);

	public String getAttributeData(String attributeCode, HttpHeaders headers);

	public HttpHeaders getHeader();

	public AttributeResult[] getAttributeData1(String attributeCode, HttpHeaders headers);

	public Categories getCategories1(HttpHeaders headers);

	public String updateProductBySkuFormUI(String sku, MagentoProductList magentoProductList, HttpHeaders headers);

	public void saveProductMedia(MagentoProductList magentoProductList, HttpHeaders headers);

	public String deleteSpecialPrice(MagentoPriceRequest magentoPriceRequest, HttpHeaders headers);

	public String getMagentoCategoryName(int categoryId, Categories categories);

	public void deleteCategoryFromLocalMagentoProduct(String sku, int categoryId);

	public void addCategoryToLocalMagentoProduct(String sku, int categoryPosition, String categoryId,
			String categoryName);

	public String deleteMagentoProductFromCategory(String sku, int categoryId, HttpHeaders headers);

	public String urlKeyBuilder(String url_key);

	public MagentoOrderResponse getAllOrders(HttpHeaders headers);

	public MagentoOrderResponse getNewOrders(HttpHeaders headers, String lastOrderIdFetched);
}