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
package com.maxxsoft.erpMicroServices.erpArticleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxxsoft.erpMicroServices.erpArticleService.model.ArtikelStueckListID;
import com.maxxsoft.erpMicroServices.erpArticleService.model.ArtikelStuecklist;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface ErpArticleStueckRepository extends JpaRepository<ArtikelStuecklist, ArtikelStueckListID> {

	@Query(nativeQuery = true, value = "SELECT a.ART_ID, a.MENGE FROM artikel_stuecklist a where a.ARTIKEL_ART='STL' and a.REC_ID = ?1")
	public List<Object[]> findByRecId(int recId);
}
