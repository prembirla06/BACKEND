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
package com.maxxsoft.microServices.articleService.exportJob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.export.Idealo;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.model.MagentoProduct;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;
import com.maxxsoft.microServices.magentoService.repository.MagentoProductRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class IdealoExport {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	ErpArticleStueckRepository erpArticleStueckRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	PriceService priceService;
	@Autowired
	CommonExportConfig config;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private MagentoProductRepository magentoProductRepository;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;

	FTPClient ftp = null;
	byte pic[] = null;
	List<String> pictureURLs = new ArrayList<>();

	private static ArrayList<Idealo> idealoList = new ArrayList<Idealo>();

	public void runIdealoExport() {
		log.info("---Start Idealo Export");
		idealoList = new ArrayList<Idealo>();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				int actualStock = article.getStock() - article.getPreOrder();
				Idealo element = new Idealo();
				BigDecimal sellPrice = priceService.getSellingOrSalePrice(article.getNumber(), actualStock, article.getBuyPrice(), sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
				Artikel erpArticle = erpArticleService.getArtikelByNumber(article.getNumber()).get();
				Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
						.findBySku(article.getNumber());
				if (magentoProductOptional.isPresent()) {
					config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
				}
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(article.getNumber());
				pictureURLs = new ArrayList<>();
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(media.getFile());
					});
				}
				element.setSku(article.getNumber());
				element.setBrand("von moebel-guenstig24.de");
				element.setTitle(article.getShortName().replaceAll(",", " "));
				element.setCategoryPath(config.categoryPath.replace(",", " "));
				element.setUrl(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=idde");
				String desc_temp = article.getLongDescription().replace(",", " ");
				desc_temp = desc_temp.replace("|", " ");
				desc_temp = desc_temp.replace("\n", " ");
				desc_temp = desc_temp.replace("\r", " ");
				element.setDescription(desc_temp);
				element.setImageUrls(String.join(";", pictureURLs));
				element.setPrice(sellPrice.toString());
				int deliveryTime = article.getDeliveryTime();
				int deliveryTimeWorkdays = (BigDecimal.valueOf(deliveryTime).multiply(BigDecimal.valueOf(0.71))).intValue();
				element.setDeliveryTime("Lieferung in " + deliveryTimeWorkdays + " Werktagen");
				element.setCheckout("true");
				element.setFulfillmentType("Freight_Forwarder");
				element.setCheckoutLimitPerPeriod(String.valueOf(actualStock));
				if (article.getEan() != null && article.getEan().length() >= 2)
					element.setEans(article.getEan());
				else
					element.setEans("");
				element.setDeliveryCost_spedition("0.00");
				element.setPaymentCosts_cash_in_advance("0.00");
				element.setPaymentCosts_paypal("0.00");
				element.setSize(config.breite + " " + config.hoehe + " " + config.tiefe);
				element.setColour(config.farbe1.replace(",", " "));
				

				if (actualStock > 0)
					idealoList.add(element);
			}
		});
		
		articleSetRepository.findAll().forEach(articleSet -> {
			int actualStock = articleSet.getStock();
			Idealo element = new Idealo();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellPrice = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Magento").get().getSellingPlatformId());
			Artikel erpArticleSet = erpArticleService.getArtikelByNumber(articleSet.getNumber()).get();
			
			Optional<MagentoProduct> magentoProductOptional = magentoProductRepository
					.findBySku(articleSet.getNumber());
			if (magentoProductOptional.isPresent()) {
				config.setCustomAttributes(magentoProductOptional.get().getMagentoCustomAttributes());
			}
			List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(articleSet.getNumber());
			pictureURLs = new ArrayList<>();
			if (magentoMediaList.size() > 0) {
				magentoMediaList.forEach(media -> {
					pictureURLs.add(media.getFile());
				});
			}
			element.setSku(articleSet.getNumber());
			element.setBrand("von moebel-guenstig24.de");
			element.setTitle(articleSet.getShortName().replaceAll(",", " "));
			element.setCategoryPath(config.categoryPath.replace(",", " "));
			element.setUrl(config.urlWebsite + "/" + config.urlProduct + ".html" + "?utm_source=idde");
			String desc_temp = articleSet.getLongDescription().replace(",", " ");
			desc_temp = desc_temp.replace("|", " ");
			desc_temp = desc_temp.replace("\n", " ");
			desc_temp = desc_temp.replace("\r", " ");
			element.setDescription(desc_temp);
			element.setImageUrls(String.join(";", pictureURLs));
			element.setPrice(sellPrice.toString());
			int deliveryTime = articleSet.getDeliveryTime();
			int deliveryTimeWorkdays = (BigDecimal.valueOf(deliveryTime).multiply(BigDecimal.valueOf(0.71))).intValue();
			element.setDeliveryTime("Lieferung in " + deliveryTimeWorkdays + " Werktagen");
			element.setCheckout("true");
			element.setFulfillmentType("Freight_Forwarder");
			element.setCheckoutLimitPerPeriod(String.valueOf(actualStock));
			if (articleSet.getEan() != null && articleSet.getEan().length() >= 2)
				element.setEans(articleSet.getEan());
			else
				element.setEans("");
			element.setDeliveryCost_spedition("0.00");
			element.setPaymentCosts_cash_in_advance("0.00");
			element.setPaymentCosts_paypal("0.00");
			element.setSize(config.breite + " " + config.hoehe + " " + config.tiefe);
			element.setColour(config.farbe1.replace(",", " "));

			if (actualStock > 0)
				idealoList.add(element);
		
		});
		

		try {
			Map<String, Object> root = new HashMap<>();
			root.put("idealoList", idealoList);
			Configuration cfg = config.getConfiguration();

			Template idealoTemplate = cfg.getTemplate("idealoTemplate.csv");
			FileWriter writer = new FileWriter(config.outputDir + "idealo.csv", false);
			idealoTemplate.process(root, writer);
			writer.close();

			// FTP Upload:
			String localFile = config.outputDir + "idealo.csv";
			config.uploadSftpFromPath(localFile,"/var/www/moebel-guenstig24_de/pub/media/catalog/product/export/idealo.csv");

			log.info("Idealo productfile upload done!");
		} catch (Exception e) {
			log.error("PROBLEM BEI FTP UPLOADS! Idealo Export");
			e.printStackTrace();
		}
	}
}
