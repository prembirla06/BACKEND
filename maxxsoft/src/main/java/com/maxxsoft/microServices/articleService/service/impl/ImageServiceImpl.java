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
package com.maxxsoft.microServices.articleService.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.maxxsoft.microServices.articleService.jobs.TEMPAmazonSynchronizationJob;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleImage;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.ArticleSetImage;
import com.maxxsoft.microServices.articleService.model.request.ImageRequest;
import com.maxxsoft.microServices.articleService.repository.ArticleImageRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetImageRepository;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoMediaResponse;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaContent;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaEntry;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaRequest;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

	Boolean isOrderNumberExists;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private ArticleImageRepository articleImageRepository;

	@Autowired
	private ArticleSetImageRepository articleSetImageRepository;

	@Autowired
	private MagentoService magentoService;

	@Override
	public void addArticleImage(Long articleId, ImageRequest imageRequest) {
		ArticleImage articleImage = new ArticleImage();
		Article article = articleService.findArticleById(articleId).get();
		articleImage.setArticle(article);
		articleImage.setName(imageRequest.getName());
		articleImage.setOrderNumber(imageRequest.getOrder());
		articleImage.setPicByte(imageRequest.getPicByte());
		articleImageRepository.save(articleImage);
		HttpHeaders headers = magentoService.getHeader();
		MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(article.getNumber(), headers);
		if (magentoProductResponse != null) {
			pushImageToMagento(articleImage, article.getShortName(), article.getNumber());
		} else {
			log.info("this article is not pushed to magento yet");
		}

	}

	@Override
	public void addArticleSetImage(Long articleSetId, ImageRequest imageRequest) {
		ArticleSetImage articleSetImage = new ArticleSetImage();
		ArticleSet articleSet = articleService.findArticleSetById(articleSetId).get();
		articleSetImage.setArticleSet(articleSet);
		articleSetImage.setName(imageRequest.getName());
		articleSetImage.setOrderNumber(imageRequest.getOrder());
		articleSetImage.setPicByte(imageRequest.getPicByte());
		articleSetImageRepository.save(articleSetImage);
		HttpHeaders headers = magentoService.getHeader();
		MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(articleSet.getNumber(),
				headers);
		if (magentoProductResponse != null) {
			pushImageToMagentoForSet(articleSetImage, articleSet.getShortName(), articleSet.getNumber());
		}
	}

	@Override
	public void updateArticleImage(Long articleImageId, ImageRequest imageRequest, String articleNumber) {
		Optional<ArticleImage> articleImageOptional = findArticleImageById(articleImageId);
		if (articleImageOptional.isPresent()) {

			ArticleImage articleImage = articleImageOptional.get();
			int existingOrderNumber = articleImage.getOrderNumber();
			articleImage.setName(imageRequest.getName());
			articleImage.setOrderNumber(imageRequest.getOrder());
			articleImageRepository.save(articleImage);
			updateToMagento(articleImage, existingOrderNumber, articleNumber);
		}
	}

	@Override
	public void updateArticleSetImage(Long articleSetImageId, ImageRequest imageRequest, String articleNumber) {
		Optional<ArticleSetImage> articleSetImageOptional = findArticleSetImageById(articleSetImageId);
		if (articleSetImageOptional.isPresent()) {
			ArticleSetImage articleSetImage = articleSetImageOptional.get();
			int existingOrderNumber = articleSetImage.getOrderNumber();
			articleSetImage.setName(imageRequest.getName());
			articleSetImage.setOrderNumber(imageRequest.getOrder());
			articleSetImageRepository.save(articleSetImage);
			updateToMagentoForSet(articleSetImage, existingOrderNumber, articleNumber);
		}
	}

	@Override
	public void deleteArticleImage(ArticleImage articleImage, String articleNumber) {

		articleImageRepository.delete(articleImage);
		deleteFromMagento(articleImage, articleNumber);
		if (articleImage.getOrderNumber() == 1) {
			deleteFromMagento(articleImage, articleNumber);
		}

	}

	@Override
	public void deleteArticleSetImage(ArticleSetImage articleSetImage, String articleNumber) {

		articleSetImageRepository.delete(articleSetImage);
		deleteFromMagentoForSet(articleSetImage, articleNumber);
		if (articleSetImage.getOrderNumber() == 1) {
			deleteFromMagentoForSet(articleSetImage, articleNumber);
		}
	}

	@Override
	public Optional<ArticleImage> findArticleImageById(Long articleImageId) {
		return articleImageRepository.findById(articleImageId);
	}

	@Override
	public Optional<ArticleSetImage> findArticleSetImageById(Long articleSetImageId) {
		return articleSetImageRepository.findById(articleSetImageId);
	}

	@Override
	public Set<ArticleImage> findArticleImageByArticleId(Long articleId) {

		return articleImageRepository.findByArticleArticleId(articleId);
	}

	@Override
	public Set<ArticleSetImage> findArticleSetImageByArticleSetId(Long articleSetId) {
		return articleSetImageRepository.findByArticleSetArticleSetId(articleSetId);
	}

	@Override
	public void pushImageToMagento(ArticleImage image, String articleShortName, String sku) {
		HttpHeaders headers = magentoService.getHeader();
		int position = image.getOrderNumber();
		ArrayList<String> types = new ArrayList<String>();
		if (position == 1) {
			types.add("image");
			types.add("small_image");
			types.add("thumbnail");
			types.add("swatch_image");
		}
		String shortNameWithoutSpecialChars = magentoService.urlKeyBuilder(articleShortName);
		if (shortNameWithoutSpecialChars.length() > 85) {
			shortNameWithoutSpecialChars = shortNameWithoutSpecialChars.substring(0, 85);
		}
		String imagetype;
		if (image.getName().contains(".png")) {
			imagetype = "image/png";
		} else {
			imagetype = "image/jpeg";

		}
		MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
				shortNameWithoutSpecialChars + position);
		MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(null, "image", image.getOrderNumber(), types,
				magentoMediaContent, shortNameWithoutSpecialChars + position, false);
		MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);
		String response = magentoService.createMedia(sku, magentoMediaRequest, headers);

		if (StringUtils.isEmpty(response)) {
			System.out.println("problem with aticle id------");
		} else {
			saveMagentoMediaLocal(response.substring(1, response.length() - 1), sku, headers);
		}

	}

	@Override
	public void pushImageToMagentoForSet(ArticleSetImage image, String articleShortName, String sku) {
		HttpHeaders headers = magentoService.getHeader();
		int position = image.getOrderNumber();
		String imageName;
		ArrayList<String> types = new ArrayList<String>();
		if (position == 1) {
			types.add("image");
			types.add("small_image");
			types.add("thumbnail");
			types.add("swatch_image");
		}
		String shortNameWithoutSpecialChars = magentoService.urlKeyBuilder(articleShortName);
		if (shortNameWithoutSpecialChars.length() > 85) {
			shortNameWithoutSpecialChars = shortNameWithoutSpecialChars.substring(0, 85);
		}
		String imagetype;
		if (image.getName().contains(".png")) {
			imagetype = "image/png";
		} else {
			imagetype = "image/jpeg";

		}
		MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
				shortNameWithoutSpecialChars + position);
		MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(null, "image", image.getOrderNumber(), types,
				magentoMediaContent, shortNameWithoutSpecialChars + position, false);
		MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);
		String response = magentoService.createMedia(sku, magentoMediaRequest, headers);

		if (StringUtils.isEmpty(response)) {
			System.out.println("problem with aticle id------");
		} else {
			saveMagentoMediaLocal(response.substring(1, response.length() - 1), sku, headers);
		}

	}

	private void updateToMagento(ArticleImage image, int oldOrder, String sku) {
		HttpHeaders headers = magentoService.getHeader();

		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (oldOrder == magentoMediaResponse.getPosition()) {
					int position = image.getOrderNumber();
					String imageName;
					ArrayList<String> types = new ArrayList<String>();
					if (position == 1) {
						types.add("image");
						types.add("small_image");
						types.add("thumbnail");
						types.add("swatch_image");
					}
					String imagetype;
					if (image.getName().contains(".png")) {
						imagetype = "image/png";
					} else {
						imagetype = "image/jpeg";

					}
					MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
							magentoMediaResponse.getLabel());
					MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(magentoMediaResponse.getId(), "image",
							position, types, magentoMediaContent, magentoMediaResponse.getLabel(), false);
					MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);
					String response = magentoService.updateMedia(sku, magentoMediaResponse.getId(), magentoMediaRequest,
							headers);

					if (StringUtils.isEmpty(response)) {
						System.out.println("problem with aticle id------");
					}

				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}

	}

	private void updateToMagentoForSet(ArticleSetImage image, int oldOrder, String sku) {
		HttpHeaders headers = magentoService.getHeader();

		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (oldOrder == magentoMediaResponse.getPosition()) {
					int position = image.getOrderNumber();
					System.out.println(position);
					String imageName;
					ArrayList<String> types = new ArrayList<String>();
					if (position == 1) {
						types.add("image");
						types.add("small_image");
						types.add("thumbnail");
						types.add("swatch_image");
					}
					String imagetype;
					if (image.getName().contains(".png")) {
						imagetype = "image/png";
					} else {
						imagetype = "image/jpeg";

					}
					MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
							magentoMediaResponse.getLabel());
					MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(magentoMediaResponse.getId(), "image",
							position, types, magentoMediaContent, magentoMediaResponse.getLabel(), false);
					MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);
					String response = magentoService.updateMedia(sku, magentoMediaResponse.getId(), magentoMediaRequest,
							headers);

					if (StringUtils.isEmpty(response)) {
						System.out.println("problem with aticle id------");
					}

				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}

	}

	private void deleteFromMagento(ArticleImage image, String sku) {
		HttpHeaders headers = magentoService.getHeader();

		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (image.getOrderNumber() == magentoMediaResponse.getPosition()) {
					System.out.println(magentoMediaResponse.getPosition());
					System.out.println(magentoMediaResponse.getId());
					magentoService.deleteMedia(sku, magentoMediaResponse.getId(), headers);
				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}
		deleteMagentoMediaLocal(image.getOrderNumber(), sku);
	}

	private void deleteFromMagentoForSet(ArticleSetImage image, String sku) {
		HttpHeaders headers = magentoService.getHeader();

		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (image.getOrderNumber() == magentoMediaResponse.getPosition()) {
					System.out.println(magentoMediaResponse.getPosition());
					System.out.println(magentoMediaResponse.getId());
					magentoService.deleteMedia(sku, magentoMediaResponse.getId(), headers);
				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}
		deleteMagentoMediaLocal(image.getOrderNumber(), sku);
	}

	private void saveMagentoMediaLocal(String response, String sku, HttpHeaders headers) {
		MagentoMediaResponse[] magentoMediaResponse = magentoService.getMedia(sku, headers);
		if (magentoMediaResponse != null) {
			Arrays.stream(magentoMediaResponse).forEach(magentoMediaResponseEntity -> {
				if (magentoMediaResponseEntity.getId().toString().equals(response)) {
					String types = Arrays.stream(magentoMediaResponseEntity.getTypes())
							.collect(Collectors.joining(","));
					MagentoMedia magentoMedia = new MagentoMedia(sku, magentoMediaResponseEntity.getId(),
							magentoMediaResponseEntity.getMedia_type(), magentoMediaResponseEntity.getLabel(),
							magentoMediaResponseEntity.getPosition(), types, magentoMediaResponseEntity.getFile());
					magentoMediaRepository.saveAndFlush(magentoMedia);
				}
			});

		}

	}

	private void deleteMagentoMediaLocal(int orderNumber, String sku) {
		magentoMediaRepository.findBySku(sku).forEach(image -> {
			if (image.getPosition() == orderNumber) {
				magentoMediaRepository.delete(image);
			}
		});
	}

	@Override
	public Boolean isOrderNumberExists(Long articleId, int order) {
		isOrderNumberExists = false;
		articleImageRepository.findByArticleArticleId(articleId).forEach(image -> {
			if (image.getOrderNumber() == order) {
				isOrderNumberExists = true;
			}
		});
		return isOrderNumberExists;
	}

	@Override
	public Boolean isOrderNumberExistsSet(Long articleSetId, int order) {
		isOrderNumberExists = false;
		articleSetImageRepository.findByArticleSetArticleSetId(articleSetId).forEach(image -> {
			if (image.getOrderNumber() == order) {
				isOrderNumberExists = true;
			}
		});
		return isOrderNumberExists;
	}

}
