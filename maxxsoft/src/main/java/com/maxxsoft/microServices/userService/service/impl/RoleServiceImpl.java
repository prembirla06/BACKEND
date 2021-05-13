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
package com.maxxsoft.microServices.userService.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.microServices.userService.model.Roles;
import com.maxxsoft.microServices.userService.model.request.RoleKey;
import com.maxxsoft.microServices.userService.repository.RoleRepository;
import com.maxxsoft.microServices.userService.service.RoleService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<Roles> findByRoleKey(RoleKey roleKey) {
		return roleRepository.findByRoleKey(roleKey);
	}

	@Override
	public Map<RoleKey, String> findAll() {
		List<Roles> allRoles = roleRepository.findAll();
		return allRoles.stream().collect(Collectors.toMap(Roles::getRoleKey, Roles::getRoleValue));
	}

}
