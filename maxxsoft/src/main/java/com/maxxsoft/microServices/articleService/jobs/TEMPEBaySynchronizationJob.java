package com.maxxsoft.microServices.articleService.jobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.ApiLogging;
import com.ebay.sdk.SdkSoapException;
import com.ebay.sdk.call.AddFixedPriceItemCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.ReviseFixedPriceItemCall;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BuyerPaymentMethodCodeType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.DiscountPriceInfoType;
import com.ebay.soap.eBLBaseComponents.ErrorType;
import com.ebay.soap.eBLBaseComponents.FeesType;
import com.ebay.soap.eBLBaseComponents.GalleryTypeCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.ProductListingDetailsType;
import com.ebay.soap.eBLBaseComponents.ReturnPolicyType;
import com.ebay.soap.eBLBaseComponents.ReturnsWithinOptionsCodeType;
import com.ebay.soap.eBLBaseComponents.SalesTaxType;
import com.ebay.soap.eBLBaseComponents.ShippingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceOptionsType;
import com.ebay.soap.eBLBaseComponents.ShippingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;
import com.ebay.soap.eBLBaseComponents.VATDetailsType;
import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.exportJob.CommonExportConfig;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.SellingPlatformRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.MagentoMedia;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TEMPEBaySynchronizationJob {
	
	// Sandbox
	/*
	static String ApiServerUrl = "https://api.sandbox.ebay.com/wsapi"; // Production: "https://api.ebay.com/wsapi"
	static String EpsServerUrl = "http://api.sandbox.ebay.com/ws/api.dll"; // Production: "http://msa-e1.ebay.com/ws/eBayISAPI.dll?EpsBasicApp"
	static String DeveloperID = "S4298LFZZT1E1PE427HK9961B8F163"; // Produktion: "S4298LFZZT1E1PE427HK9961B8F163"
	static String ApplicationID = "HANSPETERS8361WSZ27YE8X2NL9862"; // Produktion: "HansPete-17e7-4380-810f-7652a6064ab2"
	static String CertificateID = "W8I226MJ5L1$49119COH9-6QU3KI1Y"; // Produktion: "7b114c00-c4dd-4f31-8640-fa7c2b65295e"
	static String eBayToken = "AgAAAA**AQAAAA**aAAAAA**3ae4Vg**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4CnCpaLogydj6x9nY+seQ**CqgAAA**AAMAAA**I2HC8peZ1sn+Fp2dxH7F41yOmCoVM9WRKhMDSWadbBnXWbd1MVfzdIcR0yxwcWXMeE/cc/sRfjx1qCUh7ZeXgcYlp8EFXamG63URTyQ7NNC6JFwyLyzkCHImTv27cFVykEwLx3EfVMArSb6MBV1DQunffbvBwR2YKwfYIicQUWa22mFPUQhY27GpEfuzxiudAMJbeF/83rkacqW+928HRnqeZuEnmzSoAj/Ku7YbGCyG59YOWs00+G3SxWnicZJc6hRUldoIzrk5efv3bMpKasWoHhbVBct2EujtObJj2rpbUvaBEl0MpNG38haB65jNJ93fn+xPpfZtYA1iMLlrNj5ytGRsT+eXBmhklmZrhA95OZaTu5a38uzl1f/Zkhk5cJZf5d4zm2UOAxU6eSNamZpgj33SvciQ+EmmsQT2HHbc6WAxBWnIruwOfWkFpylPN1aJioR7YE3kLaeuG+qWDZfyNB+JzILiqgTKOg3+V6xMpJH6pvl7n0Uej2F3IOOVpNPn2Pdm/dZpl2ClmEvylcY5S98uTcaRVvZc5+L9muJCiAjKMu7DRE52t6AyoGxM3rr7VdvihoP8e7p54cb+i5mdjohU4fUacwS/99a1LB7CsUhBDrNagSTIBQx3sgg5fPOmU9Y+sVsVdHK0mE0MtZWUSUzthoPa/u9CMVjhqBuh96geiAsiGThxnt9vReCx/PvZXlGavEKIItkfX4uam97DULN6JiRG/+2RyImflWHKmZjTWhpBExP0fBA1c0m9"; 
	static String paypalMail = "maxxeee1337@googlemail.com";
	*/
	
	// Produktiv
	
	static String ApiServerUrl = "https://api.ebay.com/wsapi";
	static String EpsServerUrl = "http://msa-e1.ebay.com/ws/eBayISAPI.dll?EpsBasicApp";
	static String DeveloperID = "S4298LFZZT1E1PE427HK9961B8F163";
	static String ApplicationID = "HansPete-17e7-4380-810f-7652a6064ab2";
	static String CertificateID = "7b114c00-c4dd-4f31-8640-fa7c2b65295e";
	//static String eBayToken = "AgAAAA**AQAAAA**aAAAAA**/O67WQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AEmYGmDJiLoQWdj6x9nY+seQ**67wAAA**AAMAAA**TTVv6b3BOyE1m2PBa4lbba3+8CcQYQZAwIIFZLVTY2ua+old5ooYaTBy62LWdA7m/5Y9ZNUnQvYAKvTb7dO/ROdhp8tWjiuXRtjEfL618jKw5IPYQcqCy7JhQEbJHA71F0A6iSZoMn+xe6c+ok1NgIldl97nmpu+AJiwIcksOBIsagx/nS82WPdYcZEVlfLoZaTsS1PK+AYE7zXsPUyrS++Vgu62czMcF6KiwfuWAjIBRgj4xKS2SrVP1f2jccX74nk4Ba/yPkgODh0F1CGs9npcmDeDH3IPfUbrr9FB1J1RoYYd+GmLtyzJxn48VKfCHUBdK+olr6A2ljPxOrbGVjXIeb750ZiqFrOC1AP3YUwxEM6WFror+9g+lIyxT+h4LU4mdMP59UEfUpYJALZpTTGxTp7nHpG25uuqLz7978URQpTC06O4vUQnEuaIUAx5jS/gP8rOjItX0JCjAFkiUPMtcUnoJUPGC56dGnKF1Zu3VkiNCNx9bHBBbNrPDXpcHxdqaahWVaS27+VGyM/E8FwtXWUvx5KV9kTgbSjvWU2uWB8uEy1n/AbYCU2HEZ7TABgwsv2M8WbTCSStCBpumD65zm7HuY4XX3cUJ2xlcaq9aRgrqjXnv1dufNs+DnUmCosf8CWVDJ8bgrY+w8HHe68kpF2e+M95ITFp1dbbyB59czwEctwCtHGm2Lklti0D2qt044c3k2TxU8OyiOCqg7oMJUgL8VmexTuqWHZukfAGk5nTAvXkqixFNsuE1GrU";
	static String eBayToken = "AgAAAA**AQAAAA**aAAAAA**qX6fWw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AEmYGmDJiLoQWdj6x9nY+seQ**67wAAA**AAMAAA**CIRVshDWqXJ+iDDPLhy3SUaC/pjoi0JtzAGZG0y7RKWHslYhqjAW20OQ36NOIa9mKmEYrK3OeZM3HInVnrTkOTQwtcsKUXWMUVVs+BwhTAbvAjfz+f3cqvemx4yZtsMqIQkSVLyOYJ1XD/T1IX0HzVef+YngrIMjyw7Sk8aK6uvzKui78x305Oy8gIub2ljBXpC7zr1Ef+TWeauXersfKBDPQ7rtzNL4dGZMY0E292FfJdXL4ACB2Am8Jj/Z0v3Q3GIJEoGPnhdQIRDJd/3JpUvviER/rIjDGpFK5m5dBOUTMDZkfPRH6tMVTxYhHU6l5M9edms7yIuw7vaU6SuoLtF/bB6LlFjsPW9hmF+viplP6Sf5q0WljqNAreffr7eFVItP0G+AoQk0Cwk8r/eMCKrddZoenKdXUEx4jp5B7UcjCh4139j8yjpy/0mLuciXY1oDaqinwZP16y2WUDJvUZxPIQMxucrSKTrBytopK1jbwN85V1UFOY9HeBWwGqDIZpoLWHNhw+7Eko+3RhyLmPYiTl7FqFKC1TcXwHp+fWrkvfRwwDCPlspolX3m5Ugs7Qopz04qClkOQe0rQx1b9MEnvowSgwFXo8gI0rGsDESuMowBvyPJuxwwOipZP13du/ZOVRTx8roGzoQaGT/FsHsN2LkYR5lDnf598HKJ+qtgTa6DOmRcvGAdzRbmaROD620gln/HtTSWdvbUYSQFSlWIkBFZqiLCqwXuTqZesQIEmGANqEg6LZ2RSMVeFR1D";
	static String paypalMail = "info@tecmaxx.de";
	
	static String DeveloperID2 = "1ca21a43-818b-4e4b-9ad3-5343499ec1e3";
	static String ApplicationID2 = "JochenUn-MaxxSoft-PRD-02f839347-a30efa80";
	static String CertificateID2 = "PRD-2f839347a4d8-5ffb-4ea2-9e6c-664c";
	static String eBayToken2 = "AgAAAA**AQAAAA**aAAAAA**CJggVw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wJkYumAZCApgudj6x9nY+seQ**90IDAA**AAMAAA**ttRfiRcxzp+sh730/Fl6TznQWwpWcJsbbgUyy5V+i5+bNpMLCJc38o1YllQoyys9o5DHQ3V74rHENPXILmt6l+Ss3CmplN2jbOm6TsCZ9zoh1AVk0w/R8HKVE/2srsfmiOy8GUO6Rw8QlNfZ2B9IP7bR+bfvIViKAvlsHQ19DNkP3M10nK+y1GGsKfuM6bLV7VJXcO/fGG0CG70gno6bpWvYzzVGEupB3g0AkyA6DrGQqES2pwbrpBAqYxeGAPWrScpZBzLou3DKoiancDZ5Z61YnpnY+tKz4pGzQclM85ruZ9MbqGl59i5r/CtO8B5I4ovthaUPz5FdlOCLxrqAkIPS1/SX0v+LXShAbV2dFkxfTP6qDqNLXKv72e9Zp6ijq7ZnIK/U1JAL5eUUVoH2YZLzfLn30WGLnkhVWiNWuydknuGMpZzN6D9FVL3w4x0N/AMcRRhJs+pdHB5HGoCEFGTBpxFD/QcEoCRCjAmnEvfx1+mnjmzJMOBJwf8i1a3dwAVAB39d1/xQdTTScNqzFGQ+yy7xMFdLul6SZNrOdftDS4748VBE1FqWN5YpqiqRovd33zOkiTcYwXfxJbiO9jKWPmuLB/3UfoRL1RyiAnh4HMD7sv1Hm1flO8RCLrdIQ836G6BR/NRcROSTRzMq+iGGp9dGCBmz5gAgey18WzkpKl3Pv5cCMPwcpr54TheohDdpv4RURrRbR58/jividY8p8qsB3jnndP5R0/YjEl/5ipytwLyAt6uc8C6qhy9j";
	
    
	static int lieferzeitKunde = 0;
	static String hauptBild = new String();
	
	
	private static String urlWebsite = "https://www.moebel-guenstig24.de";
	
	@Autowired
	private ArticleSetRepository articleSetRepository;
	
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleRepository erpArticleRepository;
	
	@Autowired
	private MagentoMediaRepository magentoMediaRepository;
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;
	
	@Autowired
	CommonExportConfig config;
	
	
	@Scheduled(cron = "${cronexpression.TEMPEBaySynchronizationJob}")
	public void runArticleTEMPEbaySynchonizationJob() {
		log.info("start..runArticleTEMPEbaySynchronizationJob...");
		// 1. EBay API Initialisieren (apiContext = ruebenfritz, apiContext2 = tecmaxx_de):
		ApiContext apiContext = getApiContext();
		ApiContext apiContext2 = getApiContext2();
		articleRepository.findAll().forEach(article -> {
			Artikel artikelCAO = erpArticleRepository.findByArtnum(article.getNumber()).get();
			if (article.isActive() && article.isStandalone() && artikelCAO.getUserfeld02() != null && !artikelCAO.getUserfeld02().equals("") && artikelCAO.getUserfeld03() != null && !artikelCAO.getUserfeld03().equals("")) {
				//System.out.println("Start: " + artikelCAO.getArtnum() + " " + artikelCAO.getKurzname());
				
				lieferzeitKunde = article.getDeliveryTime();
			 	BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(),
			 			sellingPlatformRepository.findByName("Ebay").get().getSellingPlatformId());
			 	BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(article.getNumber(),
						article.getBuyPrice(), sellingPlatformRepository.findByName("Ebay").get().getSellingPlatformId());
				
				boolean reviseMode = false;
				ebayData ebaydata = new ebayData();
				ebaydata = ebaydata.parseEBayData(artikelCAO);
				
				if (ebaydata != null) {
					switch (ebaydata.getArtnum()) {
					case "0":
						//System.out.println("REVISE MODE OFF (tecmaxx_de), Kategorien ausgelesen");
						//System.out.println("Kat1 = " + ebaydata.getKat1());
						//System.out.println("Kat2 = " + ebaydata.getKat2());
						reviseMode = false;
						ebaydata.setArtnum(addItemToEbay(config, minimumSellingPrice, sellingPrice, article.getShortDescription(), article.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext, ebaydata.getArtnum(), ebaydata.getKat1(), ebaydata.getKat2(), reviseMode));
						break;
					case ("x"):
						//System.out.println("Do Nothing (tecmaxx_de)");
						break;
					default:
						//System.out.println("REVISE MODE ON (tecmaxx_de)");
						//System.out.println("Kat1 = " + ebaydata.getKat1());
						//System.out.println("Kat2 = " + ebaydata.getKat2());
						reviseMode = true;
						ebaydata.setArtnum(addItemToEbay(config, minimumSellingPrice, sellingPrice, article.getShortDescription(), article.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext, ebaydata.getArtnum(), ebaydata.getKat1(), ebaydata.getKat2(), reviseMode));
					}
					
					switch (ebaydata.getArtnum2()) {
					case "0":
						//System.out.println("REVISE MODE OFF (ruebenfritz), Kategorien ausgelesen");
						//System.out.println("Kat3 = " + ebaydata.getKat3());
						//System.out.println("Kat4 = " + ebaydata.getKat4());	
						reviseMode = false;
						ebaydata.setArtnum2(addItemToEbay(config, minimumSellingPrice, sellingPrice, article.getShortDescription(), article.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext2, ebaydata.getArtnum2(), ebaydata.getKat3(), ebaydata.getKat4(), reviseMode));
						break;
					case ("x"):
						//System.out.println("Do Nothing (ruebenfritz)");
						break;
					default:
						//System.out.println("REVISE MODE ON (ruebenfritz)");
						//System.out.println("Kat3 = " + ebaydata.getKat3());
						//System.out.println("Kat4 = " + ebaydata.getKat4());
						reviseMode = true;
						ebaydata.setArtnum2(addItemToEbay(config, minimumSellingPrice, sellingPrice, article.getShortDescription(), article.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext2, ebaydata.getArtnum2(), ebaydata.getKat3(), ebaydata.getKat4(), reviseMode));
					
					}
				
					if (artikelCAO.getUserfeld02().contains("/"))
						artikelCAO.setUserfeld02(ebaydata.getEBayCAOString() + artikelCAO.getUserfeld02().substring(artikelCAO.getUserfeld02().indexOf("/"), artikelCAO.getUserfeld02().length()));
					else
					artikelCAO.setUserfeld02(ebaydata.getEBayCAOString());
					erpArticleRepository.save(artikelCAO);
				}
				
			}
			
			
		});
		log.info("end..runArticleTEMPEbaySynchronizationJob...");
		this.runArticleSetTEMPEbaySynchonizationJob();
	}
	
	
	
	// called by previous method:
	public void runArticleSetTEMPEbaySynchonizationJob() {
		log.info("start..runArticleSetTEMPEbaySynchronizationJob...");
		// 1. EBay API Initialisieren (apiContext = ruebenfritz, apiContext2 = tecmaxx_de):
		ApiContext apiContext = getApiContext();
		ApiContext apiContext2 = getApiContext2();
		articleSetRepository.findAll().forEach(articleSet -> {
			
			//if (articleSet.getNumber().equals("58-812-13+58-816-13")) {
			
			Artikel artikelCAO = erpArticleRepository.findByArtnum(articleSet.getNumber()).get();
			if (artikelCAO.getUserfeld02() != null && !artikelCAO.getUserfeld02().equals("") && artikelCAO.getUserfeld03() != null && !artikelCAO.getUserfeld03().equals("")) {
				//System.out.println("Start: " + artikelCAO.getArtnum() + " " + artikelCAO.getKurzname());
				
				lieferzeitKunde = articleSet.getDeliveryTime();
				BigDecimal buyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
				BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), buyPrice, sellingPlatformRepository.findByName("Ebay").get().getSellingPlatformId());
				BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(articleSet.getNumber(), buyPrice,
						sellingPlatformRepository.findByName("Ebay").get().getSellingPlatformId());
				
				boolean reviseMode = false;
				ebayData ebaydata = new ebayData();
				ebaydata = ebaydata.parseEBayData(artikelCAO);
				
				if (ebaydata != null) {
					switch (ebaydata.getArtnum()) {
					case "0":
						//System.out.println("REVISE MODE OFF (tecmaxx_de), Kategorien ausgelesen");
						//System.out.println("Kat1 = " + ebaydata.getKat1());
						//System.out.println("Kat2 = " + ebaydata.getKat2());
						reviseMode = false;
						ebaydata.setArtnum(addItemToEbay(config, minimumSellingPrice, sellingPrice, articleSet.getShortDescription(), articleSet.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext, ebaydata.getArtnum(), ebaydata.getKat1(), ebaydata.getKat2(), reviseMode));
						break;
					case ("x"):
						//System.out.println("Do Nothing (tecmaxx_de)");
						break;
					default:
						//System.out.println("REVISE MODE ON (tecmaxx_de)");
						//System.out.println("Kat1 = " + ebaydata.getKat1());
						//System.out.println("Kat2 = " + ebaydata.getKat2());
						reviseMode = true;
						ebaydata.setArtnum(addItemToEbay(config, minimumSellingPrice, sellingPrice, articleSet.getShortDescription(), articleSet.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext, ebaydata.getArtnum(), ebaydata.getKat1(), ebaydata.getKat2(), reviseMode));
					}
					
					switch (ebaydata.getArtnum2()) {
					case "0":
						//System.out.println("REVISE MODE OFF (ruebenfritz), Kategorien ausgelesen");
						//System.out.println("Kat3 = " + ebaydata.getKat3());
						//System.out.println("Kat4 = " + ebaydata.getKat4());	
						reviseMode = false;
						ebaydata.setArtnum2(addItemToEbay(config, minimumSellingPrice, sellingPrice, articleSet.getShortDescription(), articleSet.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext2, ebaydata.getArtnum2(), ebaydata.getKat3(), ebaydata.getKat4(), reviseMode));
						break;
					case ("x"):
						//System.out.println("Do Nothing (ruebenfritz)");
						break;
					default:
						//System.out.println("REVISE MODE ON (ruebenfritz)");
						//System.out.println("Kat3 = " + ebaydata.getKat3());
						//System.out.println("Kat4 = " + ebaydata.getKat4());
						reviseMode = true;
						ebaydata.setArtnum2(addItemToEbay(config, minimumSellingPrice, sellingPrice, articleSet.getShortDescription(), articleSet.getLongDescription(), magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum()), artikelCAO, apiContext2, ebaydata.getArtnum2(), ebaydata.getKat3(), ebaydata.getKat4(), reviseMode));
					
					}
				
					if (artikelCAO.getUserfeld02().contains("/"))
						artikelCAO.setUserfeld02(ebaydata.getEBayCAOString() + artikelCAO.getUserfeld02().substring(artikelCAO.getUserfeld02().indexOf("/"), artikelCAO.getUserfeld02().length()));
					else
					artikelCAO.setUserfeld02(ebaydata.getEBayCAOString());
					erpArticleRepository.save(artikelCAO);
				}
				
			//}
			
			}
			
			
			
		});
		log.info("end..runArticleSetTEMPEbaySynchronizationJob...");
	}
	



