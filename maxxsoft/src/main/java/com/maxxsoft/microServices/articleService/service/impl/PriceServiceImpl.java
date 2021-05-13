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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.PriceConfiguration;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;
import com.maxxsoft.microServices.articleService.repository.PacketRepository;
import com.maxxsoft.microServices.articleService.repository.PriceConfigRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.PriceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {

	@Autowired
	SellingPlatformRepository sellingPlatformRepository;
	@Autowired
	PriceConfigRepository priceConfigRepository;
	@Autowired
	CommonConfigRepository commonConfigRepository;
	@Autowired
	private ErpArticleService erpArticleService;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private ArticleSetRepository articleSetRepository;
	
	
	@Autowired
	private ArticleService articleService;
	
	private Double d;
	
	// shipping cost configuration TODO: move to configuration on frontend
	double maxWeightTour = 3500; // maximal weight loading capacity per truck in kg
	double maxVolumeTour = 35; // maximal volume loading capacity per truck in cbm
	double CostsPerTour = 1200; // cost per Tour in Euro

	// shipping costs extra fee
	int maxLength = 2400; // length without surcharge in mm
	double lengthSurcharge = 20; // if overlenth surcharge in Euro
	
	double fixPricePerPacket = 1; // handling price per paket in Euro
	double fixPricePerOrder = 5; // handling price per order in Euro
	
	// fullfillment costs
	double fixPriceFullfillmentPerKG = 0.17;
	double fixPriceFullfillmentPerCBM = 8;
	
	// helping variables, not moving into config
	double totalWeight;
	double totalVolume;
	boolean maxLengthReached; 
	int packetCount;

	@Override
	public BigDecimal getSellingPrice(String articleNumber, BigDecimal buyPrice, Long platformId) {
		Optional<PriceConfiguration> priceConfigurationOptional = priceConfigRepository
				.findBySellingPlatformId(platformId);
		if (priceConfigurationOptional.isPresent()) {
			PriceConfiguration priceConfiguration = priceConfigurationOptional.get();
			// Calculation: (Buyprice+ShippingAndFullfillment)/(1-ProfitMargin/100-MarketingCost/100-VAT/100)
			// Zähler
			BigDecimal numberator = buyPrice.add(getShippingCost(articleNumber)); 
			// Nenner
			BigDecimal denominator = BigDecimal.ONE.subtract(new BigDecimal(priceConfiguration.getProfitMarginPercentage() / 100.0)).subtract(new BigDecimal(priceConfiguration.getMarketingCostPercentage() / 100.0)).subtract(new BigDecimal(Integer.valueOf(commonConfigRepository.findByConfigKey("VAT_Percentage").getValue()) / 100.0));
			// Division
			BigDecimal sellPrice = numberator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
			
			BigInteger sellPriceInt = sellPrice.toBigInteger();
			sellPrice = new BigDecimal(sellPriceInt).add(new BigDecimal(0.90));
			sellPrice = sellPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			return sellPrice;
		} else {
			log.info("Configurations are not available for requested platform.");
			return null;
		}
	}

	@Override
	public BigDecimal getSalePrice(String articleNumber, int actualStock, BigDecimal buyPrice, Long platformId) {
		BigDecimal minimumSellingPrice = getMinimumSellingPrice(articleNumber, buyPrice, platformId);
		return getDirectSalePrice(articleNumber, actualStock, minimumSellingPrice);
	}
	
	@Override
	public BigDecimal getMinimumSellingPrice(String articleNumber, BigDecimal buyPrice, Long platformId) {
		Optional<PriceConfiguration> priceConfigurationOptional = priceConfigRepository
				.findBySellingPlatformId(platformId);
		if (priceConfigurationOptional.isPresent()) {		
			PriceConfiguration priceConfiguration = priceConfigurationOptional.get();
			// Calculation: (Buyprice+ShippingAndFullfillment)/(1-ProfitMargin/100-MarketingCost/100-VAT/100)
			// Zähler
			BigDecimal numberator = buyPrice.add(getShippingCost(articleNumber)); 
			// Nenner
			BigDecimal denominator = BigDecimal.ONE.subtract(new BigDecimal(priceConfiguration.getProfitMarginPercentage() / 100.0)).subtract(new BigDecimal(priceConfiguration.getMarketingCostPercentage() / 100.0)).subtract(new BigDecimal(Integer.valueOf(commonConfigRepository.findByConfigKey("VAT_Percentage").getValue()) / 100.0));
			// Division
			BigDecimal sellPrice = numberator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
			
			BigInteger sellPriceInt = sellPrice.toBigInteger();
			sellPrice = new BigDecimal(sellPriceInt).add(new BigDecimal(0.90));
			sellPrice = sellPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
			return sellPrice;
		} else {
			log.info("Configurations are not available for requested platform.");
			return null;
		}
	}
	
	@Override
	public BigDecimal getDirectSalePrice(String articleNumber, int actualStock, BigDecimal minimumSellingPrice) {

		Artikel erpArticle = erpArticleService.getArtikelByNumber(articleNumber).get();
		int orderLast30Days = erpArticleService.getOrderLastxDays(erpArticle.getRecId(), 30);
		int orderLast14Days = erpArticleService.getOrderLastxDays(erpArticle.getRecId(), 14);
		int orderLast7Days = erpArticleService.getOrderLastxDays(erpArticle.getRecId(), 7);

		if ((actualStock > 0 && orderLast30Days <= 0) || (actualStock > 3 && orderLast14Days <= 0)
				|| (actualStock > 10 && orderLast7Days <= 0)) {
			return minimumSellingPrice;
		}
		return null;

	}

	@Override
	public BigDecimal getShippingCost(String articleNumber) {

		List<Packet> packets = new ArrayList<Packet>();
		Optional<Article> aritcleOptional = articleRepository.findByNumber(articleNumber);
		if (aritcleOptional.isPresent()) {
			packets = articleService.packetsByArticleId(aritcleOptional.get().getArticleId());
		} else {
			Optional<ArticleSet> aritcleSetOptional = articleSetRepository.findByNumber(articleNumber);
			if (aritcleSetOptional.isPresent()) {
				packets = articleService.packetsByArticleSetId(aritcleSetOptional.get().getArticleSetId());
			}
		}
		
		
		BigDecimal shippingCost = null;
		
		totalWeight = 0;
		totalVolume = 0;
		packetCount = 0; 
		maxLengthReached = false;
		packets.forEach(packet -> {
			packetCount++;
			totalWeight = totalWeight + packet.getWeight();
			totalVolume = totalVolume + (packet.getLenght()/1000)*(packet.getWidth()/1000)*(packet.getHeight()/1000);
			
			if (packet.getLenght() > 2400 || packet.getWidth() > 2400 || packet.getHeight() > 2400) {
				maxLengthReached = true;
			}
		});
		
		// Shipping Cost Calculation:
		if (totalWeight > 0 && totalVolume > 0) {
			// new calculation method:
			double shippingPricePerKG = CostsPerTour/maxWeightTour;
			double shippingPricePerCBM = CostsPerTour/maxVolumeTour;
			
			double weightShippingPrice =  totalWeight*shippingPricePerKG;
			double volumeShippingPrice =  totalVolume*shippingPricePerCBM;
			
			shippingCost = BigDecimal.valueOf(Math.max(weightShippingPrice, volumeShippingPrice));
			
			double weightFullfillmentPrice =  totalWeight*fixPriceFullfillmentPerKG;
			double volumeFullfillmentPrice =  totalVolume*fixPriceFullfillmentPerCBM;
			BigDecimal fullfillmentCost = BigDecimal.valueOf(Math.max(weightFullfillmentPrice, volumeFullfillmentPrice));
			
			shippingCost = shippingCost.add(BigDecimal.valueOf(packetCount));
			shippingCost = shippingCost.add(BigDecimal.valueOf(fixPricePerOrder));
			shippingCost = shippingCost.add(fullfillmentCost);
			
			if (maxLengthReached) {
				shippingCost.add(BigDecimal.valueOf(lengthSurcharge));
			}
			
		} else {
			log.error("PriceServiceImpl.java: Packets of this product are missing, please add packets for this article: " + articleNumber);
			
			// old Calculation method:
			int shippingCostClass;
		String dimension = erpArticleService.getDimension(articleNumber);
		if (StringUtils.isEmpty(dimension)) {
			shippingCostClass = 0;
		} else {
			shippingCostClass = Integer.valueOf(dimension);
		}
		
		switch (shippingCostClass) {
		case 0:
			shippingCost = new BigDecimal(0);
			break;
		case 1:
			shippingCost = new BigDecimal(6.90);
			break;
		case 2:
			shippingCost = new BigDecimal(13.90);
			break;// Normal 11.90 Euro - Erhöht auf 14.90
		case 3:
			shippingCost = new BigDecimal(19.90);
			break;// Normal 16.90 Euro - Erhöht auf 19.90
		case 4:
			shippingCost = new BigDecimal(24.90);
			break;// Normal 21.90 Euro - Erhöht auf 24.90
		case 5:
			shippingCost = new BigDecimal(26.90);
			break;
		case 6:
			shippingCost = new BigDecimal(31.90);
			break;
		case 7:
			shippingCost = new BigDecimal(36.90);
			break;
		case 8:
			shippingCost = new BigDecimal(41.90);
			break;
		case 9:
			shippingCost = new BigDecimal(45.90);
			break;
		case 10:
			shippingCost = new BigDecimal(49.90);
			break;
		case 11:
			shippingCost = new BigDecimal(53.90);
			break;
		case 12:
			shippingCost = new BigDecimal(57.90);
			break;
		case 13:
			shippingCost = new BigDecimal(61.90);
			break;
		case 14:
			shippingCost = new BigDecimal(64.90);
			break;
		case 15:
			shippingCost = new BigDecimal(67.90);
			break;
		case 16:
			shippingCost = new BigDecimal(70.90);
			break;
		case 17:
			shippingCost = new BigDecimal(73.90);
			break;
		case 18:
			shippingCost = new BigDecimal(76.90);
			break;
		case 19:
			shippingCost = new BigDecimal(79.90);
			break;
		case 20:
			shippingCost = new BigDecimal(82.90);
			break;
		case 21:
			shippingCost = new BigDecimal(84.90);
			break;
		case 22:
			shippingCost = new BigDecimal(86.90);
			break;
		case 23:
			shippingCost = new BigDecimal(88.90);
			break;
		case 24:
			shippingCost = new BigDecimal(90.90);
			break;
		case 25:
			shippingCost = new BigDecimal(92.90);
			break;
		case 26:
			shippingCost = new BigDecimal(94.90);
			break;
		case 27:
			shippingCost = new BigDecimal(96.90);
			break;
		case 28:
			shippingCost = new BigDecimal(98.90);
			break;
		case 29:
			shippingCost = new BigDecimal(100.90);
			break;
		case 30:
			shippingCost = new BigDecimal(102.90);
			break;
		case 31:
			shippingCost = new BigDecimal(104.90);
			break;
		case 32:
			shippingCost = new BigDecimal(106.90);
			break;
		case 33:
			shippingCost = new BigDecimal(108.90);
			break;
		case 34:
			shippingCost = new BigDecimal(110.90);
			break;
		case 35:
			shippingCost = new BigDecimal(112.90);
			break;
		case 36:
			shippingCost = new BigDecimal(114.90);
			break;
		case 37:
			shippingCost = new BigDecimal(116.90);
			break;
		case 38:
			shippingCost = new BigDecimal(118.90);
			break;
		case 39:
			shippingCost = new BigDecimal(120.90);
			break;
		case 40:
			shippingCost = new BigDecimal(122.90);
			break;
		case 41:
			shippingCost = new BigDecimal(124.90);
			break;
		case 42:
			shippingCost = new BigDecimal(126.90);
			break;
		case 43:
			shippingCost = new BigDecimal(128.90);
			break;
		case 44:
			shippingCost = new BigDecimal(130.90);
			break;
		case 45:
			shippingCost = new BigDecimal(132.90);
			break;
		case 46:
			shippingCost = new BigDecimal(134.90);
			break;
		case 47:
			shippingCost = new BigDecimal(136.90);
			break;
		case 48:
			shippingCost = new BigDecimal(138.90);
			break;
		case 49:
			shippingCost = new BigDecimal(140.90);
			break;
		case 50:
			shippingCost = new BigDecimal(142.90);
			break;
		case 51:
			shippingCost = new BigDecimal(144.90);
			break;
		case 52:
			shippingCost = new BigDecimal(146.90);
			break;
		case 53:
			shippingCost = new BigDecimal(148.90);
			break;
		case 54:
			shippingCost = new BigDecimal(150.90);
			break;
		case 55:
			shippingCost = new BigDecimal(152.90);
			break;
		case 56:
			shippingCost = new BigDecimal(154.90);
			break;
		case 57:
			shippingCost = new BigDecimal(156.90);
			break;
		case 58:
			shippingCost = new BigDecimal(158.90);
			break;
		case 59:
			shippingCost = new BigDecimal(160.90);
			break;
		case 60:
			shippingCost = new BigDecimal(162.90);
			break;
		case 61:
			shippingCost = new BigDecimal(164.90);
			break;
		case 62:
			shippingCost = new BigDecimal(166.90);
			break;
		case 63:
			shippingCost = new BigDecimal(168.90);
			break;
		case 64:
			shippingCost = new BigDecimal(170.90);
			break;
		}
		shippingCost = shippingCost.setScale(2, BigDecimal.ROUND_HALF_UP);

		}

		return shippingCost;
		
	}

	@Override
	public BigDecimal getArticleSetBuyPrice(Long articleSetId) {
		d = 0.0;
		articleSetRepository.findById(articleSetId).get().getArticleSetRelations().forEach(asr -> {
			d = d + asr.getArticle().getBuyPrice().doubleValue() * asr.getQuantity();
		});
		return new BigDecimal(d);
	}

	@Override
	public BigDecimal getSellingOrSalePrice(String articleNumber, int actualStock, BigDecimal buyPrice,
			Long platformId) {
		if (this.getSalePrice(articleNumber, actualStock, buyPrice, platformId) == null) {
			return this.getSellingPrice(articleNumber, buyPrice, platformId);
		} else {
			return this.getSalePrice(articleNumber, actualStock, buyPrice, platformId);
		}
	}

}