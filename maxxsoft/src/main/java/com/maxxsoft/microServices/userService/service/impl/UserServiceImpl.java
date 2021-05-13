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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.exception.ErrorException;
import com.maxxsoft.microServices.userService.model.Roles;
import com.maxxsoft.microServices.userService.model.User;
import com.maxxsoft.microServices.userService.model.request.ChangePasswordRequest;
import com.maxxsoft.microServices.userService.model.request.RoleKey;
import com.maxxsoft.microServices.userService.model.request.SignUpRequest;
import com.maxxsoft.microServices.userService.repository.UserRepository;
import com.maxxsoft.microServices.userService.service.RoleService;
import com.maxxsoft.microServices.userService.service.UserService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service("userService")
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		if (username.trim().isEmpty()) {
			throw new UsernameNotFoundException("username is empty");
		}
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException("User " + username + " not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleKey().toString()))
				.collect(Collectors.toList());
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public void save(SignUpRequest signUpRequest) {
		Set<String> strRoles = signUpRequest.getRole();
		Set<Roles> roles = new HashSet<>();
		strRoles.forEach(role -> {
			Roles adminRole = roleService.findByRoleKey(RoleKey.fromName(role))
					.orElseThrow(() -> new ErrorException(ErrorCode.ROLE_DOES_NOT_EXIST));
			roles.add(adminRole);
		});
		userRepository.save(User.builder().username(signUpRequest.getUsername())
				.password(bCryptPasswordEncoder.encode(signUpRequest.getPassword())).enabled(Boolean.TRUE).roles(roles)
				.build());
	}

	@Override
	public void updateUserPassword(ChangePasswordRequest changePasswordRequest) {

		Optional<User> user = userRepository.findById(changePasswordRequest.getUserId());

		if (user.isPresent()) {
			User existingUser = user.get();
			existingUser.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword()));
		}
	}

	@Override
	public List<User> users() {
		return userRepository.findAll();
	}

	@Override
	public boolean matchOldPassword(ChangePasswordRequest changePasswordRequest) {
		Optional<User> user = userRepository.findById(changePasswordRequest.getUserId());

		if (user.isPresent()) {
			User existingUser = user.get();
			return bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), existingUser.getPassword());
		}
		return true;
	}

	@Override
	public void delete(Long userId) {
		Optional<User> user = userRepository.findById(userId);

		if (user.isPresent()) {
			userRepository.delete(user.get());
		}
	}
}
