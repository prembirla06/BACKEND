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
package com.maxxsoft.microServices.mailService.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxxsoft.microServices.mailService.model.Mail;
import com.maxxsoft.microServices.mailService.service.EmailSenderService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mail")
@Transactional
public class MailController {

	@Autowired
	private EmailSenderService emailSenderService;

	@GetMapping(value = "/sendTestEmail")
	public void sendEmail() throws MessagingException, IOException {
		Mail mail = new Mail();
		mail.setFrom("maxxsoft@tecmaxx.de");
		mail.setMailTo("ms@algoson.com");
		mail.setSubject("Lieferung ihrer Bestellung von moebel-guenstig24.de");
		mail.setTemplateName("deliveryTour");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "Neo Maximillion");
		model.put("deliveryDateTime", LocalDateTime.now());
		model.put("deliveryPhoneNumber", "121212121212");
		model.put("${customerName}", "Neo");
		mail.setProps(model);

		emailSenderService.sendEmail(mail);
	}

	@PostMapping(value = "/sendEmail")
	public void sendEmail(@RequestBody Mail mail) throws MessagingException, IOException {
		emailSenderService.sendEmail(mail);
	}

}