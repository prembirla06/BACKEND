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
package com.maxxsoft.microServices.articleService.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.microServices.articleService.model.ArticleImage;
import com.maxxsoft.microServices.articleService.model.ArticleSetImage;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoMediaResponse;
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
@Component
public class MagentoMediaMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private MagentoService magentoService;

	int count;

	// @Scheduled(fixedRate = 10000000)
	public void runMediaMigration() {
		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				System.out.println("article.getArticleId()------" + article.getArticleId());
				imageService.findArticleImageByArticleId(article.getArticleId()).forEach(image -> {

					deleteFromMagento(image, article.getNumber(), headers);
					if (image.getOrderNumber() == 1) {
						deleteFromMagento(image, article.getNumber(), headers);
						// System.out.println("Media deleted twice for position
						// 1...");
					}

					String shortNameWithoutSpecialChars = magentoService.urlKeyBuilder(article.getShortName());
					if (shortNameWithoutSpecialChars.length() > 85) {
						shortNameWithoutSpecialChars = shortNameWithoutSpecialChars.substring(0, 85);
					}
					int position = image.getOrderNumber();
					System.out.println(position);
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
					shortNameWithoutSpecialChars = shortNameWithoutSpecialChars + position;
					// String encodedString =
					// Base64.getEncoder().encodeToString(image.getPicByte());

					// System.out.println(encodedString);

					MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
							shortNameWithoutSpecialChars);
					MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(null, "image", image.getOrderNumber(),
							types, magentoMediaContent, shortNameWithoutSpecialChars, false);
					MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);

					// int maxLogSize = 50000;
					// for (int i = 0; i <=
					// magentoMediaRequest.toString().length() / maxLogSize;
					// i++) {
					// int start = i * maxLogSize;
					// int end = (i + 1) * maxLogSize;
					// end = end > magentoMediaRequest.toString().length() ?
					// magentoMediaRequest.toString().length()
					// : end;
					// log.info(magentoMediaRequest.toString().substring(start,
					// end));

					String response = magentoService.createMedia(article.getNumber(), magentoMediaRequest, headers);

					if (StringUtils.isEmpty(response)) {
						System.out.println(
								"problem with aticle id------------------------------------------$$$$$$$$$$$$$$$$$$$$$---------"
										+ article.getArticleId());
					}

				});

				MagentoMediaResponse[] magentoMediaResponse = magentoService.getMedia(article.getNumber(), headers);
				if (magentoMediaResponse.length > 0) {
					List<MagentoMedia> magentoMediaList = new ArrayList<>();
					Arrays.stream(magentoMediaResponse).forEach(magentoMediaResponseEntity -> {
						String types = Arrays.stream(magentoMediaResponseEntity.getTypes())
								.collect(Collectors.joining(","));
						MagentoMedia magentoMedia = new MagentoMedia(article.getNumber(),
								magentoMediaResponseEntity.getId(), magentoMediaResponseEntity.getMedia_type(),
								magentoMediaResponseEntity.getLabel(), magentoMediaResponseEntity.getPosition(), types,
								magentoMediaResponseEntity.getFile());
						magentoMediaList.add(magentoMedia);
					});
					magentoMediaRepository.saveAll(magentoMediaList);
				}
			}
		});
		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void runMediaMigrationForArticleSets() {
		HttpHeaders headers = magentoService.getHeader();
		System.out.println("start.....");
		articleSetRepository.findAll().forEach(articleSet -> {

			System.out.println("articleSet.getArticleSetId()---" + articleSet.getArticleSetId());
			// if (articleSet.getArticleSetId() > 142) {
			imageService.findArticleSetImageByArticleSetId(articleSet.getArticleSetId()).forEach(image -> {

				deleteFromMagentoForSet(image, articleSet.getNumber(), headers);
				if (image.getOrderNumber() == 1) {
					deleteFromMagentoForSet(image, articleSet.getNumber(), headers);
					// System.out.println("Media deleted twice for position
					// 1...");
				}

				String shortNameWithoutSpecialChars = articleSet.getShortName().replaceAll("[\"/\\:.()*]", "");
				int position = image.getOrderNumber();

				ArrayList<String> types = new ArrayList<String>();
				if (position == 1) {
					types.add("image");
					types.add("small_image");
					types.add("thumbnail");
					types.add("swatch_image");
				}
				System.out.println(position);
				if (shortNameWithoutSpecialChars.length() > 85) {
					shortNameWithoutSpecialChars = shortNameWithoutSpecialChars.substring(0, 85);
				}

				shortNameWithoutSpecialChars = shortNameWithoutSpecialChars + position;

				String imagetype;
				if (image.getName().contains(".png")) {
					imagetype = "image/png";
				} else {
					imagetype = "image/jpeg";

				}
				MagentoMediaContent magentoMediaContent = new MagentoMediaContent(image.getPicByte(), imagetype,
						magentoService.urlKeyBuilder(articleSet.getShortName()) + position);
				MagentoMediaEntry magentoMediaEntry = new MagentoMediaEntry(null, "image", position, types,
						magentoMediaContent, shortNameWithoutSpecialChars, false);
				MagentoMediaRequest magentoMediaRequest = new MagentoMediaRequest(magentoMediaEntry);

				String response = magentoService.createMedia(articleSet.getNumber(), magentoMediaRequest, headers);

				if (StringUtils.isEmpty(response)) {
					System.out.println(
							"problem with aticle id------------------------------------------$$$$$$$$$$$$$$$$$$$---------"
									+ articleSet.getArticleSetId());
				}
			});

			MagentoMediaResponse[] magentoMediaResponse = magentoService.getMedia(articleSet.getNumber(), headers);
			if (magentoMediaResponse != null) {
				List<MagentoMedia> magentoMediaList = new ArrayList<>();
				Arrays.stream(magentoMediaResponse).forEach(magentoMediaResponseEntity -> {
					String types = Arrays.stream(magentoMediaResponseEntity.getTypes())
							.collect(Collectors.joining(","));
					MagentoMedia magentoMedia = new MagentoMedia(articleSet.getNumber(),
							magentoMediaResponseEntity.getId(), magentoMediaResponseEntity.getMedia_type(),
							magentoMediaResponseEntity.getLabel(), magentoMediaResponseEntity.getPosition(), types,
							magentoMediaResponseEntity.getFile());
					magentoMediaList.add(magentoMedia);
				});
				magentoMediaRepository.saveAll(magentoMediaList);
			}
			// }
		});
		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void rundeleteMedia() {

		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone() && article.getArticleId() > 364 && article.getArticleId() < 371) {
				System.out.println("------Deleting Media for articleID------" + article.getArticleId());
				MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(article.getNumber(), headers);
				if (magentoMediaResponseList.length > 0) {
					Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
						magentoService.deleteMedia(article.getNumber(), magentoMediaResponse.getId(), headers);
					});

				} else {
					System.out.println("------Media does not exists------");
				}
			}
		});

		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void rundeleteMediaPosition1() {

		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				// System.out.println("------Deleting Media for article
				// ID------" + article.getArticleId());
				MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(article.getNumber(), headers);
				if (magentoMediaResponseList != null) {
					List<MagentoMediaResponse> magentoMediaResponseL = new ArrayList<>();
					Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
						if (magentoMediaResponse.getPosition() == 1) {
							magentoMediaResponseL.add(magentoMediaResponse);
						}
						// magentoService.deleteMedia(article.getNumber(),
						// magentoMediaResponse.getId(), headers);
					});
					if (magentoMediaResponseL.size() > 1) {
						magentoMediaResponseL.forEach(magentoMediaResponseR -> {
							if (magentoMediaResponseR.getTypes().length == 0) {
								System.out.println("deleting duplicate record for article.getNumber()== "
										+ article.getNumber() + "---id--" + article.getArticleId());
								magentoService.deleteMedia(article.getNumber(), magentoMediaResponseR.getId(), headers);
							}
						});
					}
				} else {
					System.out.println("------Media does not exists------");
				}
			}
		});

		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void rundelteMediaForArticleSet() {

		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleSetRepository.findAll().forEach(articleSet -> {
			// if (articleSet.getArticleSetId() < 16) {
			// System.out.println("------Deleting Media for article ID------" +
			// articleSet.getArticleSetId());
			MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(articleSet.getNumber(), headers);
			if (magentoMediaResponseList != null) {
				List<MagentoMediaResponse> magentoMediaResponseL = new ArrayList<>();
				Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
					if (magentoMediaResponse.getPosition() == 1) {
						magentoMediaResponseL.add(magentoMediaResponse);
					}
					// magentoService.deleteMedia(articleSet.getNumber(),
					// magentoMediaResponse.getId(), headers);
				});
				if (magentoMediaResponseL.size() > 1) {
					magentoMediaResponseL.forEach(magentoMediaResponseR -> {
						if (magentoMediaResponseR.getTypes().length == 0) {
							System.out.println("deleting duplicate record for article.getNumber()== "
									+ articleSet.getNumber() + "---id--" + articleSet.getArticleSetId());
							magentoService.deleteMedia(articleSet.getNumber(), magentoMediaResponseR.getId(), headers);
						}
					});
				}
			} else {
				System.out.println("------Media does not exists------");
			}
			// }
		});

		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void runMediaSave() {
		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {

				System.out.println("article.getArticleId()------" + article.getArticleId());

				MagentoMediaResponse[] magentoMediaResponse = magentoService.getMedia(article.getNumber(), headers);
				if (magentoMediaResponse != null) {
					List<MagentoMedia> magentoMediaList = new ArrayList<>();
					Arrays.stream(magentoMediaResponse).forEach(magentoMediaResponseEntity -> {
						String types = Arrays.stream(magentoMediaResponseEntity.getTypes())
								.collect(Collectors.joining(","));
						MagentoMedia magentoMedia = new MagentoMedia(article.getNumber(),
								magentoMediaResponseEntity.getId(), magentoMediaResponseEntity.getMedia_type(),
								magentoMediaResponseEntity.getLabel(), magentoMediaResponseEntity.getPosition(), types,
								magentoMediaResponseEntity.getFile());
						magentoMediaList.add(magentoMedia);
					});
					magentoMediaRepository.saveAll(magentoMediaList);
				}
			}
		});
		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void runMediaSetSave() {
		System.out.println("start.....");
		HttpHeaders headers = magentoService.getHeader();
		articleSetRepository.findAll().forEach(articleSet -> {

			System.out.println("articleSet.getArticleSetId()---" + articleSet.getArticleSetId());

			MagentoMediaResponse[] magentoMediaResponse = magentoService.getMedia(articleSet.getNumber(), headers);
			if (magentoMediaResponse != null) {
				List<MagentoMedia> magentoMediaList = new ArrayList<>();
				Arrays.stream(magentoMediaResponse).forEach(magentoMediaResponseEntity -> {
					String types = Arrays.stream(magentoMediaResponseEntity.getTypes())
							.collect(Collectors.joining(","));
					MagentoMedia magentoMedia = new MagentoMedia(articleSet.getNumber(),
							magentoMediaResponseEntity.getId(), magentoMediaResponseEntity.getMedia_type(),
							magentoMediaResponseEntity.getLabel(), magentoMediaResponseEntity.getPosition(), types,
							magentoMediaResponseEntity.getFile());
					magentoMediaList.add(magentoMedia);
				});
				magentoMediaRepository.saveAll(magentoMediaList);
			}

		});
		System.out.println("end.....");
	}

	private void deleteFromMagento(ArticleImage image, String sku, HttpHeaders headers) {
		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (image.getOrderNumber() == magentoMediaResponse.getPosition()) {
					// System.out.println(magentoMediaResponse.getPosition());
					// System.out.println(magentoMediaResponse.getId());
					magentoService.deleteMedia(sku, magentoMediaResponse.getId(), headers);
				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}

	}

	private void deleteFromMagentoForSet(ArticleSetImage image, String sku, HttpHeaders headers) {
		MagentoMediaResponse[] magentoMediaResponseList = magentoService.getMedia(sku, headers);
		if (magentoMediaResponseList != null) {

			Arrays.stream(magentoMediaResponseList).forEach(magentoMediaResponse -> {
				if (image.getOrderNumber() == magentoMediaResponse.getPosition()) {
					// System.out.println(magentoMediaResponse.getPosition());
					// System.out.println(magentoMediaResponse.getId());
					magentoService.deleteMedia(sku, magentoMediaResponse.getId(), headers);
				}
			});
		} else {
			System.out.println("------Media does not exists------");
		}

	}
}