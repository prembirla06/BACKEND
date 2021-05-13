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
package com.maxxsoft.microServices.userService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maxxsoft.microServices.userService.model.Roles;
import com.maxxsoft.microServices.userService.model.request.RoleKey;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Optional<Roles> findByRoleKey(RoleKey roleKey);

}
