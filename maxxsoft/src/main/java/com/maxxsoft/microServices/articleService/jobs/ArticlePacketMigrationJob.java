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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.microServices.articleService.model.Packet;
import com.maxxsoft.microServices.articleService.model.request.Verpackungsdaten;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.PacketRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticlePacketMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private PacketRepository packetRepository;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	private ErpArticleStueckRepository erpArticleStueckRepository;

	// One time process
	// @Scheduled(fixedRate = 1000000)
	public void runPacketMigrationArticle() {

		System.out.println("start.....");

		articleRepository.findAll().forEach(article -> {
			// if (article.isStandalone() && article.getArticleId() == 1) {
			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
			if (artikelOptional.isPresent()) {
				List<Packet> packets = new ArrayList<>();
				Artikel artikel = artikelOptional.get();
				Verpackungsdaten erppackets = getVerpackungsdaten(artikel.getInfo());
				if (erppackets != null) {
					erppackets.getPackstuecke().forEach(erpPacket -> {
						Packet packet = new Packet(erpPacket.getGewicht(), erpPacket.getHoehe(), erpPacket.getBreite(),
								erpPacket.getTiefe(), "MIGRATED_PACKET", article.getArticleId());
						packets.add(packet);
					});
					packetRepository.saveAll(packets);
				} else {
					System.out.println("No artikel.getInfo() is available for articke number-----ID---- "
							+ article.getArticleId());
					System.out.println(
							"No artikel.getInfo() is available for articke number------INFO- " + artikel.getInfo());
					System.out.println(
							"No artikel.getInfo() is available for articke number----ARTNUM- " + artikel.getArtnum());
				}
			}
			// }
		});
		System.out.println("end.....");
	}

	// @Scheduled(fixedRate = 10000000)
	public void runPacketMigrationArticleSet() {
		System.out.println("start.....");

		articleSetRepository.findAll().forEach(articleSet -> {

			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
			if (artikelOptional.isPresent()) {
				Artikel artikel = artikelOptional.get();
				Verpackungsdaten packets = getVerpackungsdaten(artikel.getInfo());
			}

		});
		System.out.println("end.....");
	}

	public static Verpackungsdaten getVerpackungsdaten(String erpInfo) {
		Verpackungsdaten verpDaten = new Verpackungsdaten();
		String xmlVerpackungsdaten = new String();
		if (erpInfo.contains("<verpackungsdaten>") && erpInfo.contains("</verpackungsdaten>")) {
			xmlVerpackungsdaten = erpInfo.substring(erpInfo.indexOf("<verpackungsdaten>"),
					erpInfo.indexOf("</verpackungsdaten>") + 19);
		} else
			return null;
		// System.out.println(xmlVerpackungsdaten);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Verpackungsdaten.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xmlVerpackungsdaten);
			Verpackungsdaten data = (Verpackungsdaten) jaxbUnmarshaller.unmarshal(reader);
			verpDaten = data;
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return verpDaten;
	}
}
