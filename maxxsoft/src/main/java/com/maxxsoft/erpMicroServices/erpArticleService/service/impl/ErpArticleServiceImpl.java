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
package com.maxxsoft.erpMicroServices.erpArticleService.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.model.ArtikelStueckListID;
import com.maxxsoft.erpMicroServices.erpArticleService.model.ArtikelStuecklist;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Slf4j
@Service
@Transactional
public class ErpArticleServiceImpl implements ErpArticleService {

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	private ErpArticleStueckRepository erpArticleStueckRepository;

	BigDecimal zero = new BigDecimal(0);
	BigDecimal one = new BigDecimal(1);

	@Override
	public void updateArticle(Article article) {
		Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
		if (artikelOptional.isPresent()) {
			Artikel artikel = artikelOptional.get();
			// artikel.setArtnum(article.getNumber());
			artikel.setBarcode(article.getEan());
			artikel.setLangname(article.getLongName());
			artikel.setMatchcode(article.getLongName());
			artikel.setKurzname(article.getShortName());
			artikel.setKasName(article.getShortName());
			artikel.setErsatzArtnum(article.getSubstituteNumber());
			artikel.setVpeEk(BigDecimal.valueOf(article.getPackageUnit()));
			artikel.setGewicht(BigDecimal.valueOf(article.getTotalWeight()));
			artikel.setEkPreis(article.getBuyPrice());
			artikel.setDimension(String.valueOf(article.getDimension()));

			erpArticleRepository.saveAndFlush(artikel);
		}
	}

	@Override
	public void pushArticle(Article article) {

		Artikel artikel = new Artikel();
		artikel.setArtnum(article.getNumber());
		artikel.setBarcode(article.getEan());
		artikel.setLangname(article.getLongName());
		artikel.setMatchcode(article.getLongName());
		artikel.setKurzname(article.getShortName());
		artikel.setKasName(article.getShortName());
		artikel.setErsatzArtnum(article.getSubstituteNumber());
		artikel.setVpeEk(BigDecimal.valueOf(article.getPackageUnit()));
		artikel.setGewicht(BigDecimal.valueOf(article.getTotalWeight()));
		artikel.setEkPreis(article.getBuyPrice());
		artikel.setDimension(String.valueOf(article.getDimension()));
		// activate-deactive field
		artikel.setWarengruppe(70000);
		artikel.setArtikeltyp("N");
		artikel.setKasName(article.getShortName());
		setDefaultValues(artikel);
		erpArticleRepository.save(artikel);

	}

	@Override
	public void deleteErpArticle(String artNum) {
		Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(artNum);
		if (artikelOptional.isPresent()) {
			// erpArticleRepository.delete(artikelOptional.get());
			// Erp Article soft delete
			Artikel artikel = artikelOptional.get();
			artikel.setWarengruppe(70001);
			artikel.setNoEkFlag("Y");
			artikel.setMengeMin(BigDecimal.ZERO);
			erpArticleRepository.saveAndFlush(artikel);

		}

	}

	@Override
	public void updateArticleSet(ArticleSet articleSet) {
		Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
		if (artikelOptional.isPresent()) {
			Artikel artikel = artikelOptional.get();
			// artikel.setArtnum(article.getNumber());
			artikel.setBarcode(articleSet.getEan());
			artikel.setLangname(articleSet.getLongName());
			artikel.setMatchcode(articleSet.getLongName());
			artikel.setKurzname(articleSet.getShortName());
			artikel.setKasName(articleSet.getShortName());
			artikel.setErsatzArtnum(articleSet.getSubstituteNumber());
			artikel.setDimension(String.valueOf(articleSet.getDimension()));
			// artikel.setVpeEk(BigDecimal.valueOf(articleSet.getPackageUnit()));
			// artikel.setGewicht(BigDecimal.valueOf(articleSet.getTotalWeight()));
			// artikel.setEkPreis(articleSet.getBuyPrice());

			erpArticleRepository.saveAndFlush(artikel);
		}
	}

