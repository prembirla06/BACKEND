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
package com.maxxsoft.microServices.userService.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.common.model.Response;
import com.maxxsoft.common.model.StatusCode;
import com.maxxsoft.microServices.userService.model.User;
import com.maxxsoft.microServices.userService.model.request.AuthenticationRequest;
import com.maxxsoft.microServices.userService.model.request.ChangePasswordRequest;
import com.maxxsoft.microServices.userService.model.request.RoleKey;
import com.maxxsoft.microServices.userService.model.request.SignUpRequest;
import com.maxxsoft.microServices.userService.model.response.UserTransfer;
import com.maxxsoft.microServices.userService.service.RoleService;
import com.maxxsoft.microServices.userService.service.UserService;
import com.maxxsoft.microServices.userService.util.JwtTokenUtil;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Transactional
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@PostMapping(value = "/login/authenticate")
	public ResponseEntity<UserTransfer> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
				authenticationRequest.getPassword()));
		final User user = userService.findByUsername(authenticationRequest.getUsername());
		return new ResponseEntity<>(
				UserTransfer.builder().status(HttpStatus.OK).token(jwtTokenUtil.generateToken(user))
						.userid(user.getUserId())
						.username(user.getUsername()).roles(user.getRoles().stream()
								.map(role -> role.getRoleKey().toString()).collect(Collectors.toList()))
						.build(),
				HttpStatus.OK);
	}

	@PostMapping(value = "/register")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> register(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (StringUtils.isEmpty(signUpRequest.getUsername())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.USERNAME_EMPTY.getCode(), ErrorCode.USERNAME_EMPTY.getMessage(),
							ErrorCode.USERNAME_EMPTY.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (StringUtils.isEmpty(signUpRequest.getPassword())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.PASSWORD_EMPTY.getCode(), ErrorCode.PASSWORD_EMPTY.getMessage(),
							ErrorCode.PASSWORD_EMPTY.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (Objects.nonNull(userService.findByUsername(signUpRequest.getUsername()))) {
			return new ResponseEntity<>(
					new Response(ErrorCode.USER_ALREADY_EXIST.getCode(), ErrorCode.USER_ALREADY_EXIST.getMessage(),
							ErrorCode.USER_ALREADY_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		userService.save(signUpRequest);
		return new ResponseEntity<>(new Response(StatusCode.USER_CREATED.getCode(),
				StatusCode.USER_CREATED.getMessage(), StatusCode.USER_CREATED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);

	}

	@GetMapping(value = "/allRoles")
	public ResponseEntity<Map<RoleKey, String>> roles() {
		return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/allUsers")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<User>> users() {
		return new ResponseEntity<>(userService.users(), HttpStatus.OK);
	}

	@GetMapping(value = "/isUserExist/{username}")
	public ResponseEntity<Boolean> isUserExist(@PathVariable String username) {
		boolean isUserExist = false;
		if (Objects.nonNull(userService.findByUsername(username)))
			isUserExist = true;
		return new ResponseEntity<>(isUserExist, HttpStatus.OK);
	}

	@PostMapping(value = "/changePassword")
	public ResponseEntity<Response> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		if (StringUtils.isEmpty(changePasswordRequest.getOldPassword())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.OLDPASSWORD_EMPTY.getCode(), ErrorCode.OLDPASSWORD_EMPTY.getMessage(),
							ErrorCode.OLDPASSWORD_EMPTY.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (StringUtils.isEmpty(changePasswordRequest.getNewPassword())) {
			return new ResponseEntity<>(
					new Response(ErrorCode.NEWPASSWORD_EMPTY.getCode(), ErrorCode.NEWPASSWORD_EMPTY.getMessage(),
							ErrorCode.NEWPASSWORD_EMPTY.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!userService.matchOldPassword(changePasswordRequest)) {

			return new ResponseEntity<>(new Response(ErrorCode.OLDPASSWORD_INCORRECT.getCode(),
					ErrorCode.OLDPASSWORD_INCORRECT.getMessage(), ErrorCode.OLDPASSWORD_INCORRECT.toString(),
					HttpStatus.UNPROCESSABLE_ENTITY.name()), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		userService.updateUserPassword(changePasswordRequest);
		return new ResponseEntity<>(new Response(StatusCode.PASSWORD_CHANGED.getCode(),
				StatusCode.PASSWORD_CHANGED.getMessage(), StatusCode.PASSWORD_CHANGED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);

	}

	@DeleteMapping(value = "/deleteUser/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Response> deleteUser(@PathVariable Long userId) {
		if (!userService.findById(userId).isPresent()) {
			return new ResponseEntity<>(
					new Response(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getMessage(),
							ErrorCode.USER_NOT_EXIST.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		userService.delete(userId);
		return new ResponseEntity<>(new Response(StatusCode.USER_DELETED.getCode(),
				StatusCode.USER_DELETED.getMessage(), StatusCode.USER_DELETED.toString(), HttpStatus.OK.name()),
				HttpStatus.OK);
	}

}
