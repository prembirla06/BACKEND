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
package com.maxxsoft.microServices.userService.service;

import java.util.List;
import java.util.Optional;

import com.maxxsoft.microServices.userService.model.User;
import com.maxxsoft.microServices.userService.model.request.ChangePasswordRequest;
import com.maxxsoft.microServices.userService.model.request.SignUpRequest;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface UserService {

	User findByUsername(String username);

	Optional<User> findById(Long userId);

	List<User> users();

	void save(SignUpRequest signUpRequest);

	void updateUserPassword(ChangePasswordRequest changePasswordRequest);

	void delete(Long userId);

	public boolean matchOldPassword(ChangePasswordRequest changePasswordRequest);

}