	@Override
	public void pushArticleSet(ArticleSet articleSet) {

		Artikel artikel = new Artikel();
		artikel.setArtnum(articleSet.getNumber());
		artikel.setBarcode(articleSet.getEan());
		artikel.setLangname(articleSet.getLongName());
		artikel.setMatchcode(articleSet.getLongName());
		artikel.setKurzname(articleSet.getShortName());
		artikel.setKasName(articleSet.getShortName());
		artikel.setErsatzArtnum(articleSet.getSubstituteNumber());
		artikel.setDimension(String.valueOf(articleSet.getDimension()));
		// fields not in SET

		artikel.setVpeEk(one);// good

		artikel.setGewicht(one);
		artikel.setEkPreis(one);

		// activate-deactive field
		artikel.setWarengruppe(70000);
		artikel.setArtikeltyp("S");
		artikel.setKasName(articleSet.getShortName());
		setDefaultValues(artikel);

		Artikel savedArtikel = erpArticleRepository.save(artikel);

		List<ArtikelStuecklist> artikelStuecklistlist = new ArrayList<>();

		articleSet.getArticleSetRelations().forEach(asr -> {

			ArtikelStuecklist artikelStuecklist = new ArtikelStuecklist();
			ArtikelStueckListID artikelStueckListID = new ArtikelStueckListID(savedArtikel.getRecId(),
					erpArticleRepository.findByArtnum(asr.getArticle().getNumber()).get().getRecId(), "STL");
			artikelStuecklist.setArtikelStueckListID(artikelStueckListID);

			artikelStuecklist.setMenge(asr.getQuantity());

			// required fiels
			artikelStuecklist.setPosition("position");
			artikelStuecklistlist.add(artikelStuecklist);

		});

		// articleRepository.findAllAssociatedArticles(articleSet.getArticleSetId()).forEach(article
		// -> {
		// ArtikelStuecklist artikelStuecklist = new ArtikelStuecklist();
		// ArtikelStueckListID artikelStueckListID = new
		// ArtikelStueckListID(savedArtikel.getRecId(),
		// erpArticleRepository.findByArtnum(article.getNumber()).get().getRecId(),
		// "STL");
		// artikelStuecklist.setArtikelStueckListID(artikelStueckListID);
		//
		// artikelStuecklist.setMenge(articleSet.getArticleSetRelations());
		//
		// });

		erpArticleStueckRepository.saveAll(artikelStuecklistlist);

	}

	private Artikel setDefaultValues(Artikel artikel) {
		artikel.setShopId((byte) 1);
		artikel.setShopChangeFlag((byte) 111);
		artikel.setShopDelFlag("N");
		artikel.setErsatzArtikelId(-1);
		artikel.setRabgrpId("-");
		artikel.setMeId(1);
		artikel.setVpe(one);
		artikel.setPrEinheit(one);
		artikel.setInventurWert(new BigDecimal(100));
		artikel.setEkVpe(zero);
		artikel.setEkdsPreis(zero);
		artikel.setVk1(zero);
		artikel.setVk1b(zero);
		artikel.setVk2(zero);
		artikel.setVk2b(zero);
		artikel.setVk3(zero);
		artikel.setVk3b(zero);
		artikel.setVk4(zero);
		artikel.setVk4b(zero);
		artikel.setVk5(zero);
		artikel.setVk5b(zero);
		artikel.setBasisprFaktor(one);
		artikel.setBasisprMeId(1);
		artikel.setMaxrabatt(zero);
		artikel.setMingewinn(zero);
		artikel.setProvisProz(zero);
		artikel.setSteuerCode((byte) 1);
		artikel.setAltteilFlag("N");
		artikel.setNoRabattFlag("N");
		artikel.setNoProvisionFlag("N");
		artikel.setNoBezeditFlag("N");
		artikel.setNoEkFlag("N");
		artikel.setNoVkFlag("N");
		artikel.setSnFlag("N");
		artikel.setFsk18Flag("N");
		artikel.setAutodelFlag("N");
		artikel.setKommisionFlag("N");
		artikel.setLizenzFlag("N");
		artikel.setProduktionFlag("N");
		artikel.setNoStockFlag("N");
		artikel.setKonfig((byte) 1);
		artikel.setKennzeichnungFlag((byte) 0);
		artikel.setMengeFlag((byte) 0);
		artikel.setProdDauer(14);
		artikel.setVkLieferzeitId(1);
		artikel.setEkLieferzeitId(1);
		artikel.setMengeAkt(zero);
		artikel.setMengeMin(one);
		artikel.setMengeBvor(one);
		artikel.setMengeStart(one);
		artikel.setMengeWarn(one);
		artikel.setDefaultLiefId(-1);// BR
		artikel.setErloesKto(8400);
		artikel.setAufwKto(3400);
		artikel.setHerstellerId(-1);
		artikel.setPfand1ArtikelId(-1);
		artikel.setPfand1Menge(one);
		artikel.setPfand2ArtikelId(-1);
		artikel.setPfand2Menge(zero);
		artikel.setEtikettPrInteger((byte) 0);
		artikel.setLiefstatus("LAGER");
		artikel.setVarId(-1);// CJ
		artikel.setNeZuschlagFlag("N");
		artikel.setNeGewicht(zero);
		artikel.setNeTyp("L");
		artikel.setErstellt(new Date());
		artikel.setErstName("MaXXSoft");
		artikel.setGeaend(new Date());
		artikel.setGeaendName("MaXXSoft");
		artikel.setShopArtikelId(-1);
		artikel.setShopPreisListe(zero);
		artikel.setShopVisible(1);
		artikel.setShopClickCount(0);
		artikel.setShopSync((byte) 0);
		artikel.setShopZub((byte) 0);
		artikel.setShopChangeFlag((byte) 0);
		artikel.setShopDelFlag("N");
		artikel.setShopSort(0);
		artikel.setShopLiefstatus(1);
		artikel.setShopLieftext("Standard");
		artikel.setShopChangeLief(0);
		artikel.setProjektArtikel((byte) 0);
		return artikel;
	}