private static String addItemToEbay(CommonExportConfig config, BigDecimal minimumSellingPrice, BigDecimal sellingPrice, String short_description, String long_description, List<MagentoMedia> magentoMediaList, Artikel artikelCAO, ApiContext apiContext, String artnum, String kat1, String kat2, boolean reviseMode) {
	
	ItemType item = null;	
	
	try {	
	 if (reviseMode) {
		 ItemType oldItem = null;
		 item = new ItemType();
		 GetItemCall api = new GetItemCall(apiContext);
		 oldItem = api.getItem(artnum);
		 item.setItemID(oldItem.getItemID());	
	 } else {
		 item = new ItemType();
		 
		 // TODO: Kategorie 1 abfragen, Kategorie 2 abfragen
		 //item
	 }
		 
	 
	if (apiContext.getApiCredential().getApiAccount().getDeveloper().equals("1ca21a43-818b-4e4b-9ad3-5343499ec1e3") && artikelCAO.getUserfeld04() != null && !artikelCAO.getUserfeld04().equals(""))
	item.setTitle(artikelCAO.getUserfeld04());
	else item.setTitle(artikelCAO.getKasName());

	// BILDER HOLEN
	List<String> pictureURLs = new ArrayList<String>();
	if (magentoMediaList.size() > 0) {
		magentoMediaList.forEach(media -> {
			pictureURLs.add(config.mediaURL + media.getFile());
		});
	}
	
	
	
		item.setDescription(getEBayDescription(config, artikelCAO, short_description, long_description, pictureURLs));	
		
		item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		item.setCurrency(CurrencyCodeType.EUR);
		//item.setListingDuration(ListingDurationCodeType.GTC.value());
		item.setListingDuration(ListingDurationCodeType.GTC.value());
		item.setLocation("Treuchtlingen");
		item.setCountry(CountryCodeType.DE);
		//if (artikelCAO.getWarengruppe() != 80190) {
		if (artikelCAO.getMengeMin().intValue() > 20) // normal 15
		item.setQuantity(2);
		else item.setQuantity(1);
		
		// Umsatzsteuer
		VATDetailsType vat = new VATDetailsType();
		vat.setVATPercent(Float.valueOf(16));
		item.setVATDetails(vat);
		
		
		
		StringTokenizer tokenizerUserfeld08 = new StringTokenizer(artikelCAO.getUserfeld08(), ";,");
		String produktweltString = tokenizerUserfeld08.nextToken();
		
		String kategorie = tokenizerUserfeld08.nextToken();

		String unterkategorie = tokenizerUserfeld08.nextToken();
		
		String wohnwelt = tokenizerUserfeld08.nextToken();
		if (wohnwelt.equals("x")) { 
			wohnwelt = "";
		}
		String stilwelt = tokenizerUserfeld08.nextToken();
		if (stilwelt.equals("x")) { 
			stilwelt = "";
		}
		String farbe1 = tokenizerUserfeld08.nextToken();
		if (farbe1.equals("x")) { 
			farbe1 = "";
		}
		String farbe2 = tokenizerUserfeld08.nextToken();
		if (farbe2.equals("x")) { 
			farbe2 = "";
		}
		String trendfarbe = tokenizerUserfeld08.nextToken();
		if (trendfarbe.equals("x")) { 
			trendfarbe = "";
		}
		String breite = tokenizerUserfeld08.nextToken();
		if (breite.equals("x")) { 
			breite = "";
		}
		String hoehe = tokenizerUserfeld08.nextToken();
		if (hoehe.equals("x")) { 
			hoehe = "";
		}
		String tiefe = tokenizerUserfeld08.nextToken();
		if (tiefe.equals("x")) { 
			tiefe = "";
		}
		String besonderheit = tokenizerUserfeld08.nextToken();
		if (besonderheit.equals("x")) { 
			besonderheit = "";
		}
		
		
		// Add Item Specifics
        NameValueListArrayType itemSpec = new NameValueListArrayType();
        ArrayList<NameValueListType> nvlArray = new ArrayList<NameValueListType>();
        
       
        // FESTE ATTRIBUTE:
        NameValueListType nv1 = new NameValueListType();
        nv1.setName("Marke");
        nv1.setValue(new String[]{"von moebel-guenstig24.de"});
        nvlArray.add(nv1);
        
        NameValueListType nv2 = new NameValueListType();
        nv2.setName("Herstellernummer");
        nv2.setValue(new String[]{artikelCAO.getBarcode()});
        nvlArray.add(nv2);
        
        if (!artikelCAO.getUserfeld03().contains("Breite")) {
        	NameValueListType nv3 = new NameValueListType();
            nv3.setName("Breite");
            nv3.setValue(new String[]{breite});
            nvlArray.add(nv3);
        }
        
        
        if (!artikelCAO.getUserfeld03().contains("Tiefe")) {
        NameValueListType nv4 = new NameValueListType();
        nv4.setName("Tiefe");
        nv4.setValue(new String[]{tiefe});
        nvlArray.add(nv4);
        }
        
        
        if (!artikelCAO.getUserfeld03().contains("Höhe")) {
        NameValueListType nv5 = new NameValueListType();
        nv5.setName("Höhe");
        nv5.setValue(new String[]{hoehe});
        nvlArray.add(nv5);
        }
        
        /*NameValueListType nv3 = new NameValueListType();
        nv3.setName("EAN");
        nv3.setValue(new String[]{artikelCAO.getBarcode()});
        nvlArray.add(nv3);
        
        NameValueListType nv4 = new NameValueListType();
        nv4.setName("Size (Men's)");
        nv4.setValue(new String[]{"M"});*/
        
        // AUSGELESENE ATTRIBUTE:
        String allAttributeString = artikelCAO.getUserfeld03();
        StringTokenizer tokenzierAllAttributeString = new StringTokenizer (allAttributeString, ";"); 
        while (tokenzierAllAttributeString.hasMoreElements()) {
        	String attributeString = tokenzierAllAttributeString.nextToken();
        	StringTokenizer tokenzierAttributeString = new StringTokenizer (attributeString, ":"); 
        	NameValueListType nvTemp = new NameValueListType();
        	nvTemp.setName(tokenzierAttributeString.nextToken());
        	String valueString = tokenzierAttributeString.nextToken();
        	if (valueString.contains(",")) {
        		nvTemp.setValue(valueString.split(","));
        	} else {
        		nvTemp.setValue(new String[]{valueString});
        	}
        	nvlArray.add(nvTemp);
        	}
        
        
        itemSpec.setNameValueList(nvlArray.toArray(new NameValueListType[nvlArray.size()]));

        item.setItemSpecifics(itemSpec);
        
        // EAN Code hinzufügen:
        ProductListingDetailsType product = new ProductListingDetailsType();
        //product.setEAN(artikelCAO.getBarcode());
        product.setEAN("Nicht zutreffend");
        
        item.setProductListingDetails(product);
		  
        CategoryType cat1 = new CategoryType();
        String catID1 = kat1;
        cat1.setCategoryID(catID1);
        item.setPrimaryCategory(cat1);
        

		
		if (kat2 != null && !kat2.equals("") && !kat2.equals("0")) {
		CategoryType cat2 = new CategoryType();
		String catID2 = kat2;
		cat2.setCategoryID(catID2);
		item.setSecondaryCategory(cat2);
		}
		
		
		// item condition, New
		item.setConditionID(1000);
		
		
		// BILDER
		PictureDetailsType pdt = new PictureDetailsType();
		//pictureURLs[0] = "http://www.moebel-guenstig24.de/media/catalog/product" + hauptBild;
		if (pictureURLs.size() <= 5)
		pdt.setPictureURL(pictureURLs.toArray(new String[0]));
		else 
			pdt.setPictureURL(pictureURLs.subList(0, 5).toArray(new String[0]));
		//pdt.setGalleryURL("http://www.moebel-guenstig24.de/media/catalog/product"  + hauptBild);
		pdt.setGalleryType(GalleryTypeCodeType.GALLERY);
		item.setPictureDetails(pdt);
		
		
		// price
		AmountType startPrice = new AmountType();
		startPrice.setCurrencyID(CurrencyCodeType.EUR);
	 	startPrice.setValue(sellingPrice.doubleValue());
		
		try {
		// Pr�fe Konkurrenzpreise:
		if (artikelCAO.getUserfeld02().contains("/") && !artikelCAO.getUserfeld02().contains("/x")) {
				String konkurrenzArtNums = artikelCAO.getUserfeld02().substring(artikelCAO.getUserfeld02().indexOf("/")+1, artikelCAO.getUserfeld02().length());
				StringTokenizer tokenzierKonkurrenzArtNum = new StringTokenizer(konkurrenzArtNums, ";,");
				double guenstigerKonkurrenzPreisInklVersand = 0;
				while (tokenzierKonkurrenzArtNum.hasMoreElements()) {
					String konkurrenzArtNum = tokenzierKonkurrenzArtNum.nextToken();
					GetItemCall getitemcall = new GetItemCall(apiContext);
					//getitemcall.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
					getitemcall.setItemID(konkurrenzArtNum);
					ItemType resultItem = getitemcall.getItem();
					//System.out.println(resultItem.getStartPrice().getValue());
					//System.out.println(resultItem.getShippingDetails().getShippingServiceOptions()[0].getShippingServiceCost().getValue());
					double konkurrenzPreisInklVersand = resultItem.getStartPrice().getValue() + resultItem.getShippingDetails().getShippingServiceOptions()[0].getShippingServiceCost().getValue();
					
					if (guenstigerKonkurrenzPreisInklVersand == 0 || guenstigerKonkurrenzPreisInklVersand >= konkurrenzPreisInklVersand) {
						guenstigerKonkurrenzPreisInklVersand = konkurrenzPreisInklVersand;
					}
					
					}
				if (sellingPrice.doubleValue() > guenstigerKonkurrenzPreisInklVersand) {
					
					if (minimumSellingPrice.doubleValue() < guenstigerKonkurrenzPreisInklVersand)
					startPrice.setValue(guenstigerKonkurrenzPreisInklVersand - 0.10);
					else
					System.out.println("Konkurrenzpreis ist zu niedrig, prüfe Konkurenzangebote!");
					
				}
					
					
		}
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			System.out.println("Fail to read competition price. " + artikelCAO.getKurzname());
			System.out.println("Fehler beim Konkurrenzvergleich!" + e.getMessage());
			if( e instanceof SdkSoapException ){
                 SdkSoapException sdkExe = (SdkSoapException)e;
                ErrorType errs = sdkExe.getErrorType();
                System.out.println("SdkSoapException error code " +errs.getErrorCode()+ " / error shot message: " + errs.getShortMessage());
            }
            if (e instanceof ApiException ){
                ApiException apiExe = (ApiException)e;
                ErrorType[] errs = apiExe.getErrors();
                for (int j=0; j<errs.length; j++){
                    System.out.println("ApiException error code " +errs[j].getErrorCode()+ "error shot message" + errs[j].getShortMessage());
                }
            }        
		}
		

			item.setStartPrice(startPrice);
 		
		
		
		ReturnPolicyType retPol = new ReturnPolicyType();
		retPol.setReturnsAcceptedOption("ReturnsAccepted");
	
		retPol.setDescription("Widerrufsbelehrung\r\n" + 
				"\r\n" + 
				"WIDERRUFSRECHT\r\n" + 
				"\r\n" + 
				"Sie haben das Recht, binnen einem Monat ohne Angabe von Gründen diesen Vertrag zu widerrufen.\r\n" + 
				"Die Widerrufsfrist beträgt vierzehn Tage ab dem Tag, an dem Sie oder ein von Ihnen benannter Dritter, der nicht der Beförderer ist, die letzte Ware in Besitz genommen haben bzw. hat.\r\n" + 
				"Um Ihr Widerrufsrecht auszuüben, müssen Sie uns \r\n" + 
				"(TecMaXX GmbH,\r\n" + 
				"Auf der Sünd 18,\r\n" + 
				"91757 Treuchtlingen,\r\n" + 
				"E-Mail: info@moebel-guenstig24.de,\r\n" + 
				"Fax: 09142 / 200267)\r\n" + 
				"\r\n" + 
				"mittels einer eindeutigen Erklärung (z. B. ein mit der Post versandter Brief, Telefax oder E-Mail) über Ihren Entschluss, diesen Vertrag zu widerrufen, informieren. Sie können dafür das beigefügte Muster-Widerrufsformular verwenden, das jedoch nicht vorgeschrieben ist. Zur Wahrung der Widerrufsfrist reicht es aus, dass Sie die Mitteilung über die Ausübung des Widerrufsrechts vor Ablauf der Widerrufsfrist absenden.\r\n" + 
				"\r\n" + 
				" \r\n" + 
				"\r\n" + 
				"FOLGEN DES WIDERRUFS\r\n" + 
				"\r\n" + 
				"Wenn Sie diesen Vertrag widerrufen, haben wir Ihnen alle Zahlungen, die wir von Ihnen erhalten haben, einschließlich der Lieferkosten (mit Ausnahme der zusätzlichen Kosten, die sich daraus ergeben, dass Sie eine andere Art der Lieferung als die von uns angebotene, günstigste Standardlieferung gewählt haben), unverzüglich und spätestens binnen vierzehn Tagen ab dem Tag zurückzuzahlen, an dem die Mitteilung über Ihren Widerruf dieses Vertrags bei uns eingegangen ist. Für diese Rückzahlung verwenden wir dasselbe Zahlungsmittel, das Sie bei der ursprünglichen Transaktion eingesetzt haben, es sei denn, mit Ihnen wurde ausdrücklich etwas anderes vereinbart; in keinem Fall werden Ihnen wegen dieser Rückzahlung Entgelte berechnet. Wir können die Rückzahlung verweigern, bis wir die Waren wieder zurückerhalten haben oder bis Sie den Nachweis erbracht haben, dass Sie die Waren zurückgesandt haben, je nachdem, welches der frühere Zeitpunkt ist.\r\n" + 
				"\r\n" + 
				"Sie haben die Waren unverzüglich und in jedem Fall spätestens binnen vierzehn Tagen  ab dem Tag, an dem Sie uns über den Widerruf dieses Vertrags unterrichten, an uns \r\n" + 
				"(TecMaXX GmbH,\r\n" + 
				"Auf der Sünd 18,\r\n" + 
				"91757 Treuchtlingen,\r\n" + 
				"E-Mail: info@moebel-guenstig24.de,\r\n" + 
				"Fax: 09142 / 200267)\r\n" + 
				"zurückzusenden oder zu übergeben. Die Frist ist gewahrt, wenn Sie die Waren vor Ablauf der Frist von vierzehn Tagen absenden.\r\n" + 
				"\r\n" + 
				"Sie tragen die unmittelbaren Kosten der Rücksendung der Waren. Die unmittelbaren Kosten der Rücksendung werden hinsichtlich solcher Waren, die aufgrund ihrer Beschaffenheit nicht normal mit der Post oder Paketdiensten an uns zurückgesandt werden können (Speditionsware), auf höchstens 99 Euro geschätzt.\r\n" + 
				"\r\n" + 
				"Sie müssen für einen etwaigen Wertverlust der Waren nur aufkommen, wenn dieser Wertverlust auf einen zur Prüfung der Beschaffenheit, Eigenschaften und Funktionsweise der Waren nicht notwendigen Umgang mit ihnen zurückzuführen ist.\r\n" + 
				"\r\n" + 
				"AUSSCHLUSS- UND ERLÖSCHENSGRÜNDE:\r\n" + 
				"\r\n" + 
				"DAS WIDERRUFSRECHT BESTEHT NICHT BEI  FOLGENDEN VERTRÄGEN:\r\n" + 
				"\r\n" + 
				"Verträge zur Lieferung von Waren, die nicht vorgefertigt sind und für deren Herstellung eine individuelle Auswahl oder Bestimmung durch den Verbraucher maßgeblich ist oder die eindeutig auf die persönlichen Bedürfnisse des Verbrauchers zugeschnitten sind;\r\n" + 
				"Verträge zur Lieferung von Waren, die schnell verderben können oder deren Verfallsdatum schnell überschritten würde;\r\n" + 
				"Verträge zur Lieferung versiegelter Waren, die aus Gründen des Gesundheitsschutzes oder der Hygiene (wie z.B.: Zahnbürsten, Rasierklingen, etc) nicht zur Rückgabe geeignet sind, wenn ihre Versiegelung nach der Lieferung entfernt wurde,\r\n" + 
				"Verträge zur Lieferung von Waren, wenn diese nach der Lieferung aufgrund ihrer Beschaffenheit untrennbar mit anderen Gütern vermischt wurden,\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"DAS WIDERRUFSRECHT ERLISCHT VORZEITIG BEI VERTRÄGEN:\r\n" + 
				"\r\n" + 
				"zur Lieferung versiegelter Waren, die aus Gründen des Gesundheitsschutzes oder der Hygiene nicht zur Rückgabe geeignet sind, wenn ihre Versiegelung nach der Lieferung entfernt wurde;\r\n" + 
				"zur Lieferung von Waren, wenn diese nach der Lieferung aufgrund ihrer Beschaffenheit untrennbar mit anderen Gütern vermischt wurden;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"ALLGEMEINE HINWEISE:\r\n" + 
				"\r\n" + 
				"1. Bitte vermeiden Sie Beschädigungen und Verunreinigungen der Ware.\r\n" + 
				"\r\n" + 
				"2. Senden Sie die Ware möglichst in Originalverpackung mit sämtlichem Zubehör und mit allen Verpackungsbestandteilen an uns zurück. Sorgen Sie bitte mit einer sachgemäßen und transportgeeigneten Verpackung für einen ausreichenden Schutz vor Transportschäden.\r\n" + 
				"\r\n" + 
				"3. Senden Sie die Ware bitte nicht unfrei an uns zurück.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"MUSTER-WIDERRUFSFORMULAR\r\n" + 
				"\r\n" + 
				"(WENN SIE DEN VERTRAG WIDERRUFEN WOLLEN, DANN FÜLLEN SIE BITTE DIESES FORMULAR AUS UND SENDEN SIE ES ZURÜCK.)\r\n" + 
				"\r\n" + 
				"AN: \r\n" + 
				"TecMaXX GmbH / moebel-guenstig24.de\r\n" + 
				"Auf der Sünd 18\r\n" + 
				"91757 Treuchtlingen\r\n" + 
				"FAX: 09142 200267\r\n" + 
				"\r\n" + 
				"HIERMIT WIDERRUFE(N) ICH/WIR (*) DEN VON MIR/UNS (*) ABGESCHLOSSENEN VERTRAG ÜBER DEN KAUF DER FOLGENDEN WAREN (*)\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"BESTELLT AM (*)/ERHALTEN AM (*)\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"NAME DES/DER VERBRAUCHER(S)\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"ANSCHRIFT DES/DER VERBRAUCHER(S)\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"UNTERSCHRIFT DES/DER VERBRAUCHER(S) (NUR BEI MITTEILUNG AUF PAPIER)\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"DATUM\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"(*) UNZUTREFFENDES STREICHEN. \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"DIESES MUSTER-WIDERRUFSFORMULAR STELLEN WIR IHNEN ALS ZUSÄTZLICHE ANLAGE ZU IHRER BESTELLBESTÄTIGUNG NOCHMALS GESONDERT ZUR VERFÜGUNG, UM IHNEN EINE EINFACHE MÖGLICHKEIT DES AUSDRUCKS DIESES FORMULARS („DRUCKVERSION“) ZU BIETEN.");
		
		retPol.setReturnsWithinOption(ReturnsWithinOptionsCodeType.MONTHS_1.value());
		item.setReturnPolicy(retPol);
		
		// Paypal und Zahlungsarten
		//item.setPayPalEmailAddress(paypalMail);
		//item.setPaymentMethods(new BuyerPaymentMethodCodeType[] {BuyerPaymentMethodCodeType.PAY_PAL, BuyerPaymentMethodCodeType.MONEY_XFER_ACCEPTED, BuyerPaymentMethodCodeType.CASH_ON_PICKUP});
		
		// Shipping
		item.setShippingDetails(getShippingDetails());
		
		int lieferzeitWerktage = lieferzeitKunde - (lieferzeitKunde / 7 * 2);
		// Bearbeitungszeit
		if (lieferzeitWerktage > 30)
			item.setDispatchTimeMax(40);
		else if (lieferzeitWerktage > 7 && lieferzeitWerktage < 10) 
			item.setDispatchTimeMax(10);
			else if (lieferzeitWerktage > 10 && lieferzeitWerktage < 15) 
				item.setDispatchTimeMax(15);	
			else if (lieferzeitWerktage > 15 && lieferzeitWerktage < 20) 
				item.setDispatchTimeMax(20);
			else if (lieferzeitWerktage > 20 && lieferzeitWerktage < 30) 
				item.setDispatchTimeMax(30);
			else if (lieferzeitWerktage > 30 && lieferzeitWerktage < 40) 
				item.setDispatchTimeMax(40);
			else 
		item.setDispatchTimeMax(lieferzeitWerktage);
		
		// Bestandseinheit setzen (Artikelnummer)
		item.setSKU(artikelCAO.getArtnum());
	 
	 
	 
	 //item = buildItem(artikelCAO, item);
	
	// 3. Erzeuge ein Object und f�hre dein Einstellversuch aus
	FeesType fees= null;
	if (reviseMode) {
		ReviseFixedPriceItemCall rev = new ReviseFixedPriceItemCall(apiContext);
		rev.setItemToBeRevised(item);
		fees = rev.reviseFixedPriceItem();
	} else {
		AddFixedPriceItemCall api = new AddFixedPriceItemCall(apiContext);
		api.setItem(item);
		fees = api.addFixedPriceItem();
	}

	//System.out.println("End to call eBay API, show call result ...");
	//System.out.println();
	
	
	// 4. Zeige Ergebnis
	//System.out.println("The list was listed successfully! Revisemode: " + reviseMode);

	double listingFee = eBayUtil.findFeeByName(fees.getFee(),
			"ListingFee").getFee().getValue();
	//System.out.println("Listing fee is: "
	//		+ new Double(listingFee).toString());
	//System.out.println("Listed Item ID: " + item.getItemID());
	return item.getItemID();

	} catch (Exception e) {
		System.out.println("Fail to list the item. " + artikelCAO.getKurzname());
		System.out.println("Fehler beim EBay-Listing!" + e.getMessage());
		System.out.println(e.getCause());
		
		e.printStackTrace();
		
		try
		{
		    String filename= config.outputDir + "logfile.txt";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write(artikelCAO.getArtnum() + " " + artikelCAO.getKurzname()+ "\n\n" + e.getMessage() + "\n\n\n\n\n"); //appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
		
		
		
		return artnum;
	}
}



private static ApiContext getApiContext() {
	
	// set devId, appId, certId in ApiAccount
	ApiAccount account = new ApiAccount();
	account.setDeveloper(DeveloperID);
	account.setApplication(ApplicationID);
	account.setCertificate(CertificateID);
	
	// set ApiAccount and token in ApiCredential
	ApiCredential credential = new ApiCredential();
	credential.setApiAccount(account);
	credential.seteBayToken(eBayToken);
	
	// add ApiCredential to ApiContext
	ApiContext context = new ApiContext();
	context.setApiCredential(credential);
	
	// set eBay server URL to call
	context.setApiServerUrl(ApiServerUrl);
	
	context.setSite(SiteCodeType.GERMANY);
	
	
	// set timeout in milliseconds - 3 minutes
	context.setTimeout(300000);
	
	// set wsdl version number
	context.setWSDLVersion("423");
	
	// turn on logging
	ApiLogging logging = new ApiLogging();
	logging.setLogHTTPHeaders(true);
	logging.setLogSOAPMessages(true);
	logging.setLogExceptions(true);
	context.setApiLogging(logging);
	
	return context;
	
}

private static ApiContext getApiContext2() {
	
	// set devId, appId, certId in ApiAccount
	ApiAccount account = new ApiAccount();
	account.setDeveloper(DeveloperID2);
	account.setApplication(ApplicationID2);
	account.setCertificate(CertificateID2);
	
	// set ApiAccount and token in ApiCredential
	ApiCredential credential = new ApiCredential();
	credential.setApiAccount(account);
	credential.seteBayToken(eBayToken2);
	
	// add ApiCredential to ApiContext
	ApiContext context = new ApiContext();
	context.setApiCredential(credential);
	
	// set eBay server URL to call
	context.setApiServerUrl(ApiServerUrl);
	
	context.setSite(SiteCodeType.GERMANY);
	
	
	// set timeout in milliseconds - 5 minutes
	context.setTimeout(300000);
	
	// set wsdl version number
	context.setWSDLVersion("423");
	
	// turn on logging
	ApiLogging logging = new ApiLogging();
	logging.setLogHTTPHeaders(true);
	logging.setLogSOAPMessages(true);
	logging.setLogExceptions(true);
	context.setApiLogging(logging);
	
    
	return context;
	
}


private static ShippingDetailsType getShippingDetails() {
	// Shipping details.
			ShippingDetailsType sd = new ShippingDetailsType();
			SalesTaxType salesTax = new SalesTaxType();
			salesTax.setSalesTaxPercent(new Float(0.0825));
			salesTax.setSalesTaxState("CA");
			sd.setApplyShippingDiscount(new Boolean(true));
			AmountType at = new AmountType();
			// at.setValue(2.8);
			// sd.setInsuranceFee(at);
			// sd.setInsuranceOption(InsuranceOptionCodeType.NOT_OFFERED);
			sd.setPaymentInstructions("Pauschale Versandkosten");

			// Set calculated shipping.
			sd.setShippingType(ShippingTypeCodeType.FLAT);
			//
			ShippingServiceOptionsType st1 = new ShippingServiceOptionsType();
			st1
					.setShippingService(ShippingServiceCodeType.DE_SPECIAL_DELIVERY
							.value());
			at = new AmountType();
			//at.setValue(29.90);
			at.setValue(0.00);
			st1.setShippingServiceAdditionalCost(at);
			at = new AmountType();
			//at.setValue(29.90);
			at.setValue(0.00);
			st1.setShippingServiceCost(at);
			st1.setShippingServicePriority(new Integer(1));
			at = new AmountType();
			at.setValue(1.0);
			st1.setShippingInsuranceCost(at);

			ShippingServiceOptionsType st2 = new ShippingServiceOptionsType();
			st2.setExpeditedService(new Boolean(true));
			st2.setShippingService(ShippingServiceCodeType.DE_PICKUP
					.value());
			at = new AmountType();
			at.setValue(0);
			st2.setShippingServiceAdditionalCost(at);
			at = new AmountType();
			at.setValue(0);
			st2.setShippingServiceCost(at);
			st2.setShippingServicePriority(new Integer(2));
			at = new AmountType();
			at.setValue(0);
			st2.setShippingInsuranceCost(at);
		

			sd.setShippingServiceOptions(new ShippingServiceOptionsType[] { st1,st2 });
			
			return sd;
}


private static String getEBayDescription(CommonExportConfig config, Artikel artikel, String short_description, String long_description, List<String> pictureURLs) {

	String ebayText = new String();
	
	hauptBild = pictureURLs.get(0);
	
	try {
		
		// Freemarker: Create a configuration instance
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setDirectoryForTemplateLoading(new File(config.templateDir));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);	
		
		
			
			// Freemarker: Create a data-model
			// Create the root hash
			Map<String, Object> root = new HashMap<>();
			root.put("name", artikel.getKurzname());

			int lieferzeitWerktage = lieferzeitKunde - (lieferzeitKunde / 7 * 2);
			
			if (lieferzeitWerktage <= 8) {
			root.put("delivery_time", "AB LAGER VERFÜGBAR - Lieferzeit ca. 5 Werktage");
			root.put("color_stock", "green");
			} else if (lieferzeitWerktage <= 15) {
				root.put("delivery_time", "VERFÜGBAR - Lieferzeit ca. "+ lieferzeitWerktage +" Werktage");
				root.put("color_stock", "green");
			} else if (lieferzeitWerktage <= 28) {
				root.put("delivery_time", "IM ZULAUF - Lieferzeit ca. "+ lieferzeitWerktage +" Werktage");
				root.put("color_stock", "yellow");
			} else if (lieferzeitWerktage <= 1000) {
				root.put("delivery_time", "Lieferzeit ca. "+ lieferzeitWerktage / 5 +" Wochen");
				root.put("color_stock", "red");
			}
			root.put("short_description", short_description);
			root.put("description", long_description);
			//root.put("short_description", tempShortDescription);
			//root.put("description", tempDescription);
			root.put("alttitlebild", artikel.getKasName().replaceAll("[^a-zA-Z0-9ßäüöÄÜÖ]+"," "));
			root.put("hauptbild", pictureURLs.get(0));
			root.put("pictureList", pictureURLs);
			
			
			// Freemarker: Get the template
			Template temp = cfg.getTemplate("ebayTemplate.ftl");
			
			// Freemarker: Merging the template with the data-model
			StringWriter writer = new StringWriter();
			temp.process(root, writer);
			ebayText = writer.getBuffer().toString();
			//System.out.println(ebayText);
			

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

return ebayText;
}



public static String dateieinlesen(String Dateiname){ 
    Scanner sc=null; 
    File f= new File(Dateiname); 
    String ausgabe=""; 
    try{ 
        sc= new Scanner(f); 
    }catch(FileNotFoundException e){ 
        System.out.println("Fehler: Datei wurde nicht gedunden!"); 
    } 
    while (sc.hasNextLine()){ 
        ausgabe=ausgabe+ "\n" + sc.nextLine(); 
    } 
    return ausgabe; 
}  





public class ebayData {

String kat1;
String kat2;
String kat3;
String kat4;
String artnum;
String artnum2;


public ebayData() {
	
}

public ebayData(String kat1, String kat2, String artnum, String kat3, String kat4, String artnum2) {
	super();
	this.kat1 = kat1;
	this.kat2 = kat2;
	this.kat3 = kat3;
	this.kat4 = kat4;
	this.artnum = artnum;
	this.artnum2 = artnum2;
}

public ebayData(String kat1, String kat2, String artnum, String artnum2) {
	super();
	this.kat1 = kat1;
	this.kat2 = kat2;
	this.kat3 = null;
	this.kat4 = null;
	this.artnum = artnum;
	this.artnum2 = artnum2;
}

public ebayData parseEBayData(Artikel art) {
	ebayData ret = null;
	if (art.getUserfeld02() != null && art.getUserfeld02().length() > 5) {
		String ebayStrng = new String();
		if (art.getUserfeld02().contains("/"))
			ebayStrng = new String(art.getUserfeld02().substring(0, art.getUserfeld02().indexOf("/")));
		else
			ebayStrng = new String(art.getUserfeld02());
		
		String[] parts = ebayStrng.split(";");
		if (parts.length != 4 && parts.length != 6 ){
			return null;
		}
		if (parts.length == 4)
		ret = new ebayData(parts[0],parts[1],parts[2],parts[3]);
		if (parts.length == 6)
		ret = new ebayData(parts[0],parts[1],parts[2],parts[3],parts[4],parts[5]);
		
	}
	return ret;
}

public String getEBayCAOString() {
	if (this.kat3 == null && this.kat4 == null)
	return kat1+";"+kat2+";"+artnum+";"+artnum2;
	else
		return kat1+";"+kat2+";"+artnum+";"+kat3+";"+kat4+";"+artnum2;
}

public String getKat1() {
	return kat1;
}
public void setKat1(String kat1) {
	this.kat1 = kat1;
}
public String getKat2() {
	return kat2;
}
public void setKat2(String kat2) {
	this.kat2 = kat2;
}
public String getKat3() {
	if (this.kat3 == null)
		return kat1;
	else
	return kat3;
}
public void setKat3(String kat3) {
	this.kat3 = kat3;
}
public String getKat4() {
	if (this.kat4 == null)
		return kat2;
	else
	return kat4;
}
public void setKat4(String kat4) {
	this.kat4 = kat4;
}
public String getArtnum() {
	return artnum;
}
public void setArtnum(String artnum) {
	this.artnum = artnum;
}
public String getArtnum2() {
	return artnum2;
}
public void setArtnum2(String artnum2) {
	this.artnum2 = artnum2;
}
	
}

}









