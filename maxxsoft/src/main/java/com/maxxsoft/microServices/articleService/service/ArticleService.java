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
package com.maxxsoft.microServices.articleService.service;

import java.util.List;
import java.util.Optional;

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.model.request.ArticlePacketCalculations;
import com.maxxsoft.microServices.articleService.model.request.ArticleRequest;
import com.maxxsoft.microServices.articleService.model.request.ArticleSetRequest;
import com.maxxsoft.microServices.articleService.model.request.PacketRequest;
import com.maxxsoft.microServices.orderService.model.response.OrderArticleResponse;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface ArticleService {

	public Optional<Article> findArticleById(Long articleId);

	public Optional<Article> findArticleNumber(String artNum);

	public Optional<ArticleSet> findArticleSetById(Long articleSetId);

	public Optional<Packet> findPacketById(Long packetId);

	public List<Article> articles();

	public List<ArticleSet> articleSets();

	public List<Packet> packets();

	public List<Packet> packetsByArticleId(Long articleId);

	public void createArticle(ArticleRequest articleRequest);

	public void createArticleSet(ArticleSetRequest articleSetRequest);

	public void createPacket(PacketRequest packetRequest);

	public void updateArticle(Long articleId, ArticleRequest articleRequest);

	public void updateArticleSet(Long articleSetId, ArticleSetRequest articleSetRequest);

	public void updatePacket(PacketRequest packetRequest, Long packetId);

	public void deleteArticle(Long articleId);

	public void deleteArticleSet(Long articleSetId);

	public void deletePacket(Long packetId);

	public List<Article> getArticlesByArticleSet(Long articleSetId);

	public List<Packet> packetsByArticleSetId(Long articleSetId);

	public Double getTotalArticleSetWeight(Long articleSetId);

	public Double getTotalArticleWeight(Long articleId);

	public ErrorCode checkDuplicateArticleValues(String longName, String shortName, String articleNumber, String ean);

	public ErrorCode checkDuplicateArticleSetValues(String longName, String shortName, String articleSetNumber,
			String ean);

	public ErrorCode checkDuplicateArticleSetValuesForUpdate(String longName, String shortName, String articleSetNumber,
			String ean);

	public ErrorCode checkDuplicateArticleValuesForUpdate(String longName, String shortName, String articleNumber,
			String ean);

	public int getStock(String artNum);

	public double getWeightByArtNum(String artNum);

	public String getArticleNameByArtNum(String artNum);

	public OrderArticleResponse getArticleByArtNum(String artNum);

	public List<Packet> getPacketByArtNum(String artNum);

	public void deleteBarcode(Long barcodeId);

	public boolean verifyBarcode(String barcode, String artNum);

	public ArticlePacketCalculations getTotalArticleSetWeightAndVolume(Long articleSetId);

	public ArticlePacketCalculations getTotalArticleWeightAndVolume(Long articleId);

	public ArticlePacketCalculations getWeightAndVolumeByArtNum(String artNum);

}