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
package com.maxxsoft.microServices.articleService.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.common.model.Response;
import com.maxxsoft.common.model.StatusCode;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.model.request.ArticleRequest;
import com.maxxsoft.microServices.articleService.model.request.ArticleSetRequest;
import com.maxxsoft.microServices.articleService.model.request.PacketRequest;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.PriceService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional
public class ArticleController {

	public static boolean ArticleExists = true;
	public static boolean ActiveArticleSetExists = false;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private PriceService priceService;

	@PostMapping(value = "/article")
	public ResponseEntity<Response> create(@RequestBody ArticleRequest articleRequest) {
		ErrorCode errorCode = articleService.checkDuplicateArticleValues(articleRequest.getLongName(),
				articleRequest.getShortName(), articleRequest.getNumber(), articleRequest.getEan());
		if (errorCode != null) {
			return new ResponseEntity<>(new Response(errorCode.getCode(), errorCode.getMessage(), errorCode.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.createArticle(articleRequest);
		return new ResponseEntity<>(new Response(StatusCode.ARTICLE_CREATED.getCode(),
				StatusCode.ARTICLE_CREATED.getMessage(), StatusCode.ARTICLE_CREATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@GetMapping(value = "/allArticles")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Article>> articles() {
		return new ResponseEntity<>(articleService.articles(), HttpStatus.OK);
	}

	@GetMapping(value = "/article/{articleId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Article> getArticleById(@PathVariable Long articleId) {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		if (articleOptional.isPresent()) {
			return new ResponseEntity<>(articleOptional.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@GetMapping(value = "/articleSet/{articleSetId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ArticleSet> getArticleSetById(@PathVariable Long articleSetId) {
		Optional<ArticleSet> articleSetOptional = articleService.findArticleSetById(articleSetId);
		if (articleSetOptional.isPresent()) {
			return new ResponseEntity<>(articleSetOptional.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@GetMapping(value = "/allArticleSets")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ArticleSet>> articleSets() {
		return new ResponseEntity<>(articleService.articleSets(), HttpStatus.OK);
	}

	@GetMapping(value = "/allPackets")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Packet>> packets() {
		return new ResponseEntity<>(articleService.packets(), HttpStatus.OK);
	}

	@GetMapping(value = "/packets/{articleId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Packet>> packetsByArticleId(@PathVariable Long articleId) {
		return new ResponseEntity<>(articleService.packetsByArticleId(articleId), HttpStatus.OK);
	}

	@GetMapping(value = "/articlesBySet/{articleSetId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Article>> articlesByArticleSetId(@PathVariable Long articleSetId) {
		return new ResponseEntity<>(articleService.getArticlesByArticleSet(articleSetId), HttpStatus.OK);
	}

	@PutMapping("/article/{articleId}")
	public ResponseEntity<Response> updateArticle(@PathVariable Long articleId,
			@RequestBody ArticleRequest articleRequest) {
		if (!articleService.findArticleById(articleId).isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		ErrorCode errorCode = articleService.checkDuplicateArticleValuesForUpdate(articleRequest.getLongName(),
				articleRequest.getShortName(), articleRequest.getNumber(), articleRequest.getEan());
		if (errorCode != null) {
			return new ResponseEntity<>(new Response(errorCode.getCode(), errorCode.getMessage(), errorCode.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.updateArticle(articleId, articleRequest);
		return new ResponseEntity<>(new Response(StatusCode.ARTICLE_UPDATED.getCode(),
				StatusCode.ARTICLE_UPDATED.getMessage(), StatusCode.ARTICLE_UPDATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PutMapping("/articleSet/{articleSetId}")
	public ResponseEntity<Response> updateArticleSet(@PathVariable Long articleSetId,
			@RequestBody ArticleSetRequest articleSetRequest) {
		if (!articleService.findArticleSetById(articleSetId).isPresent()) {
			return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_SET_NOT_EXIST.getCode(),
					ErrorCode.ARTICLE_SET_NOT_EXIST.getMessage(), ErrorCode.ARTICLE_SET_NOT_EXIST.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		ErrorCode errorCode = articleService.checkDuplicateArticleSetValuesForUpdate(articleSetRequest.getLongName(),
				articleSetRequest.getShortName(), articleSetRequest.getNumber(), articleSetRequest.getEan());
		if (errorCode != null) {
			return new ResponseEntity<>(new Response(errorCode.getCode(), errorCode.getMessage(), errorCode.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.updateArticleSet(articleSetId, articleSetRequest);
		return new ResponseEntity<>(
				new Response(StatusCode.ARTICLE_SET_UPDATED.getCode(), StatusCode.ARTICLE_SET_UPDATED.getMessage(),
						StatusCode.ARTICLE_SET_UPDATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PutMapping("/packet/{packetId}")
	public ResponseEntity<Response> updatePacket(@PathVariable Long packetId,
			@RequestBody PacketRequest packetRequest) {
		if (!articleService.findPacketById(packetId).isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.PACKET_NOT_EXIST.getCode(), ErrorCode.PACKET_NOT_EXIST.getMessage(),
							ErrorCode.PACKET_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.updatePacket(packetRequest, packetId);
		return new ResponseEntity<>(new Response(StatusCode.PACKET_UPDATED.getCode(),
				StatusCode.PACKET_UPDATED.getMessage(), StatusCode.PACKET_UPDATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PostMapping(value = "/articleSet")
	public ResponseEntity<Response> createArticleSet(@RequestBody ArticleSetRequest articleSetRequest) {
		ErrorCode errorCode = articleService.checkDuplicateArticleSetValues(articleSetRequest.getLongName(),
				articleSetRequest.getShortName(), articleSetRequest.getNumber(), articleSetRequest.getEan());
		if (errorCode != null) {
			return new ResponseEntity<>(new Response(errorCode.getCode(), errorCode.getMessage(), errorCode.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		articleSetRequest.getArticleWithQuantity().forEach(awq -> {
			if (!articleService.findArticleById(awq.getArticleId()).isPresent()) {
				ArticleExists = false;
			}
		});
		if (!ArticleExists) {
			ArticleExists = true;
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.createArticleSet(articleSetRequest);
		return new ResponseEntity<>(
				new Response(StatusCode.ARTICLE_SET_CREATED.getCode(), StatusCode.ARTICLE_SET_CREATED.getMessage(),
						StatusCode.ARTICLE_SET_CREATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PostMapping(value = "/packet")
	public ResponseEntity<Response> createPackage(@RequestBody PacketRequest packetRequest) {
		articleService.createPacket(packetRequest);
		return new ResponseEntity<>(new Response(StatusCode.PACKET_CREATED.getCode(),
				StatusCode.PACKET_CREATED.getMessage(), StatusCode.PACKET_CREATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/article/{articleId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> delete(@PathVariable Long articleId) {
		Optional<Article> optionalArticle = articleService.findArticleById(articleId);
		if (!optionalArticle.isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else if (!optionalArticle.get().getArticleSetRelations().isEmpty()) {
			// this code will be used to display associated set
			// List<Long> articleSetIds = new ArrayList<>();
			// optionalArticle.get().getArticleSetRelations().forEach(asr -> {
			// articleSetIds.add(asr.getArticleSet().getArticleSetId());
			// });
			optionalArticle.get().getArticleSetRelations().forEach(asr -> {
				if (asr.getArticleSet().getActive()) {
					ActiveArticleSetExists = true;
				}
			});
			if (ActiveArticleSetExists) {
				ActiveArticleSetExists = false;
				return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_ASSOCIATED_WITH_SET.getCode(),
						ErrorCode.ARTICLE_ASSOCIATED_WITH_SET.getMessage(),
						ErrorCode.ARTICLE_ASSOCIATED_WITH_SET.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
						HttpStatus.UNPROCESSABLE_ENTITY);
			}

		}
		articleService.deleteArticle(articleId);
		return new ResponseEntity<>(new Response(StatusCode.ARTICLE_DELETED.getCode(),
				StatusCode.ARTICLE_DELETED.getMessage(), StatusCode.ARTICLE_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/articleSet/{articleSetId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> deleteArticleSet(@PathVariable Long articleSetId) {
		if (!articleService.findArticleSetById(articleSetId).isPresent()) {
			return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_SET_NOT_EXIST.getCode(),
					ErrorCode.ARTICLE_SET_NOT_EXIST.getMessage(), ErrorCode.ARTICLE_SET_NOT_EXIST.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.deleteArticleSet(articleSetId);
		return new ResponseEntity<>(
				new Response(StatusCode.ARTICLE_SET_DELETED.getCode(), StatusCode.ARTICLE_SET_DELETED.getMessage(),
						StatusCode.ARTICLE_SET_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/packet/{packetId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> deletePacket(@PathVariable Long packetId) {
		if (!articleService.findPacketById(packetId).isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.PACKET_NOT_EXIST.getCode(), ErrorCode.PACKET_NOT_EXIST.getMessage(),
							ErrorCode.PACKET_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		articleService.deletePacket(packetId);
		return new ResponseEntity<>(new Response(StatusCode.PACKET_DELETED.getCode(),
				StatusCode.PACKET_DELETED.getMessage(), StatusCode.PACKET_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@GetMapping(value = "/packets/articleSet/{articleSetId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Packet>> packetsByArticleSetId(@PathVariable Long articleSetId) {
		return new ResponseEntity<>(articleService.packetsByArticleSetId(articleSetId), HttpStatus.OK);
	}

	@GetMapping(value = "/sellingPrice/{articleNumber}/{platformId}/{buyPrice}/")
	public BigDecimal getSellPrice(@PathVariable String articleNumber, @PathVariable BigDecimal buyPrice,
			@PathVariable Long platformId) {
		return priceService.getSellingPrice(articleNumber, buyPrice, platformId);
	}

	@GetMapping(value = "/salePrice/{articleNumber}/{actualStock}/{platformId}/{buyPrice}/")
	public BigDecimal getSalePrice(@PathVariable String articleNumber, @PathVariable int actualStock,
			@PathVariable BigDecimal buyPrice, @PathVariable Long platformId) {
		return priceService.getSalePrice(articleNumber, actualStock, buyPrice, platformId);
	}

	@GetMapping(value = "/articleSetBuyPrice/{articleSetId}")
	public BigDecimal getArticleSetBuyPrice(@PathVariable Long articleSetId) {
		return priceService.getArticleSetBuyPrice(articleSetId);
	}

	@GetMapping(value = "/articleSetTotalWeight/{articleSetId}")
	public Double getArticleSetTotalWeight(@PathVariable Long articleSetId) {
		return articleService.getTotalArticleSetWeight(articleSetId);
	}

	@GetMapping(value = "/articleTotalWeight/{articleId}")
	public Double getArticleTotalWeight(@PathVariable Long articleId) {
		return articleService.getTotalArticleWeight(articleId);
	}

	@DeleteMapping(value = "/barcode/{barcodeId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> deleteBarcode(@PathVariable Long barcodeId) {
		articleService.deleteBarcode(barcodeId);
		return new ResponseEntity<>(new Response(StatusCode.BARCODE_DELETED.getCode(),
				StatusCode.BARCODE_DELETED.getMessage(), StatusCode.BARCODE_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@GetMapping(value = "/verifyBarcode/{barcode}/{artNum}")
	public boolean verifyBarcode(@PathVariable String barcode, @PathVariable String artNum) {
		return articleService.verifyBarcode(barcode, artNum);
	}
}
