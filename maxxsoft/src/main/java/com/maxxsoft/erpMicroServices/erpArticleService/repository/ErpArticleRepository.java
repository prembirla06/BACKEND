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
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
@Transactional
public interface ErpArticleRepository extends JpaRepository<Artikel, Integer> {

	public Optional<Artikel> findByArtnum(String articleNumber);

	@Query(nativeQuery = true, value = "SELECT a.* FROM artikel a where a.WARENGRUPPE IN(80100,80150,80160,80165,80190) and a.ARTIKELTYP = ?1")
	public List<Artikel> findAllByArtikeltypAndWarengruppe(String artikelType);

	@Query(nativeQuery = true, value = "SELECT a.* FROM artikel a where a.ARTIKELTYP = ?1")
	public Set<Artikel> findAllByArtikeltyp(String artikelType);

	@Query(nativeQuery = true, value = "select ekbestell_op.MENGE_OFFEN, ekbestell_info.LIEFERTERMIN from artikel RIGHT JOIN ekbestell_op ON artikel.REC_ID = ekbestell_op.ARTIKEL_ID RIGHT JOIN ekbestell_info ON ekbestell_op.EKBESTPOS_ID = ekbestell_info.EKBESTPOS_ID WHERE ekbestell_info.LIEFERTERMIN != '0000-00-00' AND artikel.ARTNUM = ?1 ORDER BY ekbestell_info.LIEFERTERMIN")
	public List<Object[]> findAllArtikelEkbestels(String articleNumber);
	
	@Query(nativeQuery = true, value = "select ekbestell_op.MENGE_OFFEN, ekbestell_info.LIEFERTERMIN from artikel RIGHT JOIN ekbestell_op ON artikel.REC_ID = ekbestell_op.ARTIKEL_ID RIGHT JOIN ekbestell_info ON ekbestell_op.EKBESTPOS_ID = ekbestell_info.EKBESTPOS_ID WHERE ekbestell_info.LIEFERTERMIN != '0000-00-00' AND (ekbestell_info.LIEFERINFO LIKE '%Tour%' OR ekbestell_info.LIEFERINFO LIKE '%tour%') AND artikel.ARTNUM = ?1 ORDER BY ekbestell_info.LIEFERTERMIN")
	public List<Object[]> findAllArtikelEkbestelsStrict(String articleNumber);

	public void deleteByArtnum(String articleNumber);

	@Query(nativeQuery = true, value = "select SUM(JP.MENGE) from journalpos JP, journal J  where JP.JOURNAL_ID=J.REC_ID and JP.ARTIKEL_ID= ?1 and J.RDATUM>(ADDDATE(current_date, INTERVAL - ?2 DAY)) and JP.QUELLE IN (3,4) and KUN_NUM != 017583")
	public Integer getTotalMenge(int recId, int day);

	@Query(nativeQuery = true, value = "SELECT USERFELD_01 FROM Adressen  WHERE REC_ID = (select ADRESS_ID from ARTIKEL_PREIS where ARTIKEL_ID= ?1 and PREIS_TYP=5 and ADRESS_ID>0 LIMIT 1)")
	public Integer getSupplierDeliveyTime(int recId);
}
