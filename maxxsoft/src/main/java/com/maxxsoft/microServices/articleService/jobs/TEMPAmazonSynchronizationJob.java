package com.maxxsoft.microServices.articleService.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.FeedSubmissionInfo;
import com.amazonaws.mws.model.IdList;
import com.amazonaws.mws.model.ResponseMetadata;
import com.amazonaws.mws.model.SubmitFeedRequest;
import com.amazonaws.mws.model.SubmitFeedResponse;
import com.amazonaws.mws.model.SubmitFeedResult;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForSKURequest;
import com.amazonservices.mws.products.model.GetLowestPricedOffersForSKUResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.amazonservices.mws.products.model.SKUOfferDetail;
import com.amazonservices.mws.products.model.SKUOfferDetailList;
import com.amazonservices.mws.products.samples.GetLowestPricedOffersForSKUSample;
import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.exportJob.CommonExportConfig;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.amazon_temp.AmazonInventory;
import com.maxxsoft.microServices.articleService.model.amazon_temp.AmazonPrice;
import com.maxxsoft.microServices.articleService.model.amazon_temp.AmazonProduct;
import com.maxxsoft.microServices.articleService.model.amazon_temp.AmazonProductImage;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;
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
public class  TEMPAmazonSynchronizationJob {

	// AMAZON Variablen 
	/************************************************************************
     * Access Key ID and Secret Access Key ID, obtained from:
     * http://aws.amazon.com
     ***********************************************************************/
	static final String accessKeyId = "AKIAIWQTQDIIBWYNHX4Q";
    static final String secretAccessKey = "qQxemNs0FGjqfD8lVV7U3r2XLLWptus9iXXhkua7";

    static final String appName = "TecMaXX GmbH";
    static final String appVersion = "<Your Application Version or Build Number or Release Date>";
    
    static final String merchantId = "AHL13GY14NYFE";
    static final String sellerDevAuthToken = "amzn.mws.0aa48421-1804-ea50-c5af-e4738a9f9464";
    
    static final IdList marketplaces = new IdList(Arrays.asList("A1PA6795UKMFR9"));
    static final String marketplaceId = "A1PA6795UKMFR9";
    
    
    
    private static MarketplaceWebServiceProductsAsyncClient client = null;
    private static final String serviceURL = "https://mws.amazonservices.de";
    /** Developer AWS access key. */
    private static final String accessKey = "AKIAIWQTQDIIBWYNHX4Q";
    /** Developer AWS secret key. */
    private static final String secretKey = "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";
    
    
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
	CommonExportConfig config;
	
	@Autowired
	private SellingPlatformRepository sellingPlatformRepository;
	
	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	CommonConfigRepository commonConfigRepository;

    
    
	// STATISCHE VARIABLEN
    static int fulfillmentLatencyPaketdienst = 5; // Normal 1 Tag
    static int fulfillmentLatencySpedition = 8; // Normal 2 Tage
    static double priceBelowCheapest = 1.10; // Wechsel als günstigster bei 0,40 Euro, letzter Wert 0,80
    static int maximumRequestsPerHour = 200;
    static int waitingMinutesAfterReachMaximumRequests = 60;
    static int requestsPerHour = 0; // wird während laufzeit verändert
    
    static int lagerMenge = 0;
	static ArrayList<String> FeedSubmissionIds;
	
	static boolean konkurrenzPreisvergleich = false;
	
	// immer erst Produkte abgleichen mit Preis auf false - dann Preis danach
	static boolean abgleichEigenartikel = true;
	static boolean createInventory = true;
	static boolean createPrice = true;
	static boolean createProductImage = true;
	static boolean createProduct = true;
	
	
	static boolean abgleichFremdartikel = true;
	static boolean createInventoryFremdartikel = true;
	static boolean createPriceFremdartikel = true;
	static boolean createProductFremdartikel = true;
	
	
	// DATENBANKZUGRIFF
	//private static IDaoImpl impl;
	//static HelperCAO caohelper;
	
