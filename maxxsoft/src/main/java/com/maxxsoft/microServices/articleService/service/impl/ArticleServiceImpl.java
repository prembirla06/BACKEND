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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.ArticleWithQuantity;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.model.PacketBarcode;
import com.maxxsoft.microServices.articleService.model.request.ArticlePacketCalculations;
import com.maxxsoft.microServices.articleService.model.request.ArticleRequest;
import com.maxxsoft.microServices.articleService.model.request.ArticleSetRequest;
import com.maxxsoft.microServices.articleService.model.request.ImageRequest;
import com.maxxsoft.microServices.articleService.model.request.PacketRequest;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.BarcodeRepository;
import com.maxxsoft.microServices.articleService.repository.PacketRepository;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductRequest;
import com.maxxsoft.microServices.magentoService.service.MagentoService;
import com.maxxsoft.microServices.orderService.model.response.OrderArticleResponse;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private PacketRepository packetRepository;

	@Autowired
	MagentoService magentoService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private BarcodeRepository barcodeRepository;

	Double packetweight;

	Double packetVolume;

	boolean duplicateFlag = false;
	boolean verified = false;

	@Override
	public Optional<Article> findArticleById(Long articleId) {
		return articleRepository.findById(articleId);
	}

	@Override
	public void createArticle(ArticleRequest articleRequest) {
		Article article = new Article();
		article.setShortName(articleRequest.getShortName());
		article.setLongName(articleRequest.getLongName());
		article.setShortDescription(articleRequest.getShortDescription());
		article.setLongDescription(articleRequest.getLongDescription());
		article.setBuyPrice(articleRequest.getBuyPrice());
		article.setNumber(articleRequest.getNumber());
		article.setSubstituteNumber(articleRequest.getSubstituteNumber());
		article.setPackageUnit(articleRequest.getPackageUnit());
		article.setTotalWeight(articleRequest.getTotalWeight());
		article.setEan(articleRequest.getEan());
		article.setStandalone(articleRequest.isStandalone());
		article.setStock(articleRequest.getStock());
		article.setPreOrder(articleRequest.getPreOrder());
		article.setDeliveryTime(articleRequest.getDeliveryTime());
		article.setDimension(articleRequest.getDimension());
		article.setEol(articleRequest.isEol());
		article.setHeight(articleRequest.getHeight());
		article.setWidth(articleRequest.getWidth());
		article.setDepth(articleRequest.getDepth());
		Set<ImageRequest> imageSet = articleRequest.getImages();
		imageSet.forEach(image -> {
			article.addImage(image);
		});
		try {
			articleRepository.save(article);
			erpArticleService.pushArticle(article);
		} catch (ConstraintViolationException ce) {
			throw ce;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void createPacket(PacketRequest packetRequest) {
		Packet packet = new Packet(packetRequest.getWeight(), packetRequest.getHeight(), packetRequest.getLenght(),
				packetRequest.getWidth(), packetRequest.getNumber(), packetRequest.getArticleId());
		packetRequest.getPacketBarcodeRequest().forEach(pbr -> {
			packet.addBarcode(pbr);
		});
		packetRepository.save(packet);
	}

	@Override
	public void createArticleSet(ArticleSetRequest articleSetRequest) {

		Set<ArticleWithQuantity> articleWithQuantitySet = articleSetRequest.getArticleWithQuantity();
		ArticleSet articleSet = new ArticleSet();
		articleSet.setShortName(articleSetRequest.getShortName());
		articleSet.setLongName(articleSetRequest.getLongName());
		articleSet.setNumber(articleSetRequest.getNumber());
		articleSet.setSubstituteNumber(articleSetRequest.getSubstituteNumber());
		articleSet.setEan(articleSetRequest.getEan());
		articleSet.setShortDescription(articleSetRequest.getShortDescription());
		articleSet.setLongDescription(articleSetRequest.getLongDescription());
		articleSet.setTotalArticles(articleSetRequest.getTotalArticles());
		articleSet.setStock(articleSetRequest.getStock());
		articleSet.setPreOrder(articleSetRequest.getPreOrder());
		articleSet.setDeliveryTime(articleSetRequest.getDeliveryTime());
		articleSet.setDimension(articleSetRequest.getDimension());
		articleSet.setHeight(articleSetRequest.getHeight());
		articleSet.setWidth(articleSetRequest.getWidth());
		articleSet.setDepth(articleSetRequest.getDepth());
		articleWithQuantitySet.forEach(articleWithQuantity -> {
			Article article = articleRepository.findById(articleWithQuantity.getArticleId()).get();
			articleSet.addArticle(article, articleWithQuantity.getQuantity());
		});

		Set<ImageRequest> imageSet = articleSetRequest.getImages();
		imageSet.forEach(image -> {
			articleSet.addImage(image);
		});

		// Hack to avoid Null Article Set ID AND Single Article ArticleSet
		if (articleWithQuantitySet.size() == 1) {
			articleWithQuantitySet.forEach(articleWithQuantity -> {
				Article article = articleRepository.findById(articleWithQuantity.getArticleId()).get();
				articleSet.addArticle1(article, articleWithQuantity.getQuantity());
			});
		}
		articleSet.getArticleSetRelations().forEach(asr -> {
			if (asr.getArticleSetId() == null) {
				asr.setArticleSetId(articleSet.getArticleSetId());
			}
		});
		// Hack ends

		articleSetRepository.save(articleSet);
		erpArticleService.pushArticleSet(articleSet);
	}

	@Override
	public void updateArticle(Long articleId, ArticleRequest articleRequest) {
		Optional<Article> articleOptional = articleRepository.findById(articleId);
		if (articleOptional.isPresent()) {
			Article article = articleOptional.get();
			article.setShortName(articleRequest.getShortName());
			article.setLongName(articleRequest.getLongName());
			article.setShortDescription(articleRequest.getShortDescription());
			article.setLongDescription(articleRequest.getLongDescription());
			article.setBuyPrice(articleRequest.getBuyPrice());
			article.setNumber(articleRequest.getNumber());
			article.setSubstituteNumber(articleRequest.getSubstituteNumber());
			article.setPackageUnit(articleRequest.getPackageUnit());
			article.setTotalWeight(articleRequest.getTotalWeight());
			article.setEan(articleRequest.getEan());
			article.setDimension(articleRequest.getDimension());
			article.setEol(articleRequest.isEol());
			article.setHeight(articleRequest.getHeight());
			article.setWidth(articleRequest.getWidth());
			article.setDepth(articleRequest.getDepth());
			articleRepository.saveAndFlush(article);
			erpArticleService.updateArticle(article);
		}
	}

	@Override
	public void updateArticleSet(Long articleSetId, ArticleSetRequest articleSetRequest) {
		Optional<ArticleSet> articleSetOptional = articleSetRepository.findById(articleSetId);
		if (articleSetOptional.isPresent()) {
			ArticleSet articleSet = articleSetOptional.get();
			Set<ArticleWithQuantity> articleWithQuantitySet = articleSetRequest.getArticleWithQuantity();
			articleSet.setShortName(articleSetRequest.getShortName());
			articleSet.setLongName(articleSetRequest.getLongName());
			articleSet.setNumber(articleSetRequest.getNumber());
			articleSet.setSubstituteNumber(articleSetRequest.getSubstituteNumber());
			articleSet.setEan(articleSetRequest.getEan());
			articleSet.setShortDescription(articleSetRequest.getShortDescription());
			articleSet.setLongDescription(articleSetRequest.getLongDescription());
			articleSet.setTotalArticles(articleSetRequest.getTotalArticles());
			articleSet.setDimension(articleSetRequest.getDimension());
			articleSet.setHeight(articleSetRequest.getHeight());
			articleSet.setWidth(articleSetRequest.getWidth());
			articleSet.setDepth(articleSetRequest.getDepth());
			// Set<ArticleSetRelation> articleSetRelations =
			// articleSet.getArticleSetRelations();
			// articleSetRelations.forEach(articleSetRelation -> {
			// // articleSet.removeArticle(ar);
			//
			// articleSetRelation.getArticle().getArticleSetRelations().remove(articleSetRelation);
			// articleSetRelation.setArticleSet(null);
			// articleSetRelation.setArticle(null);
			// });
			// articleSet.setArticleSetRelations(articleSetRelations);
			// articleWithQuantitySet.forEach(articleWithQuantity -> {
			// Article article =
			// articleRepository.findById(articleWithQuantity.getArticleId())
			// .orElseThrow(() -> new
			// ErrorException(ErrorCode.ARTICLE_NOT_EXIST));
			// articleSet.addArticle(article,
			// articleWithQuantity.getQuantity());
			// // articleSet.removeArticle(article);
			//
			// });
			articleSetRepository.saveAndFlush(articleSet);
			erpArticleService.updateArticleSet(articleSet);
		}
	}

	@Override
	public void deleteArticle(Long articleId) {
		Optional<Article> articleOptional = articleRepository.findById(articleId);

		if (articleOptional.isPresent()) {
			// articleRepository.delete(article.get());
			// Article Soft delete
			Article article = articleOptional.get();
			BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(), 1L);
			article.setActive(false);
			articleRepository.saveAndFlush(article);
			erpArticleService.deleteErpArticle(article.getNumber());
			// Disable product at magento
			deactivateOnMagento(article.getNumber(), sellingPrice);

		}
	}

	@Override
	public List<Article> articles() {
		return articleRepository.findAll();
	}

	@Override
	public List<ArticleSet> articleSets() {
		return articleSetRepository.findAll();
	}

	@Override
	public List<Packet> packets() {
		return packetRepository.findAll();
	}

	@Override
	public void updatePacket(PacketRequest packetRequest, Long packetId) throws ConstraintViolationException {

		Packet packet = packetRepository.getOne(packetId);

		packet.setNumber(packetRequest.getNumber());
		packet.setHeight(packetRequest.getHeight());
		packet.setLenght(packetRequest.getLenght());
		packet.setWeight(packetRequest.getWeight());
		packet.setWidth(packetRequest.getWidth());

		packetRequest.getPacketBarcodeRequest().forEach(pbr -> {
			PacketBarcode packetBarcode = new PacketBarcode(pbr.getBarcode(), packet);
			packet.getPacketBarcodes().add(packetBarcode);
		});

		packetRepository.saveAndFlush(packet);

	}

	@Override
	public void deleteArticleSet(Long articleSetId) {
		Optional<ArticleSet> articleSetOptional = articleSetRepository.findById(articleSetId);
		// Soft delete only
		if (articleSetOptional.isPresent()) {
			ArticleSet articleSet = articleSetOptional.get();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), setBuyPrice, 1L);
			// Used in hard delete
			// articleSetRepository.deleteAssociations(articleSetOptional.get());
			// articleSetRepository.delete(articleSetOptional.get());
			articleSet.setActive(false);
			articleSetRepository.saveAndFlush(articleSet);
			erpArticleService.deleteErpArticle(articleSet.getNumber());

			// Disable product at magento
			deactivateOnMagento(articleSet.getNumber(), sellingPrice);
		}
	}

	@Override
	public void deletePacket(Long packetId) {
		Optional<Packet> packet = packetRepository.findById(packetId);

		if (packet.isPresent()) {
			packetRepository.delete(packet.get());
		}

	}

	@Override
	public Optional<ArticleSet> findArticleSetById(Long articleSetId) {
		return articleSetRepository.findById(articleSetId);
	}

	@Override
	public Optional<Packet> findPacketById(Long packetId) {
		return packetRepository.findById(packetId);
	}

	@Override
	public List<Packet> packetsByArticleId(Long articleId) {
		return packetRepository.findByArticleId(articleId);
	}

	@Override
	public List<Article> getArticlesByArticleSet(Long articleSetId) {
		// TODO Auto-generated method stub
		return articleRepository.findAllAssociatedArticles(articleSetId);
	}

	@Override
	public List<Packet> packetsByArticleSetId(Long articleSetId) {
		List<Packet> packets = new ArrayList<Packet>();
		getArticlesByArticleSet(articleSetId).forEach(article -> {
			List<Packet> articlePackets = packetRepository.findByArticleId(article.getArticleId());
			if (articlePackets != null) {
				packets.addAll(articlePackets);
			}
		});
		return packets;
	}

	@Override
	public Double getTotalArticleSetWeight(Long articleSetId) {
		packetweight = 0.0;
		getArticlesByArticleSet(articleSetId).forEach(article -> {
			List<Packet> articlePackets = packetRepository.findByArticleId(article.getArticleId());
			if (articlePackets != null) {
				articlePackets.forEach(packet -> {
					packetweight = packetweight + packet.getWeight();
				});

			}
		});
		return Math.round(packetweight * 100.0) / 100.0;
	}

	@Override
	public Double getTotalArticleWeight(Long articleId) {
		packetweight = 0.0;

		List<Packet> articlePackets = packetRepository.findByArticleId(articleId);
		if (articlePackets != null) {
			articlePackets.forEach(packet -> {
				// System.out.println(packet.getWeight());
				packetweight = packetweight + packet.getWeight();
			});
		}
		return Math.round(packetweight * 100.0) / 100.0;
	}

	@Override
	public ArticlePacketCalculations getTotalArticleWeightAndVolume(Long articleId) {
		packetVolume = 0.0;
		packetweight = 0.0;

		List<Packet> articlePackets = packetRepository.findByArticleId(articleId);
		if (articlePackets != null) {
			articlePackets.forEach(packet -> {
				// System.out.println(packet.getWeight());
				packetVolume = packetVolume + packet.getHeight() * packet.getLenght() * packet.getWidth();
				packetweight = packetweight + packet.getWeight();
			});
		}
		ArticlePacketCalculations articlePacketCalculations = new ArticlePacketCalculations();
		articlePacketCalculations.setTotalWeight(Math.round(packetweight * 100.0) / 100.0);
		articlePacketCalculations.setTotalVolume(Math.round(packetVolume * 100.0) / 100.0);
		return articlePacketCalculations;
	}

	@Override
	public ArticlePacketCalculations getTotalArticleSetWeightAndVolume(Long articleSetId) {
		packetVolume = 0.0;
		packetweight = 0.0;
		getArticlesByArticleSet(articleSetId).forEach(article -> {
			List<Packet> articlePackets = packetRepository.findByArticleId(article.getArticleId());
			if (articlePackets != null) {
				articlePackets.forEach(packet -> {
					packetVolume = packetVolume + packet.getHeight() * packet.getLenght() * packet.getWidth();
					packetweight = packetweight + packet.getWeight();
				});

			}
		});
		ArticlePacketCalculations articlePacketCalculations = new ArticlePacketCalculations();
		articlePacketCalculations.setTotalWeight(Math.round(packetweight * 100.0) / 100.0);
		articlePacketCalculations.setTotalVolume(Math.round(packetVolume * 100.0) / 100.0);
		return articlePacketCalculations;
	}

	private void deactivateOnMagento(String sku, BigDecimal sellingPrice) {
		HttpHeaders headers = magentoService.getHeader();
		// Disable product at magento
		MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(sku, headers);
		MagentoProductList magentoProductList = new MagentoProductList();
		MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
		mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
		mogentoProductRequest.setSku(magentoProductResponse.getSku());
		mogentoProductRequest.setStatus(0);
		mogentoProductRequest.setPrice(sellingPrice);
		mogentoProductRequest.setVisibility(4);
		magentoProductList.setProduct(mogentoProductRequest);
		magentoService.updateProductBySku(sku, magentoProductList, headers);

	}

	@Override
	public ErrorCode checkDuplicateArticleValues(String longName, String shortName, String articleNumber, String ean) {
		if (articleNumber.contains(" ")) {
			return ErrorCode.ARTICLE_NUMBER_SPACE;
		}
		List<Article> articleList = articleRepository.findAll();
		Optional<Article> duplicateArticleNumber = articleList.stream()
				.filter(article -> article.getNumber().equals(articleNumber)).findFirst();
		if (duplicateArticleNumber.isPresent()) {
			return ErrorCode.DUPLICATE_ARTNUM;
		}
		Optional<Article> duplicateEan = articleList.stream().filter(article -> article.getEan().equals(ean))
				.findFirst();
		if (duplicateEan.isPresent()) {
			return ErrorCode.DUPLICATE_EAN;
		}
		Optional<Article> duplicateLongName = articleList.stream()
				.filter(article -> article.getLongName().equals(longName)).findFirst();
		if (duplicateLongName.isPresent()) {
			return ErrorCode.DUPLICATE_LONGNAME;
		}
		Optional<Article> duplicateShortName = articleList.stream()
				.filter(article -> article.getShortName().equals(shortName)).findFirst();
		if (duplicateShortName.isPresent()) {
			return ErrorCode.DUPLICATE_SHORTNAME;
		}
		return null;
	}

	@Override
	public ErrorCode checkDuplicateArticleValuesForUpdate(String longName, String shortName, String articleNumber,
			String ean) {
		List<Article> articleList = articleRepository.findAll();
		Optional<Article> duplicateArticleNumber = articleList.stream()
				.filter(article -> article.getNumber().equals(articleNumber)).findFirst();
		// System.out.println(articleList.size());
		if (duplicateArticleNumber.isPresent()) {
			articleList.remove(duplicateArticleNumber.get());
		}
		// System.out.println(articleList.size());
		Optional<Article> duplicateEan = articleList.stream().filter(article -> article.getEan().equals(ean))
				.findFirst();
		if (duplicateEan.isPresent()) {
			return ErrorCode.DUPLICATE_EAN;
		}
		Optional<Article> duplicateLongName = articleList.stream()
				.filter(article -> article.getLongName().equals(longName)).findFirst();
		if (duplicateLongName.isPresent()) {
			return ErrorCode.DUPLICATE_LONGNAME;
		}
		Optional<Article> duplicateShortName = articleList.stream()
				.filter(article -> article.getShortName().equals(shortName)).findFirst();
		if (duplicateShortName.isPresent()) {
			return ErrorCode.DUPLICATE_SHORTNAME;
		}
		return null;
	}

	@Override
	public ErrorCode checkDuplicateArticleSetValues(String longName, String shortName, String articleSetNumber,
			String ean) {
		if (articleSetNumber.contains(" ")) {
			return ErrorCode.ARTICLESET_NUMBER_SPACE;
		}
		List<ArticleSet> articleSetList = articleSetRepository.findAll();
		Optional<ArticleSet> duplicateArticleNumber = articleSetList.stream()
				.filter(articleSet -> articleSet.getNumber().equals(articleSetNumber)).findFirst();
		if (duplicateArticleNumber.isPresent()) {
			return ErrorCode.DUPLICATE_ARTNUM;
		}
		Optional<ArticleSet> duplicateEan = articleSetList.stream()
				.filter(articleSet -> articleSet.getEan().equals(ean)).findFirst();
		if (duplicateEan.isPresent()) {
			return ErrorCode.DUPLICATE_EAN;
		}
		Optional<ArticleSet> duplicateLongName = articleSetList.stream()
				.filter(articleSet -> articleSet.getLongName().equals(longName)).findFirst();
		if (duplicateLongName.isPresent()) {
			return ErrorCode.DUPLICATE_LONGNAME;
		}
		Optional<ArticleSet> duplicateShortName = articleSetList.stream()
				.filter(articleSet -> articleSet.getShortName().equals(shortName)).findFirst();
		if (duplicateShortName.isPresent()) {
			return ErrorCode.DUPLICATE_SHORTNAME;
		}
		return null;
	}

	@Override
	public ErrorCode checkDuplicateArticleSetValuesForUpdate(String longName, String shortName, String articleSetNumber,
			String ean) {

		List<ArticleSet> articleSetList = articleSetRepository.findAll();
		Optional<ArticleSet> duplicateArticleNumber = articleSetList.stream()
				.filter(articleSet -> articleSet.getNumber().equals(articleSetNumber)).findFirst();
		if (duplicateArticleNumber.isPresent()) {
			articleSetList.remove(duplicateArticleNumber.get());
		}
		Optional<ArticleSet> duplicateEan = articleSetList.stream()
				.filter(articleSet -> articleSet.getEan().equals(ean)).findFirst();
		if (duplicateEan.isPresent()) {
			return ErrorCode.DUPLICATE_EAN;
		}
		Optional<ArticleSet> duplicateLongName = articleSetList.stream()
				.filter(articleSet -> articleSet.getLongName().equals(longName)).findFirst();
		if (duplicateLongName.isPresent()) {
			return ErrorCode.DUPLICATE_LONGNAME;
		}
		Optional<ArticleSet> duplicateShortName = articleSetList.stream()
				.filter(articleSet -> articleSet.getShortName().equals(shortName)).findFirst();
		if (duplicateShortName.isPresent()) {
			return ErrorCode.DUPLICATE_SHORTNAME;
		}
		return null;
	}

	@Override
	public Optional<Article> findArticleNumber(String artNum) {
		return articleRepository.findByNumber(artNum);
	}

	@Override
	public int getStock(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			Article article = aritcleOptional.get();
			return article.getStock() - article.getPreOrder();
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				ArticleSet articleSet = aritcleSetOptional.get();
				return articleSet.getStock() - articleSet.getPreOrder();
			}
		}
		return 0;
	}

	@Override
	public double getWeightByArtNum(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			return getTotalArticleWeight(aritcleOptional.get().getArticleId());
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				return getTotalArticleSetWeight(aritcleSetOptional.get().getArticleSetId());
			}
		}
		return 0;
	}

	@Override
	public ArticlePacketCalculations getWeightAndVolumeByArtNum(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			return getTotalArticleWeightAndVolume(aritcleOptional.get().getArticleId());
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				return getTotalArticleSetWeightAndVolume(aritcleSetOptional.get().getArticleSetId());
			}
		}
		return null;
	}

	@Override
	public String getArticleNameByArtNum(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			return aritcleOptional.get().getShortName();
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				aritcleSetOptional.get().getShortName();
			}
		}
		return null;
	}

	@Override
	public OrderArticleResponse getArticleByArtNum(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			Article article = aritcleOptional.get();
			OrderArticleResponse orderArticleResponse = new OrderArticleResponse();
			orderArticleResponse.setArticleNumber(article.getNumber());
			orderArticleResponse.setShortName(article.getShortName());
			orderArticleResponse.setStock(article.getStock());
			orderArticleResponse.setSubstituteNumber(article.getSubstituteNumber());
			orderArticleResponse.setActualStock(article.getStock() - article.getPreOrder());
			orderArticleResponse.setDeliveryTime(article.getDeliveryTime());
			return orderArticleResponse;
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				ArticleSet article = aritcleSetOptional.get();
				OrderArticleResponse orderArticleResponse = new OrderArticleResponse();
				orderArticleResponse.setArticleNumber(article.getNumber());
				orderArticleResponse.setShortName(article.getShortName());
				orderArticleResponse.setStock(article.getStock());
				orderArticleResponse.setSubstituteNumber(article.getSubstituteNumber());
				orderArticleResponse.setActualStock(article.getStock() - article.getPreOrder());
				orderArticleResponse.setDeliveryTime(article.getDeliveryTime());
				return orderArticleResponse;
			}
		}
		return null;
	}

	@Override
	public List<Packet> getPacketByArtNum(String artNum) {
		Optional<Article> aritcleOptional = articleRepository.findByNumber(artNum);
		if (aritcleOptional.isPresent()) {
			return packetsByArticleId(aritcleOptional.get().getArticleId());
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(artNum);
			if (aritcleSetOptional.isPresent()) {
				return packetsByArticleSetId(aritcleSetOptional.get().getArticleSetId());
			}
		}
		return null;
	}

	@Override
	public void deleteBarcode(Long barcodeId) {
		barcodeRepository.deleteById(barcodeId);
	}

	@Override
	public boolean verifyBarcode(String barcode, String artNum) {
		verified = false;
		getPacketByArtNum(artNum).forEach(packet -> {
			packet.getPacketBarcodes().forEach(packetBarcode -> {
				if (packetBarcode.getBarcode().equals(barcode)) {
					verified = true;
				}
			});
		});
		return verified;
	}

}