	@Override
	public Integer getStock(String articleNumber) {
		if (erpArticleRepository.findByArtnum(articleNumber).isPresent()) {
			BigDecimal stock = erpArticleRepository.findByArtnum(articleNumber).get().getMengeAkt();
			if (stock.compareTo(zero) == 0) {
				return 0;
			}
			return stock.intValue();
		} else {
			log.error("Erp Article with number " + articleNumber + " is not present.");
		}
		return null;

	}

	@Override
	public Integer getPreOrder(String articleNumber) {
		if (erpArticleRepository.findByArtnum(articleNumber).isPresent()) {
			return erpArticleRepository.findByArtnum(articleNumber).get().getShopSort();
		} else {
			log.error("Erp Article with number " + articleNumber + " is not present.");
		}
		return null;
	}

	@Override
	public String getKasName(String articleNumber) {
		if (erpArticleRepository.findByArtnum(articleNumber).isPresent()) {
			return erpArticleRepository.findByArtnum(articleNumber).get().getKasName();
		} else {
			log.error("Erp Article with number " + articleNumber + " is not present.");
		}
		return null;
	}

	@Override
	public String getDimension(String articleNumber) {
		if (erpArticleRepository.findByArtnum(articleNumber).isPresent()) {
			return erpArticleRepository.findByArtnum(articleNumber).get().getDimension();
		} else {
			log.error("Erp Article with number " + articleNumber + " is not present.");
		}
		return null;
	}

	@Override
	public Set<Artikel> getAllArtikelByType(String artikelType) {
		return erpArticleRepository.findAllByArtikeltyp(artikelType);
	}

	@Override
	public Optional<Artikel> getArtikelByNumber(String articleNumber) {
		return erpArticleRepository.findByArtnum(articleNumber);
	}

	@Override
	public List<Object[]> findAllArtikelEkbestels(String articleNumber) {
		return erpArticleRepository.findAllArtikelEkbestels(articleNumber);
	}
	
	@Override
	public List<Object[]> findAllArtikelEkbestelsStrict(String articleNumber) {
		return erpArticleRepository.findAllArtikelEkbestelsStrict(articleNumber);
	}

	@Override
	public Integer getOrderLastxDays(int recId, int day) {

		Integer result = erpArticleRepository.getTotalMenge(recId, day);

		if (result == null)
			return 0;
		else
			return result;

	}
}