	@Scheduled(cron = "${cronexpression.TEMPAmazonSynchonizationJob}")
	public void runArticleTEMPAmazonSynchonizationJob() {
	FeedSubmissionIds = new ArrayList<String>();	
	log.info("start..runArticleTEMPAmazonSynchonizationJob...");
	ArrayList<String> artikelPriceVeryLow = new ArrayList();
	
	if (abgleichEigenartikel) {
		ArrayList<AmazonInventory> amazonInventoryList = new ArrayList<AmazonInventory>();
		ArrayList<AmazonPrice> amazonPriceList = new ArrayList<AmazonPrice>();
		ArrayList<AmazonProductImage> amazonProductImageList = new ArrayList<AmazonProductImage>();
		ArrayList<AmazonProduct> amazonProductList = new ArrayList<AmazonProduct>();
	
	// EIGENARTIKEL IN LISTE eigenArtikelCAO AUFNEHMEN
	ArrayList<Artikel> eigenArtikelCAO = new ArrayList();
	articleRepository.findAll().forEach(article -> {
		if (article.isActive() && article.isStandalone()) {
		Artikel artikelCAO = erpArticleRepository.findByArtnum(article.getNumber()).get();
		//System.out.println(artikelCAO.getArtnum());
		if (artikelCAO.getUserfeld06() != null && !artikelCAO.getUserfeld06().equals("") && !artikelCAO.getUserfeld06().contains("x")) {
			lagerMenge = article.getStock() - article.getPreOrder();
			if (requestsPerHour != 0 && requestsPerHour % 195 == 0) {
				
	             try {
	            	 for (int i = 0; i < 60; i++) {
	            		 TimeUnit.MINUTES.sleep(1);
	            		 log.info(i+1 + " Minuten gewartet");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         log.info("Time's Up!");
	         requestsPerHour = 0;
}
			
			
			// Amazon Inventory List erstellen
			if (createInventory) {
			AmazonInventory amazonInventoryElement = new AmazonInventory();
			amazonInventoryElement.setSku(artikelCAO.getArtnum());
			
			if (
					(lagerMenge >= 2 && artikelCAO.getMengeMin().intValue() <= 2) ||
					(lagerMenge >= 4 && artikelCAO.getMengeMin().intValue() <= 4) ||
					(lagerMenge >= 5 && artikelCAO.getMengeMin().intValue() > 4)
					) {
				amazonInventoryElement.setQuantity(lagerMenge);
				if (artikelCAO.getInfo().contains("paketdienst"))
					amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencyPaketdienst);
					else if (artikelCAO.getInfo().contains("spedition"))
					amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
					else amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
				}
				else {
					amazonInventoryElement.setQuantity(10);
					int deliveryTimeStrict = this.getDeliveryTimeStrict(article);
					if (deliveryTimeStrict <= 30)
					amazonInventoryElement.setFulfillmentLatency(deliveryTimeStrict);
					else {
						amazonInventoryElement.setFulfillmentLatency(30);
						amazonInventoryElement.setQuantity(0);
					}
				}
			
			amazonInventoryList.add(amazonInventoryElement);
			}
			
			
			// Amazon Price List erstellen
			if (createPrice) {
			// EIGENER STANDARD PREIS BERECHNEN
			int actualStock = article.getStock() - article.getPreOrder();
			BigDecimal sellPrice = priceService.getSellingOrSalePrice(article.getNumber(), actualStock, article.getBuyPrice(), sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
			
			BigDecimal amazonStandardPrice = sellPrice;
			
		 	BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(article.getNumber(),
					article.getBuyPrice(), sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
			
			BigDecimal amazonMinimalPrice = minimumSellingPrice;
			
			
			BigDecimal amazonPreis = amazonStandardPrice;
			
			if (konkurrenzPreisvergleich) {
			// PREISVERGLEICH MIT ANDEREN ANBIETERN
			MarketplaceWebServiceProductsClient client = this.getMarketplaceWebServiceProductsClient();
					// Create a request.
			        GetLowestPricedOffersForSKURequest request = new GetLowestPricedOffersForSKURequest();
			        request.setSellerId(merchantId);
			        request.setMWSAuthToken(sellerDevAuthToken);
			        
			        request.setMarketplaceId(marketplaceId);
			        String sellerSKU = artikelCAO.getArtnum();
			        request.setSellerSKU(sellerSKU);
			        String itemCondition = "New";
			        request.setItemCondition(itemCondition);
			        
			     // Make the call.
			        BigDecimal lowestPrice = null;
			        boolean findOtherOffers = false;
			        try {
			        GetLowestPricedOffersForSKUResponse response = new GetLowestPricedOffersForSKUResponse();
			        response = GetLowestPricedOffersForSKUSample.invokeGetLowestPricedOffersForSKU(client, request);
			        requestsPerHour ++;
			        SKUOfferDetailList offers = response.getGetLowestPricedOffersForSKUResult().getOffers();
			        List<SKUOfferDetail> offersArray = offers.getOffer();

			        if (!offersArray.isEmpty()) {
			        for (Iterator iterator1 = offersArray.iterator(); iterator1.hasNext();) {
			        	SKUOfferDetail offer = (SKUOfferDetail) iterator1.next();
			        	if (!offer.getMyOffer()) {
			        		findOtherOffers = true;
			        		if (lowestPrice == null) {
			        			lowestPrice = offer.getListingPrice().getAmount().add(offer.getShipping().getAmount());
			        		} else {
			        			
			        			if (lowestPrice.doubleValue() > offer.getListingPrice().getAmount().add(offer.getShipping().getAmount()).doubleValue()) {
			        				lowestPrice = offer.getListingPrice().getAmount();
			        			}
			        		}
			        		
			        	}
					}
			        log.info("Niedrigster Angebotspreis (inkl. Versand) anderem Anbieter:" + article.getNumber() + " Lowest Price:" +  lowestPrice);
			        } else {
			        	// no other offers found
				        //System.out.println("keine Andere Angebote gefunden:" + findOtherOffers);
				        
			        }
			        	
			        } catch (MarketplaceWebServiceProductsException ex) {
			        	 log.error("Service Exception, Message: "+ex.getMessage());
			        	 //javax.swing.JOptionPane.showInputDialog("Problem, es k�nnen keine Preise mehr abgerufen werden");
			        	 if (!ex.getMessage().contains("is an invalid SKU for marketplace"))
			        	 requestsPerHour = 195;
			        }
			
			        
			        if (findOtherOffers) {
			        	if (lowestPrice.doubleValue() >= amazonMinimalPrice.doubleValue()) {
			        		amazonPreis = lowestPrice.subtract(new BigDecimal(priceBelowCheapest));
			        	} else {
			        		artikelPriceVeryLow.add("Artikelnummer: " + artikelCAO.getArtnum() + " Konkurrenzpreis: " + lowestPrice.doubleValue());
			        		amazonPreis = amazonMinimalPrice;
			        	}
			        } else
			        	amazonPreis = amazonStandardPrice;
			  
			}
			// SET AMAZON PRICE
			AmazonPrice amazonPriceElement = new AmazonPrice();
			amazonPriceElement.setSku(artikelCAO.getArtnum());
			amazonPriceElement.setStandardPrice(amazonPreis.doubleValue());
			amazonPriceElement.setCurrencyPrice("EUR");
			amazonPriceList.add(amazonPriceElement);
			}
			
			
			// Amazon ProductImage List erstellen
			if (createProductImage || createProduct) {
			//CoreStore coreStoreAdmin = impl.getCoreStore("Admin");
			
			if (createProductImage) {
				// Bilder HOLEN
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum());
				List<String> pictureURLs = new ArrayList<String>();
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(config.mediaURL + media.getFile());
					});
				}
				
				for (int i = 0; i < pictureURLs.size() && i < 8; i++) {
					AmazonProductImage image = new AmazonProductImage();
					image.setSku(artikelCAO.getArtnum());
				    if (i == 0) {
				    	image.setImageType("Main");
				    } else {
				    	image.setImageType("PT" + String.valueOf(i));
				    }
				    image.setImageLocation(pictureURLs.get(i));
				    amazonProductImageList.add(image);
				}
			}
			
			
			// Amazon Product List erstellen
			if (createProduct) {
			String amazonStrng = new String(artikelCAO.getUserfeld06());
			AmazonProduct amazonProductElement = new AmazonProduct();
			amazonProductElement.setSku(artikelCAO.getArtnum());
			if (artikelCAO.getBarcode() == null || artikelCAO.getBarcode().length() < 5){
				log.error(artikelCAO.getArtnum() + " PROBLEM MIT EAN CODE");
				System.exit(0);
			}
			String amazonEAN = new String();
			if (amazonStrng.contains("(") && amazonStrng.contains(")")) {
				amazonEAN = amazonStrng.substring(amazonStrng.indexOf("(")+1, amazonStrng.indexOf(")"));
			} else amazonEAN = artikelCAO.getBarcode();
			amazonProductElement.setStandardProductID(amazonEAN);
			amazonProductElement.setLaunchDate(new Date());
			amazonProductElement.setCondition("New");
			
			amazonProductElement.setTitle(artikelCAO.getKurzname());
			amazonProductElement.setBrand("moebel-guenstig24.de");
			amazonProductElement.setManufacturer("von moebel-guenstig24.de");
	    	
			amazonProductElement.setDescription(this.createAmazonDescription(article.getLongDescription()));

			ArrayList<String> amazonAttribute = this.createAmazonDescriptionAttribute(article.getLongDescription());
			if (amazonAttribute.size() >= 1) 
			amazonProductElement.setBulletPoint1(amazonAttribute.get(0));
			if (amazonAttribute.size() >= 2) 
			amazonProductElement.setBulletPoint2(amazonAttribute.get(1));
			if (amazonAttribute.size() >= 3) 
			amazonProductElement.setBulletPoint3(amazonAttribute.get(2));
			if (amazonAttribute.size() >= 4) 
			amazonProductElement.setBulletPoint4(amazonAttribute.get(3));
			if (amazonAttribute.size() >= 5) 
			amazonProductElement.setBulletPoint5(amazonAttribute.get(4));
			
			
			// wenn etwas in Klammer steht entferne es:
			if (amazonStrng.contains("(") && amazonStrng.contains(")")) {
				amazonStrng = amazonStrng.substring(0, amazonStrng.indexOf("("));
			}
			
			String[] parts = amazonStrng.split(";");
			if (parts.length == 1) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(null);
				amazonProductElement.setRecommendedBrowseNode3(null);
			}
			else if (parts.length == 2) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(parts[1]);
				amazonProductElement.setRecommendedBrowseNode3(null);
			}
			else if (parts.length == 3) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(parts[1]);
				amazonProductElement.setRecommendedBrowseNode3(parts[2]);
			} else {
				log.error(artikelCAO.getKurzname() + "    PROBLEM BEIM Amazon-Datenfeld");
				System.exit(0);
			}
			
			amazonProductList.add(amazonProductElement);
			}
			}
			
			
			
			
		}
		
	}
	});
	
	
	
	
	
	// Sets
	articleSetRepository.findAll().forEach(articleSet -> {
		//if (articleSet.getNumber().equals("10-D0-DD-81+29-D0-DD-02")) {
		Artikel artikelCAO = erpArticleRepository.findByArtnum(articleSet.getNumber()).get();
		//System.out.println(artikelCAO.getArtnum());
		if (artikelCAO.getUserfeld06() != null && !artikelCAO.getUserfeld06().equals("") && !artikelCAO.getUserfeld06().contains("x")) {
			lagerMenge = articleSet.getStock() - articleSet.getPreOrder();
			if (requestsPerHour != 0 && requestsPerHour % 195 == 0) {
				
	             try {
	            	 for (int i = 0; i < 60; i++) {
	            		 TimeUnit.MINUTES.sleep(1);
	            		 //System.out.println(i+1 + " waited minutes");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         log.info("Time's Up!");
	         requestsPerHour = 0;
}
			
			
			// Amazon Inventory List erstellen
			if (createInventory) {
			AmazonInventory amazonInventoryElement = new AmazonInventory();
			amazonInventoryElement.setSku(artikelCAO.getArtnum());
			
			if (
					(lagerMenge >= 2 && artikelCAO.getMengeMin().intValue() <= 2) ||
					(lagerMenge >= 4 && artikelCAO.getMengeMin().intValue() <= 4) ||
					(lagerMenge >= 5 && artikelCAO.getMengeMin().intValue() > 4)
					) {
				amazonInventoryElement.setQuantity(lagerMenge);
				if (artikelCAO.getInfo().contains("paketdienst"))
					amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencyPaketdienst);
					else if (artikelCAO.getInfo().contains("spedition"))
					amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
					else amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
				}
				else {
					int deliveryTimeStrict = this.getSetDeliveryTimeStrict(articleSet);
					amazonInventoryElement.setQuantity(10);
					if (deliveryTimeStrict <= 30)
					amazonInventoryElement.setFulfillmentLatency(deliveryTimeStrict);
					else {
						amazonInventoryElement.setFulfillmentLatency(30);
						amazonInventoryElement.setQuantity(0);
					}
				}
			
			amazonInventoryList.add(amazonInventoryElement);
			}
			
			
			// Amazon Price List erstellen
			if (createPrice) {
			// EIGENER STANDARD PREIS BERECHNEN
			int actualStock = articleSet.getStock() - articleSet.getPreOrder();
			BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellPrice = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
			
			BigDecimal amazonStandardPrice = sellPrice;
			
		 	BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(articleSet.getNumber(),
		 			setBuyPrice, sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
			
			BigDecimal amazonMinimalPrice = minimumSellingPrice;
			
			
			
			BigDecimal amazonPreis = amazonStandardPrice;
			
			if (konkurrenzPreisvergleich) {
			// PREISVERGLEICH MIT ANDEREN ANBIETERN
			MarketplaceWebServiceProductsClient client = this.getMarketplaceWebServiceProductsClient();
					// Create a request.
			        GetLowestPricedOffersForSKURequest request = new GetLowestPricedOffersForSKURequest();
			        request.setSellerId(merchantId);
			        request.setMWSAuthToken(sellerDevAuthToken);
			        
			        request.setMarketplaceId(marketplaceId);
			        String sellerSKU = artikelCAO.getArtnum();
			        request.setSellerSKU(sellerSKU);
			        String itemCondition = "New";
			        request.setItemCondition(itemCondition);
			        
			     // Make the call.
			        BigDecimal lowestPrice = null;
			        boolean findOtherOffers = false;
			        try {
			        GetLowestPricedOffersForSKUResponse response = new GetLowestPricedOffersForSKUResponse();
			        response = GetLowestPricedOffersForSKUSample.invokeGetLowestPricedOffersForSKU(client, request);
			        requestsPerHour ++;
			        SKUOfferDetailList offers = response.getGetLowestPricedOffersForSKUResult().getOffers();
			        List<SKUOfferDetail> offersArray = offers.getOffer();

			        if (!offersArray.isEmpty()) {
			        for (Iterator iterator1 = offersArray.iterator(); iterator1.hasNext();) {
			        	SKUOfferDetail offer = (SKUOfferDetail) iterator1.next();
			        	if (!offer.getMyOffer()) {
			        		findOtherOffers = true;
			        		if (lowestPrice == null) {
			        			lowestPrice = offer.getListingPrice().getAmount().add(offer.getShipping().getAmount());
			        		} else {
			        			
			        			if (lowestPrice.doubleValue() > offer.getListingPrice().getAmount().add(offer.getShipping().getAmount()).doubleValue()) {
			        				lowestPrice = offer.getListingPrice().getAmount();
			        			}
			        		}
			        		
			        	}
					}
			        //System.out.println("Niedrigster Angebotspreis (inkl. Versand) anderem Anbieter:" + lowestPrice);
			        } else {
			        	// no other offers found
				        //System.out.println("keine Andere Angebote gefunden:" + findOtherOffers);
				        
			        }
			        	
			        } catch (MarketplaceWebServiceProductsException ex) {
			        	 log.error("Service Exception, Message: "+ex.getMessage());
			        	 //javax.swing.JOptionPane.showInputDialog("Problem, es k�nnen keine Preise mehr abgerufen werden");
			        	 if (!ex.getMessage().contains("is an invalid SKU for marketplace"))
			        	 requestsPerHour = 195;
			        }
			
			        
			        if (findOtherOffers) {
			        	if (lowestPrice.doubleValue() >= amazonMinimalPrice.doubleValue()) {
			        		amazonPreis = lowestPrice.subtract(new BigDecimal(priceBelowCheapest));
			        	} else {
			        		artikelPriceVeryLow.add("Artikelnummer: " + artikelCAO.getArtnum() + " Konkurrenzpreis: " + lowestPrice.doubleValue());
			        		amazonPreis = amazonMinimalPrice;
			        	}
			        } else
			        	amazonPreis = amazonStandardPrice;
			  
			}
			
			// AMAZON PREIS SETZEN
			AmazonPrice amazonPriceElement = new AmazonPrice();
			amazonPriceElement.setSku(artikelCAO.getArtnum());
			amazonPriceElement.setStandardPrice(amazonPreis.doubleValue());
			amazonPriceElement.setCurrencyPrice("EUR");
			amazonPriceList.add(amazonPriceElement);
			}
			
			
			// Amazon ProductImage List erstellen
			if (createProductImage || createProduct) {
			//CoreStore coreStoreAdmin = impl.getCoreStore("Admin");
			
			if (createProductImage) {
				// Bilder HOLEN
				List<MagentoMedia> magentoMediaList = magentoMediaRepository.findBySkuOrderByPositionAsc(artikelCAO.getArtnum());
				List<String> pictureURLs = new ArrayList<String>();
				if (magentoMediaList.size() > 0) {
					magentoMediaList.forEach(media -> {
						pictureURLs.add(config.mediaURL + media.getFile());
					});
				}
				
				for (int i = 0; i < pictureURLs.size() && i < 8; i++) {
					AmazonProductImage image = new AmazonProductImage();
					image.setSku(artikelCAO.getArtnum());
				    if (i == 0) {
				    	image.setImageType("Main");
				    } else {
				    	image.setImageType("PT" + String.valueOf(i));
				    }
				    image.setImageLocation(pictureURLs.get(i));
				    amazonProductImageList.add(image);
				}
			}
			
			
			// Amazon Product List erstellen
			if (createProduct) {
			String amazonStrng = new String(artikelCAO.getUserfeld06());
			AmazonProduct amazonProductElement = new AmazonProduct();
			amazonProductElement.setSku(artikelCAO.getArtnum());
			if (artikelCAO.getBarcode() == null || artikelCAO.getBarcode().length() < 5){
				log.error(artikelCAO.getArtnum() + " PROBLEM MIT EAN CODE");
				System.exit(0);
			}
			String amazonEAN = new String();
			if (amazonStrng.contains("(") && amazonStrng.contains(")")) {
				amazonEAN = amazonStrng.substring(amazonStrng.indexOf("(")+1, amazonStrng.indexOf(")"));
			} else amazonEAN = artikelCAO.getBarcode();
			amazonProductElement.setStandardProductID(amazonEAN);
			amazonProductElement.setLaunchDate(new Date());
			amazonProductElement.setCondition("New");
			
			amazonProductElement.setTitle(artikelCAO.getKurzname());
			amazonProductElement.setBrand("moebel-guenstig24.de");
			amazonProductElement.setManufacturer("von moebel-guenstig24.de");

			amazonProductElement.setDescription(this.createAmazonDescription(articleSet.getLongDescription()));
			
			ArrayList<String> amazonAttribute = this.createAmazonDescriptionAttribute(articleSet.getLongDescription());
			if (amazonAttribute.size() >= 1) 
			amazonProductElement.setBulletPoint1(amazonAttribute.get(0));
			if (amazonAttribute.size() >= 2) 
			amazonProductElement.setBulletPoint2(amazonAttribute.get(1));
			if (amazonAttribute.size() >= 3) 
			amazonProductElement.setBulletPoint3(amazonAttribute.get(2));
			if (amazonAttribute.size() >= 4) 
			amazonProductElement.setBulletPoint4(amazonAttribute.get(3));
			if (amazonAttribute.size() >= 5) 
			amazonProductElement.setBulletPoint5(amazonAttribute.get(4));
			
			
			// wenn etwas in Klammer steht entferne es:
			if (amazonStrng.contains("(") && amazonStrng.contains(")")) {
				amazonStrng = amazonStrng.substring(0, amazonStrng.indexOf("("));
			}
			
			String[] parts = amazonStrng.split(";");
			if (parts.length == 1) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(null);
				amazonProductElement.setRecommendedBrowseNode3(null);
			}
			else if (parts.length == 2) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(parts[1]);
				amazonProductElement.setRecommendedBrowseNode3(null);
			}
			else if (parts.length == 3) {
				amazonProductElement.setRecommendedBrowseNode1(parts[0]);
				amazonProductElement.setRecommendedBrowseNode2(parts[1]);
				amazonProductElement.setRecommendedBrowseNode3(parts[2]);
			} else {
				log.error(artikelCAO.getKurzname() + "    PROBLEM BEIM Amazon-Datenfeld");
				System.exit(0);
			}
			
			amazonProductList.add(amazonProductElement);
			}
			}
			
			
			
			
		}
	//	}
	});
	
	
	
	
	
	
	try {
		// Freemarker: Create a configuration instance
		Configuration cfg = config.getConfiguration();
		cfg.setNumberFormat("###############");
		Map<String, Object> root = new HashMap<>();
		
		// 1. xml invenory_feed erzeugen
		if (createInventory) {
		root.put("amazonInvenoryList", amazonInventoryList);
			
		Template tempInventory = cfg.getTemplate("amazonInventoryFeedTemplate.xml");
		
		FileWriter writerInventory = new FileWriter(config.outputDir + "amazon_inventory_list.xml", false);
		tempInventory.process(root, writerInventory);
		writerInventory.close();
		}
		
		// 2. xml price feed erzeugen
		if (createPrice) {
		root.put("amazonPriceList", amazonPriceList);
			
		Template tempPrice = cfg.getTemplate("amazonPriceFeedTemplate.xml");
		
		FileWriter writerPrice = new FileWriter(config.outputDir + "amazon_price_list.xml", false);
		tempPrice.process(root, writerPrice);
		writerPrice.close();
		}
		
		
		// 3. xml productImage feed erzeugen
		if (createProductImage) {
		root.put("amazonProductImageList", amazonProductImageList);
					
		Template tempProductImage = cfg.getTemplate("amazonProductImageFeedTemplate.xml");
				
		FileWriter writerProductImage = new FileWriter(config.outputDir + "amazon_productImage_list.xml", false);
		tempProductImage.process(root, writerProductImage);
		writerProductImage.close();
		}		
				
		// 4. xml product feed erzeugen
		if (createProduct) {
		root.put("amazonProductList", amazonProductList);
					
		Template tempProduct = cfg.getTemplate("amazonProductFeedTemplate.xml");
				
		FileWriter writerProduct = new FileWriter(config.outputDir + "amazon_product_list.xml", false);
		tempProduct.process(root, writerProduct);
		writerProduct.close();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

	
	
	// TODO: 2. amazon update
	
    MarketplaceWebServiceConfig marketplaceWebSericeConfig = new MarketplaceWebServiceConfig();
    marketplaceWebSericeConfig.setServiceURL("https://mws.amazonservices.de");
    MarketplaceWebService service = new MarketplaceWebServiceClient(
            accessKeyId, secretAccessKey, appName, appVersion, marketplaceWebSericeConfig);
    
    SubmitFeedRequest requestInventory = new SubmitFeedRequest();
    SubmitFeedRequest requestPrice = new SubmitFeedRequest();
    SubmitFeedRequest requestProductImage = new SubmitFeedRequest();
  SubmitFeedRequest requestProduct = new SubmitFeedRequest();
    
    
    requestInventory.setMerchant(merchantId);
    requestPrice.setMerchant(merchantId);
    requestProductImage.setMerchant(merchantId);
    requestProduct.setMerchant(merchantId);
    
    requestInventory.setMarketplaceIdList(marketplaces);
    requestPrice.setMarketplaceIdList(marketplaces);
    requestProductImage.setMarketplaceIdList(marketplaces);
    requestProduct.setMarketplaceIdList(marketplaces);

    requestInventory.setFeedType("_POST_INVENTORY_AVAILABILITY_DATA_");
    requestPrice.setFeedType("_POST_PRODUCT_PRICING_DATA_");
    requestProductImage.setFeedType("_POST_PRODUCT_IMAGE_DATA_");
    requestProduct.setFeedType("_POST_PRODUCT_DATA_");

    try {
    	FileInputStream isInventory = new FileInputStream(config.outputDir + "amazon_inventory_list.xml");
    	requestInventory.setFeedContent(isInventory);
    	requestInventory.setContentMD5(computeContentMD5HeaderValue(isInventory));
    	FileInputStream isPrice = new FileInputStream(config.outputDir + "amazon_price_list.xml");
    	requestPrice.setFeedContent(isPrice);
    	requestPrice.setContentMD5(computeContentMD5HeaderValue(isPrice));
    	FileInputStream isProductPicture = new FileInputStream(config.outputDir + "amazon_productImage_list.xml");
    	requestProductImage.setFeedContent(isProductPicture);
    	requestProductImage.setContentMD5(computeContentMD5HeaderValue(isProductPicture));
    	FileInputStream isProduct = new FileInputStream(config.outputDir + "amazon_product_list.xml");
    	requestProduct.setFeedContent(isProduct);
    	requestProduct.setContentMD5(computeContentMD5HeaderValue(isProduct));
		
		
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    SubmitFeedResponse responseInventory = new SubmitFeedResponse();
    if (createInventory)
    responseInventory = invokeSubmitFeed(service, requestInventory);
    
    SubmitFeedResponse responsePrice = new SubmitFeedResponse();
    if (createPrice)
    responsePrice = invokeSubmitFeed(service, requestPrice);
    
    SubmitFeedResponse responseProductImage = new SubmitFeedResponse();
    if (createProductImage)
    responseProductImage = invokeSubmitFeed(service, requestProductImage);
    
    SubmitFeedResponse responseProduct = new SubmitFeedResponse();
    if (createProduct)
    responseProduct = invokeSubmitFeed(service, requestProduct);
	
    
    if (createInventory) {
    log.info("INVENTORY: FeedSubmissionId by CAOAmazonAbgleich: " + responseInventory.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	FeedSubmissionIds.add("Inventory:" + responseInventory.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
    }
    if (createPrice) {
    log.info("PRICE: FeedSubmissionId by CAOAmazonAbgleich: " + responsePrice.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	FeedSubmissionIds.add("Price:" + responsePrice.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
    }
    if (createProductImage) {
    log.info("PRODUCT_IMAGE: FeedSubmissionId by CAOAmazonAbgleich: " + responseProductImage.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	FeedSubmissionIds.add("Product_Image:" + responseProductImage.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
    }
    if (createProduct) {
    log.info("PRODUCT: FeedSubmissionId by CAOAmazonAbgleich: " + responseProduct.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	FeedSubmissionIds.add("Product:" + responseProduct.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
    }
	
	
	
	// Neues File erzeugen!
			try{
		         FileOutputStream fos= new FileOutputStream(config.outputDir + "AmazonFeedSubmissionIds");
		         ObjectOutputStream oos= new ObjectOutputStream(fos);
		         oos.writeObject(FeedSubmissionIds);
		         oos.close();
		         fos.close();
		       }catch(IOException ioe){
		            ioe.printStackTrace();
		        }
			
	
	
	}



	
			
	
			
			
			
		
			
			
			
		
			
		
			
	if (abgleichFremdartikel) {		
				
		ArrayList<AmazonInventory> amazonInventoryList = new ArrayList<AmazonInventory>();
		ArrayList<AmazonPrice> amazonPriceList = new ArrayList<AmazonPrice>();
		ArrayList<AmazonProduct> amazonProductList = new ArrayList<AmazonProduct>();
		
			
		// Article Fremdartikel Abgleich
			articleRepository.findAll().forEach(article -> {
				if (article.isActive() && article.isStandalone()) {
				Artikel artikelCAO = erpArticleRepository.findByArtnum(article.getNumber()).get();
				if (artikelCAO.getUserfeld07() != null && !artikelCAO.getUserfeld07().equals("") && !artikelCAO.getUserfeld07().equals("x") && !artikelCAO.getUserfeld07().equals("X")) {
					if (requestsPerHour != 0 && requestsPerHour % 199 == 0) {
						
			             try {
			            	 for (int i = 0; i < 60; i++) {
			            		 TimeUnit.MINUTES.sleep(1);
			            		 //System.out.println(i+1 + " Minuten gewartet");
			            	 }
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             
			         log.info("Time's Up!");
			         requestsPerHour = 0;
	}
	
	
   String allAttributeString = artikelCAO.getUserfeld07();
   StringTokenizer tokenzierAllAttributeString = new StringTokenizer (allAttributeString, ";"); 
   while (tokenzierAllAttributeString.hasMoreElements()) {
   	String amazonAsinString = tokenzierAllAttributeString.nextToken();
   	
   	
	
	//System.out.println("Start Fremdartikelabgleich - ASIN:" + amazonAsinString + " Artikel: "+ artikelCAO.getKurzname());
	if (amazonAsinString.equals("B00ENCN77Y")) {
		//System.out.println("HIER!");
	}
	lagerMenge = article.getStock() - article.getPreOrder();
	
	
	// Amazon Inventory List erstellen
	if (createInventoryFremdartikel) {
	AmazonInventory amazonInventoryElement = new AmazonInventory();
	amazonInventoryElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
	
	if (lagerMenge > 0) {
		amazonInventoryElement.setQuantity(lagerMenge);
		if (artikelCAO.getInfo().contains("paketdienst"))
		amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencyPaketdienst);
		else if (artikelCAO.getInfo().contains("spedition"))
		amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
		else amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
		}
		else {
		amazonInventoryElement.setQuantity(10);
		int deliveryTimeStrict = this.getDeliveryTimeStrict(article);
		if (deliveryTimeStrict <= 30)
		amazonInventoryElement.setFulfillmentLatency(deliveryTimeStrict);
		else {
			amazonInventoryElement.setFulfillmentLatency(30);
			amazonInventoryElement.setQuantity(0);
		}
	}
	
	amazonInventoryList.add(amazonInventoryElement);
	}
	
	
	// Amazon Price List erstellen
	if (createPriceFremdartikel) {
		// EIGENER STANDARD PREIS BERECHNEN
		int actualStock = article.getStock() - article.getPreOrder();
		BigDecimal sellPrice = priceService.getSellingOrSalePrice(article.getNumber(), actualStock, article.getBuyPrice(), sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
		
		BigDecimal amazonStandardPrice = sellPrice;
		
	 	BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(article.getNumber(),
				article.getBuyPrice(), sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
		
		BigDecimal amazonMinimalPrice = minimumSellingPrice;
		
		
		
		BigDecimal amazonPreis = amazonStandardPrice;
		

		
		if (konkurrenzPreisvergleich) {
			// PREISVERGLEICH MIT ANDEREN ANBIETERN
			MarketplaceWebServiceProductsClient client = this.getMarketplaceWebServiceProductsClient();
				// Create a request.
		        GetLowestPricedOffersForSKURequest request = new GetLowestPricedOffersForSKURequest();
		        request.setSellerId(merchantId);
		        request.setMWSAuthToken(sellerDevAuthToken);
		        //String marketplaceId = "A1PA6795UKMFR9";
		        request.setMarketplaceId(marketplaces.getId().get(0));
		        String sellerSKU = artikelCAO.getArtnum() + "/" + amazonAsinString;
		        request.setSellerSKU(sellerSKU);
		        String itemCondition = "New";
		        request.setItemCondition(itemCondition);
		        
		     // Make the call.
		        BigDecimal lowestPrice = null;
		        boolean findOtherOffers = false;
		        try {
		        GetLowestPricedOffersForSKUResponse response = new GetLowestPricedOffersForSKUResponse();  
		        response = GetLowestPricedOffersForSKUSample.invokeGetLowestPricedOffersForSKU(client, request);
		        requestsPerHour ++;
		        SKUOfferDetailList offers = response.getGetLowestPricedOffersForSKUResult().getOffers();
		        List<SKUOfferDetail> offersArray = offers.getOffer();

		        if (!offersArray.isEmpty()) {
			        for (Iterator iterator1 = offersArray.iterator(); iterator1.hasNext();) {
			        	SKUOfferDetail offer = (SKUOfferDetail) iterator1.next();
			        	if (!offer.getMyOffer()) {
			        		findOtherOffers = true;
			        		if (lowestPrice == null) {
			        			lowestPrice = offer.getListingPrice().getAmount().add(offer.getShipping().getAmount());
			        		} else {
			        			
			        			if (lowestPrice.doubleValue() > offer.getListingPrice().getAmount().add(offer.getShipping().getAmount()).doubleValue()) {
			        				lowestPrice = offer.getListingPrice().getAmount();
			        			}
			        		}
			        		
			        	}
					}
			        log.info("Niedrigster Angebotspreis (inkl. Versand) anderem Anbieter:" + article.getNumber() + " Lowest Price:" +  lowestPrice);
			        } else {
			        	// no other offers found
				        //System.out.println("keine Andere Angebote gefunden:" + findOtherOffers);
				        
			        }
			        	
			        } catch (MarketplaceWebServiceProductsException ex) {
			        	log.error("Service Exception, Message: "+ex.getMessage());
			        	 //javax.swing.JOptionPane.showInputDialog("Problem, es k�nnen keine Preise mehr abgerufen werden");
			        	 if (!ex.getMessage().contains("is an invalid SKU for marketplace"))
			        	 requestsPerHour = 195;
			        }
			
			        
			        if (findOtherOffers) {
			        	if (lowestPrice.doubleValue() >= amazonMinimalPrice.doubleValue()) {
			        		amazonPreis = lowestPrice.subtract(new BigDecimal(priceBelowCheapest));
			        	} else {
			        		artikelPriceVeryLow.add("Artikelnummer: " + artikelCAO.getArtnum() + " Konkurrenzpreis: " + lowestPrice.doubleValue());
			        		amazonPreis = amazonMinimalPrice;
			        	}
			        } else
			        	amazonPreis = amazonStandardPrice;
		}
		
		// AMAZON PREIS SETZEN
		AmazonPrice amazonPriceElement = new AmazonPrice();
		amazonPriceElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
		amazonPriceElement.setStandardPrice(amazonPreis.doubleValue());
		amazonPriceElement.setCurrencyPrice("EUR");
		amazonPriceList.add(amazonPriceElement);	
	}
	
	
	// Amazon Product List erstellen
	if (createProductFremdartikel) {
	//CoreStore coreStoreAdmin = impl.getCoreStore("Admin");
	//CatalogProductEntity productEntity = impl.getProductEntityBySKU(String.valueOf(artikelCAO.getArtnum()));
	
	AmazonProduct amazonProductElement = new AmazonProduct();
	amazonProductElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
	if (artikelCAO.getBarcode() == null || artikelCAO.getBarcode().length() < 5){
		log.error(artikelCAO.getArtnum() + " PROBLEM MIT EAN CODE");
		System.exit(0);
	}
	amazonProductElement.setStandardProductID(amazonAsinString);
	amazonProductElement.setLaunchDate(new Date());
	amazonProductElement.setCondition("New");
	
	
	amazonProductList.add(amazonProductElement);
	
	}
	
}

				}
				}
			});
				
				
			
			
		// ArticleSet Fremdartikel Abgleich
			articleSetRepository.findAll().forEach(articleSet -> {
				Artikel artikelCAO = erpArticleRepository.findByArtnum(articleSet.getNumber()).get();
				if (artikelCAO.getUserfeld07() != null && !artikelCAO.getUserfeld07().equals("") && !artikelCAO.getUserfeld07().equals("x") && !artikelCAO.getUserfeld07().equals("X")) {
					if (requestsPerHour != 0 && requestsPerHour % 199 == 0) {
						
			             try {
			            	 for (int i = 0; i < 60; i++) {
			            		 TimeUnit.MINUTES.sleep(1);
			            		 //System.out.println(i+1 + " Minuten gewartet");
			            	 }
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             
			         log.info("Time's Up!");
			         requestsPerHour = 0;
	}
	
	
   String allAttributeString = artikelCAO.getUserfeld07();
   StringTokenizer tokenzierAllAttributeString = new StringTokenizer (allAttributeString, ";"); 
   while (tokenzierAllAttributeString.hasMoreElements()) {
   	String amazonAsinString = tokenzierAllAttributeString.nextToken();
   	
   	
	
	//System.out.println("Start Fremdartikelabgleich - ASIN:" + amazonAsinString + " Artikel: "+ artikelCAO.getKurzname());
	if (amazonAsinString.equals("B00ENCN77Y")) {
		//System.out.println("HIER!");
	}
	lagerMenge = articleSet.getStock() - articleSet.getPreOrder();
	
	
	// Amazon Inventory List erstellen
	if (createInventoryFremdartikel) {
	AmazonInventory amazonInventoryElement = new AmazonInventory();
	amazonInventoryElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
	
	if (lagerMenge > 0) {
		amazonInventoryElement.setQuantity(lagerMenge);
		if (artikelCAO.getInfo().contains("paketdienst"))
		amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencyPaketdienst);
		else if (artikelCAO.getInfo().contains("spedition"))
		amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
		else amazonInventoryElement.setFulfillmentLatency(fulfillmentLatencySpedition);
		}
		else {
		amazonInventoryElement.setQuantity(10);
		int deliveryTimeStrict = this.getSetDeliveryTimeStrict(articleSet);
		if (deliveryTimeStrict <= 30)
		amazonInventoryElement.setFulfillmentLatency(deliveryTimeStrict);
		else {
			amazonInventoryElement.setFulfillmentLatency(30);
			amazonInventoryElement.setQuantity(0);
		}
	}
	
	amazonInventoryList.add(amazonInventoryElement);
	}
	
	
	// Amazon Price List erstellen
	if (createPriceFremdartikel) {
		// EIGENER STANDARD PREIS BERECHNEN
					int actualStock = articleSet.getStock() - articleSet.getPreOrder();
					BigDecimal setBuyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
					BigDecimal sellPrice = priceService.getSellingOrSalePrice(articleSet.getNumber(), actualStock, setBuyPrice, sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
					
					BigDecimal amazonStandardPrice = sellPrice;
					
				 	BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(articleSet.getNumber(),
				 			setBuyPrice, sellingPlatformRepository.findByName("Amazon").get().getSellingPlatformId());
					
					BigDecimal amazonMinimalPrice = minimumSellingPrice;
					
					
					BigDecimal amazonPreis = amazonStandardPrice;
		

		
		if (konkurrenzPreisvergleich) {
			// PREISVERGLEICH MIT ANDEREN ANBIETERN
			MarketplaceWebServiceProductsClient client = this.getMarketplaceWebServiceProductsClient();
				// Create a request.
		        GetLowestPricedOffersForSKURequest request = new GetLowestPricedOffersForSKURequest();
		        request.setSellerId(merchantId);
		        request.setMWSAuthToken(sellerDevAuthToken);
		        //String marketplaceId = "A1PA6795UKMFR9";
		        request.setMarketplaceId(marketplaces.getId().get(0));
		        String sellerSKU = artikelCAO.getArtnum() + "/" + amazonAsinString;
		        request.setSellerSKU(sellerSKU);
		        String itemCondition = "New";
		        request.setItemCondition(itemCondition);
		        
		     // Make the call.
		        BigDecimal lowestPrice = null;
		        boolean findOtherOffers = false;
		        try {
		        GetLowestPricedOffersForSKUResponse response = new GetLowestPricedOffersForSKUResponse();  
		        response = GetLowestPricedOffersForSKUSample.invokeGetLowestPricedOffersForSKU(client, request);
		        requestsPerHour ++;
		        SKUOfferDetailList offers = response.getGetLowestPricedOffersForSKUResult().getOffers();
		        List<SKUOfferDetail> offersArray = offers.getOffer();

		        if (!offersArray.isEmpty()) {
			        for (Iterator iterator1 = offersArray.iterator(); iterator1.hasNext();) {
			        	SKUOfferDetail offer = (SKUOfferDetail) iterator1.next();
			        	if (!offer.getMyOffer()) {
			        		findOtherOffers = true;
			        		if (lowestPrice == null) {
			        			lowestPrice = offer.getListingPrice().getAmount().add(offer.getShipping().getAmount());
			        		} else {
			        			
			        			if (lowestPrice.doubleValue() > offer.getListingPrice().getAmount().add(offer.getShipping().getAmount()).doubleValue()) {
			        				lowestPrice = offer.getListingPrice().getAmount();
			        			}
			        		}
			        		
			        	}
					}
			        log.info("Niedrigster Angebotspreis (inkl. Versand) anderem Anbieter:" + articleSet.getNumber() + " Lowest Price:" +  lowestPrice);
			        } else {
			        	// no other offers found
				        //System.out.println("keine Andere Angebote gefunden:" + findOtherOffers);
				        
			        }
			        	
			        } catch (MarketplaceWebServiceProductsException ex) {
			        	log.error("Service Exception, Message: "+ex.getMessage());
			        	//javax.swing.JOptionPane.showInputDialog("Problem, es k�nnen keine Preise mehr abgerufen werden");
			        	 if (!ex.getMessage().contains("is an invalid SKU for marketplace"))
			        	 requestsPerHour = 195;
			        }
			
			        
			        if (findOtherOffers) {
			        	if (lowestPrice.doubleValue() >= amazonMinimalPrice.doubleValue()) {
			        		amazonPreis = lowestPrice.subtract(new BigDecimal(priceBelowCheapest));
			        	} else {
			        		artikelPriceVeryLow.add("Artikelnummer: " + artikelCAO.getArtnum() + " Konkurrenzpreis: " + lowestPrice.doubleValue());
			        		amazonPreis = amazonMinimalPrice;
			        	}
			        } else
			        	amazonPreis = amazonStandardPrice;
		}
		
		// AMAZON PREIS SETZEN
		AmazonPrice amazonPriceElement = new AmazonPrice();
		amazonPriceElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
		amazonPriceElement.setStandardPrice(amazonPreis.doubleValue());
		amazonPriceElement.setCurrencyPrice("EUR");
		amazonPriceList.add(amazonPriceElement);	
	}
	
	
	// Amazon Product List erstellen
	if (createProductFremdartikel) {
	//CoreStore coreStoreAdmin = impl.getCoreStore("Admin");
	//CatalogProductEntity productEntity = impl.getProductEntityBySKU(String.valueOf(artikelCAO.getArtnum()));
	
	AmazonProduct amazonProductElement = new AmazonProduct();
	amazonProductElement.setSku(artikelCAO.getArtnum() + "/" + amazonAsinString);
	if (artikelCAO.getBarcode() == null || artikelCAO.getBarcode().length() < 5){
		log.error(artikelCAO.getArtnum() + " PROBLEM MIT EAN CODE");
		System.exit(0);
	}
	amazonProductElement.setStandardProductID(amazonAsinString);
	amazonProductElement.setLaunchDate(new Date());
	amazonProductElement.setCondition("New");
	
	
	amazonProductList.add(amazonProductElement);
	
	}
	
}

				}
			});
			
			

		
		
		
		
		try {
			// Freemarker: Create a configuration instance
			Configuration cfg = config.getConfiguration();
			cfg.setNumberFormat("###############");
			Map<String, Object> root = new HashMap<>();
			
			// 1. xml invenory_feed erzeugen
			if (createInventoryFremdartikel) {
			root.put("amazonInvenoryList", amazonInventoryList);
				
			Template tempInventory = cfg.getTemplate("amazonInventoryFeedTemplate.xml");
			
			FileWriter writerInventory = new FileWriter(config.outputDir + "amazon_inventory_list_fremdartikel.xml", false);
			tempInventory.process(root, writerInventory);
			writerInventory.close();
			}
			
			// 2. xml price feed erzeugen
			if (createPriceFremdartikel) {
			root.put("amazonPriceList", amazonPriceList);
				
			Template tempPrice = cfg.getTemplate("amazonPriceFeedTemplate.xml");
			
			FileWriter writerPrice = new FileWriter(config.outputDir + "amazon_price_list_fremdartikel.xml", false);
			tempPrice.process(root, writerPrice);
			writerPrice.close();
			}
					
			// 3. xml product feed erzeugen
			if (createProductFremdartikel) {
			root.put("amazonProductList", amazonProductList);
						
			Template tempProduct = cfg.getTemplate("amazonProductFeedFremdartikelTemplate.xml");
					
			FileWriter writerProduct = new FileWriter(config.outputDir + "amazon_product_list_fremdartikel.xml", false);
			tempProduct.process(root, writerProduct);
			writerProduct.close();
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		
		// TODO: 2. amazon update
		
		MarketplaceWebServiceConfig marketplaceWebSericeConfig = new MarketplaceWebServiceConfig();
		marketplaceWebSericeConfig.setServiceURL("https://mws.amazonservices.de");
	    MarketplaceWebService service = new MarketplaceWebServiceClient(
	            accessKeyId, secretAccessKey, appName, appVersion, marketplaceWebSericeConfig);
	    
	    SubmitFeedRequest requestInventory = new SubmitFeedRequest();
	    SubmitFeedRequest requestPrice = new SubmitFeedRequest();
	    SubmitFeedRequest requestProduct = new SubmitFeedRequest();
	    
	    
	    requestInventory.setMerchant(merchantId);
	    requestPrice.setMerchant(merchantId);
	    requestProduct.setMerchant(merchantId);
	    
	    requestInventory.setMarketplaceIdList(marketplaces);
	    requestPrice.setMarketplaceIdList(marketplaces);
	    requestProduct.setMarketplaceIdList(marketplaces);

	    requestInventory.setFeedType("_POST_INVENTORY_AVAILABILITY_DATA_");
	    requestPrice.setFeedType("_POST_PRODUCT_PRICING_DATA_");
	    requestProduct.setFeedType("_POST_PRODUCT_DATA_");
	    
	    try {
	    	FileInputStream isInventory = new FileInputStream(config.outputDir + "amazon_inventory_list_fremdartikel.xml");
	    	requestInventory.setFeedContent(isInventory);
	    	requestInventory.setContentMD5(computeContentMD5HeaderValue(isInventory));
	    	FileInputStream isPrice = new FileInputStream(config.outputDir + "amazon_price_list_fremdartikel.xml");
	    	requestPrice.setFeedContent(isPrice);
	    	requestPrice.setContentMD5(computeContentMD5HeaderValue(isPrice));
	    	FileInputStream isProduct = new FileInputStream(config.outputDir + "amazon_product_list_fremdartikel.xml");
	    	requestProduct.setFeedContent(isProduct);
	    	requestProduct.setContentMD5(computeContentMD5HeaderValue(isProduct));
			
			
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    SubmitFeedResponse responseInventory = new SubmitFeedResponse();
	    if (createInventoryFremdartikel)
	    responseInventory = invokeSubmitFeed(service, requestInventory);
	    
	    SubmitFeedResponse responsePrice = new SubmitFeedResponse();
	    if (createPriceFremdartikel)
	    responsePrice = invokeSubmitFeed(service, requestPrice);
	    
	    SubmitFeedResponse responseProduct = new SubmitFeedResponse();
	    if (createProductFremdartikel)
	    responseProduct = invokeSubmitFeed(service, requestProduct);
		
	    
	    if (createInventoryFremdartikel) {
	    log.info("INVENTORY: FeedSubmissionId by CAOAmazonAbgleich: " + responseInventory.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
		FeedSubmissionIds.add("Inventory_Fremd:" + responseInventory.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	    }
	    if (createPriceFremdartikel) {
	    log.info("PRICE: FeedSubmissionId by CAOAmazonAbgleich: " + responsePrice.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
		FeedSubmissionIds.add("Price_Fremd:" + responsePrice.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	    }
	    if (createProductFremdartikel) {
	    log.info("PRODUCT: FeedSubmissionId by CAOAmazonAbgleich: " + responseProduct.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
		FeedSubmissionIds.add("Product_Fremd:" + responseProduct.getSubmitFeedResult().getFeedSubmissionInfo().getFeedSubmissionId());
	    }
		
		
		
		// Neues File erzeugen!
				try{
			         FileOutputStream fos= new FileOutputStream(config.outputDir + "AmazonFeedSubmissionIds");
			         ObjectOutputStream oos= new ObjectOutputStream(fos);
			         oos.writeObject(FeedSubmissionIds);
			         oos.close();
			         fos.close();
			       }catch(IOException ioe){
			            ioe.printStackTrace();
			        }
				
		
		
		
		
	}
	
	
	
	
	for (Iterator iterator = artikelPriceVeryLow.iterator(); iterator.hasNext();) {
		String string = (String) iterator.next();
		log.info(string);
	}
	
	
	log.info("end..runArticleTEMPAmazonSynchonizationJob...");	
	
}

	
	private int getDeliveryTimeStrict(Article article) {
		int deliveryTime = 63;
		int defaultDeliveryTime = Integer
				.valueOf(commonConfigRepository.findByConfigKey("Default_Delivery_Time").getValue());
		int actualStock = article.getStock() - article.getPreOrder();
		if (actualStock > 0) {
			return article.getDeliveryTime();
		} else {
			List<Object[]> artikelEkbestelList = erpArticleService.findAllArtikelEkbestelsStrict(article.getNumber());
			if (artikelEkbestelList.size() > 0) {
				for (Iterator iterator = artikelEkbestelList.iterator(); iterator.hasNext();) {
					Object[] artikelEkbestel = (Object[]) iterator.next();
					if (((BigDecimal) artikelEkbestel[0]).intValue() > actualStock) {
						long nextStockDelivery = ChronoUnit.DAYS.between(LocalDate.now(), ((java.sql.Date) artikelEkbestel[1]).toLocalDate());
						if (nextStockDelivery <= 0) {
							return defaultDeliveryTime;
						} else {
							return (int)nextStockDelivery + defaultDeliveryTime;
						}
					} 
				}
			} 
		}
	return deliveryTime;
	}
	
	private int getSetDeliveryTimeStrict(ArticleSet articleSet) {
		List<Integer> deliveryTimeList = new ArrayList<Integer>();
		articleSet.getArticleSetRelations().forEach(asr -> {
			deliveryTimeList.add(this.getDeliveryTimeStrict(asr.getArticle()));
		});
		return Collections.max(deliveryTimeList);

		
	}


	// crate amazon bulletpoints
    private ArrayList<String> createAmazonDescriptionAttribute(String longDescription) {
    	ArrayList<String> AmazonAttribute = new ArrayList<String>();
    	Whitelist wl = Whitelist.none();
    	String[] split = longDescription.split("</p>");
    	for (int i = 0; i < split.length; i++) {
    		split[i] = split[i] + "</p>";
    		String clean = Jsoup.clean(split[i], wl);
    		clean = clean.replace(":", ": ");
    		clean = clean.replace("&nbsp;", " ");
    		boolean greaterThan499 = false;
    		while (clean.length() >= 499) {
    			greaterThan499 = true;
    			if (!clean.contains("-") && !clean.contains(".")) {
    				clean = clean.substring(0, clean.lastIndexOf(" "));
    			}
    			else if (clean.contains("-") && clean.lastIndexOf("-") > clean.lastIndexOf("."))
    			clean = clean.substring(0, clean.lastIndexOf("-"));
    			else {
    				clean = clean.substring(0, clean.lastIndexOf("."));
    			}
    			
    		}
    		if (greaterThan499) {
    			clean = clean + ".";
    		}
    		AmazonAttribute.add(clean);
		}

		return AmazonAttribute;
	}

    
    
    private String createAmazonDescription(String longDescription) {
    	Whitelist wl = Whitelist.basic();
		wl.removeTags("span");
    	String descString = Jsoup.clean(longDescription, wl);
    	descString = descString.replace("<br>", "<br/>");
    	descString = descString.replace("&nbsp;", " ");
    	descString = descString.replace("<", "&lt;");
		descString = descString.replace(">", "&gt;");
		return descString;
	}
    
    
    


	public static String computeContentMD5HeaderValue( FileInputStream fis )
    		throws IOException, NoSuchAlgorithmException {

    		DigestInputStream dis = new DigestInputStream( fis,
    		MessageDigest.getInstance( "MD5" ));
    		byte[] buffer = new byte[8192];
    		while( dis.read( buffer ) > 0 );
    		String md5Content = new String(
    		org.apache.commons.codec.binary.Base64.encodeBase64(dis.getMessageDigest().digest())
    		 );
    		// Effectively resets the stream to be beginning of the file via a FileChannel.
    		fis.getChannel().position( 0 );
    		return md5Content;
    		} 
	
    public static SubmitFeedResponse invokeSubmitFeed(MarketplaceWebService service,
            SubmitFeedRequest request) {
        SubmitFeedResponse response = null;
    	try {
    		response = service.submitFeed(request);

            /*System.out.println("SubmitFeed Action Response");
            System.out
            .println("=============================================================================");
            System.out.println();

            System.out.print("    SubmitFeedResponse");
            System.out.println();*/
            if (response.isSetSubmitFeedResult()) {
                /*System.out.print("        SubmitFeedResult");
                System.out.println();*/
                SubmitFeedResult submitFeedResult = response
                .getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    /*System.out.print("            FeedSubmissionInfo");
                    System.out.println();*/
                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult
                    .getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        /*System.out.print("                FeedSubmissionId");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedSubmissionId());
                        System.out.println();*/
                    }
                    if (feedSubmissionInfo.isSetFeedType()) {
                        /*System.out.print("                FeedType");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedType());
                        System.out.println();*/
                    }
                    if (feedSubmissionInfo.isSetSubmittedDate()) {
                        /*System.out.print("                SubmittedDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getSubmittedDate());
                        System.out.println();*/
                    }
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                        /*System.out
                        .print("                FeedProcessingStatus");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedProcessingStatus());
                        System.out.println();*/
                    }
                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
                        /*System.out
                        .print("                StartedProcessingDate");
                        System.out.println();
                        System.out
                        .print("                    "
                                + feedSubmissionInfo
                                .getStartedProcessingDate());
                        System.out.println();*/
                    }
                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
                        /*System.out
                        .print("                CompletedProcessingDate");
                        System.out.println();
                        System.out.print("                    "
                                + feedSubmissionInfo
                                .getCompletedProcessingDate());
                        System.out.println();*/
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                /*System.out.print("        ResponseMetadata");
                System.out.println();*/
                ResponseMetadata responseMetadata = response
                .getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    /*System.out.print("            RequestId");
                    System.out.println();
                    System.out.print("                "
                            + responseMetadata.getRequestId());
                    System.out.println();*/
                }
            }
            /*System.out.println(response.getResponseHeaderMetadata());
            System.out.println();
            System.out.println();*/

        } catch (MarketplaceWebServiceException ex) {

            log.error("Caught Exception: " + ex.getMessage());
            log.error("Response Status Code: " + ex.getStatusCode());
            log.error("Error Code: " + ex.getErrorCode());
            log.error("Error Type: " + ex.getErrorType());
            log.error("Request ID: " + ex.getRequestId());
            log.error("XML: " + ex.getXML());
            log.error("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
		return response;
    }

    
    
    
    /**
     * Get a client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static MarketplaceWebServiceProductsClient getMarketplaceWebServiceProductsClient() {
        return getMarketplaceWebServiceProductsAsyncClient();
    }

    /**
     * Get an async client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static synchronized MarketplaceWebServiceProductsAsyncClient getMarketplaceWebServiceProductsAsyncClient() {
        if (client==null) {
            MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceProductsAsyncClient(accessKey, secretKey, 
                    appName, appVersion, config, null);
        }
        return client;
    }
    
    
	
}
