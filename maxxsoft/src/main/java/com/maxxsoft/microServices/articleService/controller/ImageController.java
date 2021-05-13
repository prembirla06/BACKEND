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

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.maxxsoft.microServices.articleService.model.ArticleImage;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.ArticleSetImage;
import com.maxxsoft.microServices.articleService.model.request.ImageRequest;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.ImageService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional
public class ImageController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ImageService imageService;

	@GetMapping(value = "/articleImage/{articleId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<ArticleImage>> getArticleImageByArticleId(@PathVariable Long articleId) {

		return new ResponseEntity<>(imageService.findArticleImageByArticleId(articleId), HttpStatus.OK);
	}

	@GetMapping(value = "/articleSetImage/{articleSetId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<ArticleSetImage>> getArticleSetImageByArticleSetId(@PathVariable Long articleSetId) {

		return new ResponseEntity<>(imageService.findArticleSetImageByArticleSetId(articleSetId), HttpStatus.OK);
	}

	@PostMapping(value = "/articleImage/{articleId}")
	public ResponseEntity<Response> addArticleImage(@PathVariable Long articleId,
			@RequestBody ImageRequest imageRequest) {
		if (!articleService.findArticleById(articleId).isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// System.out.println("Original Image Byte Size - " +
		// imageRequest.getPicByte().length);
		if (imageService.isOrderNumberExists(articleId, imageRequest.getOrder())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.IMAGE_ORDER_EXIST.getCode(), ErrorCode.IMAGE_ORDER_EXIST.getMessage(),
							ErrorCode.IMAGE_ORDER_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		imageService.addArticleImage(articleId, imageRequest);
		return new ResponseEntity<>(new Response(StatusCode.IMAGE_ADDED.getCode(), StatusCode.IMAGE_ADDED.getMessage(),
				StatusCode.IMAGE_ADDED.toString(), HttpStatus.OK.name()), HttpStatus.OK);
	}

	@PostMapping(value = "/articleSetImage/{articleSetId}")
	public ResponseEntity<Response> addArticleSetImage(@PathVariable Long articleSetId,
			@RequestBody ImageRequest imageRequest) {
		if (!articleService.findArticleSetById(articleSetId).isPresent()) {
			return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_SET_NOT_EXIST.getCode(),
					ErrorCode.ARTICLE_SET_NOT_EXIST.getMessage(), ErrorCode.ARTICLE_SET_NOT_EXIST.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (imageService.isOrderNumberExistsSet(articleSetId, imageRequest.getOrder())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.IMAGE_ORDER_EXIST.getCode(), ErrorCode.IMAGE_ORDER_EXIST.getMessage(),
							ErrorCode.IMAGE_ORDER_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		imageService.addArticleSetImage(articleSetId, imageRequest);
		return new ResponseEntity<>(new Response(StatusCode.IMAGE_ADDED.getCode(), StatusCode.IMAGE_ADDED.getMessage(),
				StatusCode.IMAGE_ADDED.toString(), HttpStatus.OK.name()), HttpStatus.OK);
	}

	@PutMapping("/article/{articleId}/articleImage/{articleImageId}")
	public ResponseEntity<Response> updateArticleImage(@PathVariable Long articleId, @PathVariable Long articleImageId,
			@RequestBody ImageRequest imageRequest) {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		if (!articleOptional.isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (imageService.isOrderNumberExists(articleId, imageRequest.getOrder())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.IMAGE_ORDER_EXIST.getCode(), ErrorCode.IMAGE_ORDER_EXIST.getMessage(),
							ErrorCode.IMAGE_ORDER_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		imageService.updateArticleImage(articleImageId, imageRequest, articleOptional.get().getNumber());
		return new ResponseEntity<>(
				new Response(StatusCode.ARTICLE_IMAGE_UPDATED.getCode(), StatusCode.ARTICLE_IMAGE_UPDATED.getMessage(),
						StatusCode.ARTICLE_IMAGE_UPDATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@PutMapping("/articleSet/{articleSetId}/articleSetImage/{articleSetImageId}")
	public ResponseEntity<Response> updateArticleSetImage(@PathVariable Long articleSetId,
			@PathVariable Long articleSetImageId, @RequestBody ImageRequest imageRequest) {
		Optional<ArticleSet> articleSetOptional = articleService.findArticleSetById(articleSetId);
		if (!articleSetOptional.isPresent()) {
			return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_SET_NOT_EXIST.getCode(),
					ErrorCode.ARTICLE_SET_NOT_EXIST.getMessage(), ErrorCode.ARTICLE_SET_NOT_EXIST.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (imageService.isOrderNumberExistsSet(articleSetId, imageRequest.getOrder())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.IMAGE_ORDER_EXIST.getCode(), ErrorCode.IMAGE_ORDER_EXIST.getMessage(),
							ErrorCode.IMAGE_ORDER_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		imageService.updateArticleSetImage(articleSetImageId, imageRequest, articleSetOptional.get().getNumber());
		return new ResponseEntity<>(new Response(StatusCode.ARTICLE_SET_IMAGE_UPDATED.getCode(),
				StatusCode.ARTICLE_SET_IMAGE_UPDATED.getMessage(), StatusCode.ARTICLE_SET_IMAGE_UPDATED.toString(),
				HttpStatus.OK.name()), HttpStatus.OK);
	}

	@DeleteMapping("/article/{articleId}/articleImage/{articleImageId}")
	public ResponseEntity<Response> deleteArticleImage(@PathVariable Long articleId,
			@PathVariable Long articleImageId) {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		if (!articleOptional.isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.ARTICLE_NOT_EXIST.getCode(), ErrorCode.ARTICLE_NOT_EXIST.getMessage(),
							ErrorCode.ARTICLE_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<ArticleImage> articleImage = imageService.findArticleImageById(articleImageId);
		if (articleImage.isPresent()) {
			articleOptional.get().removeImage(articleImage.get());
			imageService.deleteArticleImage(articleImage.get(), articleOptional.get().getNumber());
		}

		return new ResponseEntity<>(
				new Response(StatusCode.ARTICLE_IMAGE_DELETED.getCode(), StatusCode.ARTICLE_IMAGE_DELETED.getMessage(),
						StatusCode.ARTICLE_IMAGE_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

	@DeleteMapping("/articleSet/{articleSetId}/articleSetImage/{articleSetImageId}")
	public ResponseEntity<Response> deleteArticleSetImage(@PathVariable Long articleSetId,
			@PathVariable Long articleSetImageId) {
		Optional<ArticleSet> articleSetOptional = articleService.findArticleSetById(articleSetId);
		if (!articleSetOptional.isPresent()) {
			return new ResponseEntity<>(new Response(ErrorCode.ARTICLE_SET_NOT_EXIST.getCode(),
					ErrorCode.ARTICLE_SET_NOT_EXIST.getMessage(), ErrorCode.ARTICLE_SET_NOT_EXIST.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<ArticleSetImage> articleSetImage = imageService.findArticleSetImageById(articleSetImageId);
		if (articleSetImage.isPresent()) {
			articleSetOptional.get().removeImage(articleSetImage.get());
			imageService.deleteArticleSetImage(articleSetImage.get(), articleSetOptional.get().getNumber());
		}

		return new ResponseEntity<>(new Response(StatusCode.ARTICLE_SET_IMAGE_DELETED.getCode(),
				StatusCode.ARTICLE_SET_IMAGE_DELETED.getMessage(), StatusCode.ARTICLE_SET_IMAGE_DELETED.toString(),
				HttpStatus.OK.name()), HttpStatus.OK);
	}
}